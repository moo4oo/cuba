package com.company.example.service;

import com.company.example.Request.FinishTaskRequest;
import com.company.example.Request.StartTaskRequest;
import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.SubDivision;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service(OutgoingDocumentsProcessService.NAME)
public class OutgoingDocumentsProcessServiceBean implements OutgoingDocumentsProcessService {

    @Inject
    private DataManager dataManager;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private ProcessRuntimeService processRuntimeService;
    @Inject
    private Metadata metadata;
    @Inject
    private BpmEntitiesService bpmEntitiesService;
    private String PROCESS_CODE = "contractApproval";

    @Override
    public List<ProcTask> getCurrentTasks() {
        LoadContext<ProcTask> docsLoadContext = new LoadContext<>(ProcTask.class)
                .setQuery(LoadContext.createQuery("select e from bpm$ProcTask e where e.procActor.user.id = :id order by e.startDate")
                        .setParameter("id", userSessionSource.getUserSession().getCurrentOrSubstitutedUser().getId()))
                .setView("procTask-view_1");

        List<ProcTask> list = dataManager.loadList(docsLoadContext);
        List<ProcTask> result = new ArrayList<>();
        for(ProcTask task : list){
            if(task.getEndDate() == null){
                result.add(task);
            }
        }
        return result;
    }

    @Override
    public ProcTask finishTask(FinishTaskRequest finishTaskBody) {

        ProcTask procTask = dataManager.load(LoadContext.create(ProcTask.class).setId(UUID.fromString(finishTaskBody.getTaskId())).setView("procTask-view"));
        if (procTask.getName() != null && procTask.getName().equals("Доработка документа") && finishTaskBody.getOutcome().equals("To renegotiate")
        && finishTaskBody.getMatchers() != null) {
                        ProcInstance procInstance = procTask.getProcInstance();
                        ProcRole procRole = null;
                        for (ProcActor actor : procInstance.getProcActors()) {
                            if (actor.getProcRole().getCode().equals("matching")) {
                                procRole = actor.getProcRole();
                            }
                        }
                        for (String id : finishTaskBody.getMatchers()) {
                            ProcActor actor = metadata.create(ProcActor.class);
                            actor.setProcRole(procRole);
                            actor.setUser(dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(id))));
                            actor.setProcInstance(procInstance);
                            dataManager.commit(actor);
                        }
                    }
        processRuntimeService.completeProcTask(procTask, finishTaskBody.getOutcome(), finishTaskBody.getComment(), null);
        return procTask;
    }

    @Override
    public ProcInstance startProcInstance(StartTaskRequest startProcBody) {
        User init = dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(startProcBody.getInit())));
        User sign = dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(startProcBody.getSign())));

        OutgoingDocuments doc = dataManager.load(LoadContext.create(OutgoingDocuments.class).setId(UUID.fromString(startProcBody.getDocId()))
                .setView("main-outgoingDocuments-view"));
        Workers initWorker = dataManager.load(LoadContext.create(Workers.class)
                .setQuery(LoadContext
                        .createQuery("select e from example$Workers e where e.user.id = :id")
                        .setParameter("id", init.getId()))
                .setView("workers-view"));
        SubDivision initSubDivision = initWorker.getSub_division();
        Workers subDivHead = null;
        if(initSubDivision != null)
            subDivHead = initSubDivision.getDepartament_head();
        User devHead = null;
        if(subDivHead != null)
            devHead = subDivHead.getUser();
        List<User> matching = new ArrayList<>();
        for (String matcher : startProcBody.getMatching()) {
            matching.add(dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(matcher))));
        }
        BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(PROCESS_CODE)
                .addProcActor("init", init)
                .addProcActor("sign", sign)
                .setEntity(doc);
        if(devHead != null){
            procInstanceDetails.addProcActor("dev_head", devHead);
        }
        for (User user : matching) {
            procInstanceDetails.addProcActor("matching", user);
        }

        ProcInstance procInstance = bpmEntitiesService.createProcInstance(procInstanceDetails);
        processRuntimeService.startProcess(procInstance, "start process", null);

        return procInstance;
    }
}