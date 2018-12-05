package com.company.example.web.outgoingdocuments;

import com.company.example.entity.OutgoingDocuments;
import com.company.example.service.OutgoingDocumentsService;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class OutgoingDocumentsBrowse extends AbstractLookup {
    @Inject
    private GroupTable<OutgoingDocuments> outgoingDocumentsesTable;
    @Inject
    private OutgoingDocumentsService outgoingDocumentsService;
    @Inject
    private UserSession userSession;
    private boolean active = false;
    private List<ProcTask> tasks = null;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        UUID currentUserId = userSession.getCurrentOrSubstitutedUser().getId();
        outgoingDocumentsesTable.addStyleProvider((doc, property) -> {
            if (doc.getState() != null && !doc.getState().getId().equals("Registered")) {
                if (!active) {
                    tasks = outgoingDocumentsService.getActiveDocTasks(doc.getId());
                    if (tasks != null && !tasks.isEmpty()) {
                        active = true;
                    }
                }
                if (tasks != null && !tasks.isEmpty()) {
                    for (ProcTask task : tasks) {
                        if (task.getEndDate() == null) {
                            User user = outgoingDocumentsService.getCurrentTaskUser(task.getId(), doc.getId());
                            if (user != null && user.getId().equals(currentUserId)) {
                                if (property != null && property.equals("state")) {
                                    active = false;
                                }
                                return "colored-cell-red";
                            }
                        }
                    }
                }
            }
            return null;
        });
    }

}