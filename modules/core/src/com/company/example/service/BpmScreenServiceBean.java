package com.company.example.service;

import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service(BpmScreenService.NAME)
public class BpmScreenServiceBean implements BpmScreenService {


    
    private ProcRole procRole;
    private ProcInstance procInstance = null;

    public ProcRole getProcRole() {
        return procRole;
    }

    public void setProcRole(ProcRole procRole) {
        this.procRole = procRole;
    }

    public ProcInstance getProcInstance() {
        return procInstance;
    }

    public void setProcInstance(ProcInstance procInstance) {
        this.procInstance = procInstance;
    }

    @Inject
    private Metadata metadata;

    @Override
    public ProcActor createProcActor(ProcRole procRole, ProcInstance procInstance, User user) {
        ProcActor actor = metadata.create(ProcActor.class);
        actor.setProcRole(procRole);
        actor.setUser(user);
        actor.setProcInstance(procInstance);
        return actor;
    }

    @Override
    public List<ProcActor> setData(ProcInstance procInstance) {

        List<ProcActor> actors = new ArrayList<>();
        Set<ProcActor> procActors = procInstance.getProcActors();
        for (ProcActor actor : procActors) {
            if (actor.getProcRole().getCode().equals("matching")) {
                procRole = actor.getProcRole();
                actors.add(actor);
            }
        }

        return actors;
    }
}