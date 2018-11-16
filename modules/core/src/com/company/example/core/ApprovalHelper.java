package com.company.example.core;

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
        try (Transaction tx = persistence.getTransaction()) {
            OutgoingDocuments doc = persistence.getEntityManager().find(OutgoingDocuments.class, entityId);
            if (doc != null) {
                doc.setState(state);
                if(state.equals("Registered")){
                    doc.setDate(new Date());
                    doc.setTopic(doc.getTopic() + "1");
                }
            }
            tx.commit();
        }
    }


}