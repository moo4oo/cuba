package com.company.example.web.outgoingdocuments;

import com.company.example.entity.OutgoingDocuments;
import com.company.example.service.OutgoingDocumentsService;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class OutgoingDocumentsBrowse extends AbstractLookup {
    @Inject
    private GroupTable<OutgoingDocuments> outgoingDocumentsesTable;
    @Inject
    private OutgoingDocumentsService outgoingDocumentsService;
    @Inject
    private UserSession userSession;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);


        User currentUser = userSession.getUser();
        outgoingDocumentsesTable.addStyleProvider(new Table.StyleProvider<OutgoingDocuments>() {
            @Override
            public String getStyleName(OutgoingDocuments doc, @Nullable String property) {
                if(doc != null){
                    List<ProcTask> tasks = outgoingDocumentsService.getDocTasks(doc.getId());
                    for(ProcTask task : tasks){
                        if(task.getEndDate() == null){
                            User user = outgoingDocumentsService.getCurrentTaskUser(task.getId(), doc.getId());
                            if(user != null){
                                if(user.getId().equals(currentUser.getId())){
                                    return "colored-cell-red";
                                }
                            }
                        }
                    }


                }
                return null;
            }
        });
    }
}