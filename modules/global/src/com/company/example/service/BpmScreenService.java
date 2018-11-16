package com.company.example.service;


import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.cuba.security.entity.User;

import java.util.ArrayList;
import java.util.List;

public interface BpmScreenService {
    String NAME = "example_BpmScreenService";
    ProcActor createProcActor(ProcRole procRole, ProcInstance procInstance, User user);
    List<ProcActor> setData(ProcInstance procInstance);
    ProcRole getProcRole();
}