package com.company.example.core;

import com.company.example.entity.DocState;
import com.company.example.entity.OutgoingDocuments;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;


import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component(ApprovalHelper.NAME)
public class ApprovalHelper {
    
    public static final String NAME = "demo_ApprovalHelper";
    @Inject
    private Persistence persistence;
    @Inject
    private DataManager dataManager;

    public void updateState(UUID entityId, String state) {
        DocState docState = DocState.fromId(state);
        try (Transaction tx = persistence.getTransaction()) {
            OutgoingDocuments doc = persistence.getEntityManager().find(OutgoingDocuments.class, entityId);
            if (doc != null) {
                doc.setState(docState);
                if(state.equals("Registered")){
                    doc.setDate(new Date());
                }
                tx.commit();
            }

        }
    }

    public boolean roleExists(String roleCode, UUID bpmProcInstanceId) {
        LoadContext<ProcActor> ctx = LoadContext.create(ProcActor.class).setView(View.MINIMAL);
        ctx.setQueryString("select pa from bpm$ProcActor pa where pa.procRole.code = :roleCode and " +
                "pa.procInstance.id = :bpmProcInstanceId")
                .setParameter("roleCode", roleCode)
                .setParameter("bpmProcInstanceId", bpmProcInstanceId);
        List<ProcActor> procActors = dataManager.loadList(ctx);
        boolean result = false;
        if(procActors.isEmpty())
            result = true;
        for(ProcActor actor : procActors){
            if(actor.getUser() == null){
                result = true;
            }
        }
        return result;
    }





}