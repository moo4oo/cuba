package com.company.example.web.forms;

import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.gui.form.ProcForm;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import java.util.*;

public class BpmScreen extends AbstractWindow implements ProcForm {

    @Inject
    private CollectionDatasource<ProcActor, UUID> procActorsDs;
    @Inject
    private ResizableTextArea commentTextArea;
    private boolean comm = false;
    @Inject
    private Metadata metadata;
    @Inject
    private DataManager dataManager;

    @Override
    public String getComment() {
        return commentTextArea.getValue();
    }

    private ProcRole procRole = null;
    private ProcInstance procInstance = null;
    private List<ProcActor> actors = new ArrayList<>();


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
        procInstance =((ProcInstance) params.get("procInstance"));
        Set<ProcActor> procActors = procInstance.getProcActors();
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
            for(ProcActor actor : actors){
                dataManager.commit(actor);
            }
            close(COMMIT_ACTION_ID);
        } else {
            showNotification("COMMENTS REQUIRED");
        }
    }

    public void onWindowClose() {
        for(ProcActor actor : actors){
            procActorsDs.excludeItem(actor);
        }
        close(CLOSE_ACTION_ID);
    }

    public void onAddButtonClick() {
        openLookup(User.class, items -> {
            User user = (User) items.toArray()[0];
            ProcActor actor = metadata.create(ProcActor.class);
            actor.setProcRole(procRole);
            actor.setUser(user);
            actor.setProcInstance(procInstance);
            actors.add(actor);
            procActorsDs.includeItem(actor);

        }, WindowManager.OpenType.DIALOG);

    }
}