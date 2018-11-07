package com.company.example.core;

import com.company.example.entity.MatchingTable;
import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.Workers;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;

import javax.inject.Inject;
import java.util.UUID;

@Component(ApprovalHelper.NAME)
public class ApprovalHelper {
    public static final String NAME = "demo_ApprovalHelper";
    @Inject
    private Persistence persistence;
    @Inject
    private DataManager dataManager;


    public void updateState(UUID entityId, String state) {
        try (Transaction tx = persistence.getTransaction()) {
            OutgoingDocuments doc = persistence.getEntityManager().find(OutgoingDocuments.class, entityId);
            if (doc != null) {
                doc.setState(state);

            }
            tx.commit();
        }
    }



    private Workers getCurrentWorker(UUID userUUID){
        return dataManager.load(Workers.class).query("select e from example$Workers e where " +
                "e.user.id = :userUUID")
                .parameter("userUUID", userUUID)
                .one();
    }
}