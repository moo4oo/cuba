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
import java.text.SimpleDateFormat;
import java.util.*;

@Service(OutgoingDocumentsService.NAME)
public class OutgoingDocumentsServiceBean implements OutgoingDocumentsService {
    
    @Inject
    private Metadata metadata;
    @Inject
    private DataManager dataManager;
    @Inject
    private BpmEntitiesService bpmEntitiesService;

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
    public List<ProcTask> getActiveDocTasks(UUID docUUID) {
        return dataManager.load(ProcTask.class).query("select e from bpm$ProcTask e " +
                "where e.procInstance.entity.entityId " +
                "= :docUUID and e.endDate is NULL")
                .parameter("docUUID", docUUID)
                .list();
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

    @Override
    public String getRegNumber(String f, Date date, String number, long serialNumber) {
        StringBuilder sb = new StringBuilder(f);
        SimpleDateFormat fd = null;
        String replace = null;
        if(sb.lastIndexOf("dd.") >= 0) {
            if (sb.lastIndexOf("dd.MM") >= 0)
            {
                if(sb.lastIndexOf("dd.MM.yyyy") >= 0){
                    fd = new SimpleDateFormat("dd.MM.yyyy");
                    replace = "dd.MM.yyyy";
                }else if(sb.lastIndexOf("dd.MM.yy") >= 0){
                    fd = new SimpleDateFormat("dd.MM.yy");
                    replace = "dd.MM.yy";
                }else{
                    fd = new SimpleDateFormat("dd.MM");
                    replace = "dd.MM";
                }
            }
        }else if(sb.lastIndexOf("MM.yyyy") >= 0){
            fd = new SimpleDateFormat("MM.yyyy");
            replace = "MM.yyyy";
        }
        Date d = null;
        String result = "";
        if (date != null) {
            d = date;
            if(fd != null) {
                result = f.replace(replace, fd.format(d));
            }
        }else{
            if(fd != null){
                    result = f.replace(replace, "");
            }
        }
        String n = "";
        if(number != null) {
            n = String.format("%0" + number + "d", serialNumber);
        }else{
            n = serialNumber +"";
        }
        return result + " " + n;
    }

    @Override
    public List<OutgoingDocuments> getDocuments() {
        return dataManager.loadList(LoadContext.create(OutgoingDocuments.class).setView("outDocBrowse-view").setQuery(
                LoadContext.createQuery("select e from example$OutgoingDocuments e")
        ));
    }


}