package com.company.bpmdemo.core;

import com.company.example.entity.OutgoingDocuments;
import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import javax.inject.Inject;
import java.util.UUID;

@Component("demo_ApprovalHelper")
public class ApprovalHelper {

    @Inject
    private Persistence persistence;

    public void updateState(UUID entityId, String state) {
        try (Transaction tx = persistence.getTransaction()) {
            OutgoingDocuments doc = persistence.getEntityManager().find(OutgoingDocuments.class, entityId);
            if (doc != null) {
                doc.setState(state);
            }
            tx.commit();
        }
    }
}