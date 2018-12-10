package com.company.example.service;

import com.company.example.ExampleTestContainer;
import com.company.example.Request.FinishTaskRequest;
import com.company.example.Request.StartTaskRequest;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OutgoingDocumentsProcessServiceTest extends TestContainer {


    @ClassRule
    public static ExampleTestContainer cont = ExampleTestContainer.Common.INSTANCE;
    public FinishTaskRequest finishTaskBody = new FinishTaskRequest();
    public StartTaskRequest startTaskBody = new StartTaskRequest();
    OutgoingDocumentsProcessService bean;

    @Before
    public void initTest() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        LoadContext<ProcTask> docsLoadContext = new LoadContext<>(ProcTask.class)
                .setQuery(LoadContext.createQuery("select e from bpm$ProcTask e where e.procActor.user.id = :id order by e.startDate")
                        .setParameter("id", UUID.fromString("60885987-1b61-4247-94c7-dff348347f93")))
                .setView("procTask-view_1");
        List<ProcTask> list = dataManager.loadList(docsLoadContext);
        List<ProcTask> result = new ArrayList<>();
        for(ProcTask task : list){
            if(task.getEndDate() == null){
                result.add(task);
            }
        }
        if(result.size() > 0){
            finishTaskBody.setTaskId(result.get(result.size() - 1).getId().toString());
        }else{
            finishTaskBody.setTaskId("f264f2ff-e444-7c1a-85f9-3a978da875d6");
        }
        finishTaskBody.setComment("test");
        finishTaskBody.setOutcome("test");
        List<String> matchers = new ArrayList<>();
        finishTaskBody.setMatchers(matchers);


        startTaskBody.setDocId("ab55017b-1d7c-1b79-f145-b53168dfe78a");
        startTaskBody.setInit("60885987-1b61-4247-94c7-dff348347f93");
        startTaskBody.setSign("60885987-1b61-4247-94c7-dff348347f93");
        List<String> m = new ArrayList<>();
        m.add("60885987-1b61-4247-94c7-dff348347f93");
        startTaskBody.setMatching(m);


        bean = AppBeans.get(OutgoingDocumentsProcessService.class);
    }

    @Test
    public void test1startProcInstance() {
        ProcInstance procInstance = bean.startProcInstance(startTaskBody);
        
        assertTrue(procInstance.getActive() && (procInstance.getStartDate() != null));
        Set<ProcActor> procActorSet = procInstance.getProcActors();
        boolean checkInit = false, checkDevHead = false, checkSign = false, checkMatching = false, checkDocId = false;
        for (ProcActor procActor : procActorSet) {
            if (procActor.getProcRole().getCode().equals("init")) {
                if (procActor.getUser().getId().equals(UUID.fromString(startTaskBody.getInit())))
                    checkInit = true;
            } else if (procActor.getProcRole().getCode().equals("sign")) {
                if (procActor.getUser().getId().equals(UUID.fromString(startTaskBody.getSign())))
                    checkSign = true;
            } else if (procActor.getProcRole().getCode().equals("matching")) {
                if (procActor.getUser().getId().equals(UUID.fromString(startTaskBody.getMatching().get(0))))
                    checkMatching = true;
            } else if (procActor.getProcRole().getCode().equals("dev_head")) {
                DataManager dataManager = AppBeans.get(DataManager.class);
                User initUser = dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(startTaskBody.getInit())));
                if(initUser != null) {
                    Workers initWorker = dataManager.load(LoadContext.create(Workers.class).setQuery(
                            LoadContext.createQuery("select e from example$Workers e where e.user.id = :id")
                                    .setParameter("id", initUser.getId())
                    ).setView("workers-view"));
                    if(initWorker != null) {
                        User devHead = initWorker.getSub_division().getDepartament_head().getUser();
                        if(procActor.getUser().getId().equals(devHead.getId()))
                            checkDevHead = true;
                    }
                }
            }
            if (procActor.getProcInstance().getEntity().getEntityId().equals(UUID.fromString(startTaskBody.getDocId()))) {
                checkDocId = true;
            }else {
                checkDocId = false;
            }
        }
        assertTrue(checkInit && checkDocId && checkSign && checkMatching && checkDevHead);
    }

    @Test
    public void test2getCurrentTasks() {
        List<ProcTask> procTasks;
        procTasks = bean.getCurrentTasks();
        assertNotNull(procTasks);
        if(procTasks.size() > 0){
            ProcTask procTask = procTasks.get(procTasks.size() - 1);
            String docId = procTask.getProcInstance().getEntity().getEntityId().toString();
            assertNotNull(docId);
            finishTaskBody.setTaskId(docId);
        }
    }


    @Test
    public void test3finishTask() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        ProcTask procTask = bean.finishTask(finishTaskBody);
        ProcTask task = dataManager.load(LoadContext.create(ProcTask.class).setId(procTask.getId()));
        assertNotNull(task);
        assertTrue(task.getStartDate() != null && task.getEndDate() != null);
    }

}