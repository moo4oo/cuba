package com.company.example.service;

import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;

@Service(OutgoingDocumentsService.NAME)
public class OutgoingDocumentsServiceBean implements OutgoingDocumentsService {

    @Inject
    private Metadata metadata;
    @Inject
    private DataManager dataManager;
    @Inject
    private BpmEntitiesService bpmEntitiesService;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;

    @Override
    public User getCurrentTaskUser(UUID procTaskUUID, UUID docUUID) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("procTaskUUID", procTaskUUID);
        parameters.put("docUUID", docUUID);
        return dataManager.load(User.class).query("select e.procActor.user from bpm$ProcTask e " +
                "where e.procInstance.entity.entityId " +
                "= :docUUID and e.id = :procTaskUUID")
                .setParameters(parameters)
                .one();
    }

    @Override
    public List<ProcTask> getDocTasks(UUID docUUID) {
        return dataManager.load(ProcTask.class).query("select e from bpm$ProcTask e " +
                "where e.procInstance.entity.entityId " +
                "= :docUUID")
                .parameter("docUUID", docUUID)
                .list();
    }

    @Override
    public Workers getCurrentWorker(UUID userUUID) {
        return dataManager.load(LoadContext.create(Workers.class).setQuery(LoadContext.createQuery(
                "select e from example$Workers e where e.user.id = :userUUID")
                .setParameter("userUUID", userUUID)).setView("workers-view"));
    }

    @Override
    public User getDevHeaderUser(User user) {
        return dataManager.load(User.class).query("select e.sub_division.departament_head.user from example$Workers e where " +
                "e.user.id = :userUUID")
                .parameter("userUUID", user.getId())
                .one();
    }

    @Override
    public ProcActor createProcActor(String procRoleCode, ProcInstance procInstance, User user) {
        ProcActor initiatorProcActor = metadata.create(ProcActor.class);
        initiatorProcActor.setUser(user);
        ProcRole initiatorProcRole = bpmEntitiesService.findProcRole(PROCESS_CODE, procRoleCode, View.MINIMAL);
        initiatorProcActor.setProcRole(initiatorProcRole);
        initiatorProcActor.setProcInstance(procInstance);
        return initiatorProcActor;
    }


}