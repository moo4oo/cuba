package com.company.example.service;


import com.company.example.Request.FinishTaskRequest;
import com.company.example.Request.StartTaskRequest;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;

import java.util.List;

public interface OutgoingDocumentsProcessService {
    String NAME = "example_OutgoingDocumentsProcessService";
    List<ProcTask> getCurrentTasks();
    ProcTask finishTask(FinishTaskRequest finishTaskBody);
    ProcInstance startProcInstance(StartTaskRequest startProcBody);
}