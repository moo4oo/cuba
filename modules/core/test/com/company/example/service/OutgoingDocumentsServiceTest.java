package com.company.example.service;

import com.company.example.ExampleTestContainer;
import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class OutgoingDocumentsServiceTest {

    @ClassRule
    public static ExampleTestContainer cont = ExampleTestContainer.Common.INSTANCE;

    OutgoingDocumentsService bean;
    DataManager dataManager;
    UserSessionSource userSessionSource;
    List<ProcTask> procTask;
    List<ProcTask> resultProcTasks;
    ProcTask task;
    UUID docId;
    UUID taskId;
    User expectedUser;


    OutgoingDocuments expectedDoc;
    @Before
    public void init() {

        bean = AppBeans.get(OutgoingDocumentsService.class);
        dataManager = AppBeans.get(DataManager.class);
        userSessionSource = AppBeans.get(UserSessionSource.class);

        initForGetCurrentTaskUser();
        initForGetDocTasks();
    }

    private void initForGetCurrentTaskUser() {
        procTask = dataManager.loadList(LoadContext.create(ProcTask.class).setQuery(LoadContext.createQuery(
                "select e from bpm$ProcTask e where e.procActor.user.id = :id order by e.startDate")
                .setParameter("id", userSessionSource.getUserSession().getUser().getId()))
                .setView("procTask-view"));
        resultProcTasks = new ArrayList<>();
        for (ProcTask task : procTask) {
            if (task.getEndDate() == null) {
                resultProcTasks.add(task);
            }
        }
        if (resultProcTasks.size() > 0) {
            ProcTask task = resultProcTasks.get(resultProcTasks.size() - 1);
            docId = task.getProcInstance().getEntity().getEntityId();
            taskId = task.getId();
            expectedUser = task.getProcActor().getUser();
        }

    }

    @Test
    public void getCurrentTaskUser() {
        if(expectedUser != null) {
            User user = bean.getCurrentTaskUser(taskId, docId);
            assertNotNull(user);
            assertEquals(expectedUser.getId(), user.getId());
        }
    }

    private void initForGetDocTasks(){
        expectedDoc = dataManager.load(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                "select e from example$OutgoingDocuments e")).setView("main-outgoingDocuments-view"));
    }
    @Test
    public void getDocTasks() {
        List<ProcTask> doc = bean.getDocTasks(expectedDoc.getId());

    }

    @Test
    public void getCurrentWorker() {
        Workers worker = bean.getCurrentWorker(userSessionSource.getUserSession().getUser().getId());
        assertEquals("Administrator", worker.getUser().getName());
    }

    @Test
    public void getDevHeaderUser() {
    }

    @Test
    public void createProcActor() {

    }
    @Test
    public void getRegNumber(){
        String f = "ИСХ - dd.MM.yyyy -";
        Date date = new Date();
        String number = "5";
        long serialNumber = 5555;
        SimpleDateFormat fd = new SimpleDateFormat("dd.MM.yyyy");
        String expected = "ИСХ - "+fd.format(date) + " - 05555";
        String result = bean.getRegNumber(f, date, number, serialNumber);
        assertEquals(expected,result);
    }
}