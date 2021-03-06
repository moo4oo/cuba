package com.company.example.service;


import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.security.entity.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface OutgoingDocumentsService {
    static final String PROCESS_CODE = "contractApproval";
    String NAME = "example_OutgoingDocumentsService";
    public User getCurrentTaskUser(UUID procTaskUUID, UUID docUUID);
    public List<ProcTask> getDocTasks(UUID docUUID);
    public List<ProcTask> getActiveDocTasks(UUID docUUID);
    public Workers getCurrentWorker(UUID userUUID);
    public User getDevHeaderUser(User user);
    public ProcActor createProcActor(String procRoleCode, ProcInstance procInstance, User user);
    public String getRegNumber(String f, Date date, String number, long serialNumber);
    List<OutgoingDocuments> getDocuments();
}