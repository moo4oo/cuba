package com.company.example.core;

import com.company.example.entity.DocState;
import com.company.example.entity.OutgoingDocuments;
import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;


import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

@Component(ApprovalHelper.NAME)
public class ApprovalHelper {
    public static final String NAME = "demo_ApprovalHelper";
    @Inject
    private Persistence persistence;


    public void updateState(UUID entityId, String state) {
        DocState docState = DocState.fromId(state);
        try (Transaction tx = persistence.getTransaction()) {
            OutgoingDocuments doc = persistence.getEntityManager().find(OutgoingDocuments.class, entityId);
            if (doc != null) {
                doc.setState(docState);
                if(state.equals("Registered")){
                    doc.setDate(new Date());
                }
            }
            tx.commit();
        }
    }


}