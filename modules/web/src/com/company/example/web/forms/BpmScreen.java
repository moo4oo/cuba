package com.company.example.web.forms;

import com.company.example.entity.OutgoingDocuments;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.gui.form.ProcForm;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ResizableTextArea;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.*;

public class BpmScreen extends AbstractWindow implements ProcForm {

    @Inject
    private CollectionDatasource<ProcActor, UUID> procActorsDs;
    @Inject
    private ResizableTextArea commentTextArea;
    private boolean comm = false;

    @Override
    public String getComment() {
        return commentTextArea.getValue();
    }

    private ProcRole procRole = null;


    @Override
    protected boolean preClose(String actionId) {
        if (comm) {
            showNotification("COMMENTS REQUIRED");
            return false;
        }
        return true;
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        Set<ProcActor> procActors = ((ProcInstance) params.get("procInstance")).getProcActors();
        for (ProcActor actor : procActors) {

            if (actor.getProcRole().getCode().equals("matching")) {
                procActorsDs.includeItem(actor);
                procRole = actor.getProcRole();
            }
        }

    }

    @Override
    public Map<String, Object> getFormResult() {
        return new HashMap<>();
    }

    public void onWindowCommit() {
        if (commentTextArea.getValue() != null) {
            close(COMMIT_ACTION_ID);
        } else {
            showNotification("COMMENTS REQUIRED");
        }
    }

    public void onWindowClose() {
        close(CLOSE_ACTION_ID);
    }
    public void onAddButtonClick() {

    }
}