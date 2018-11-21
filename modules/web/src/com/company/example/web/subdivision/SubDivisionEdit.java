package com.company.example.web.subdivision;

import com.company.example.service.EditSubDivisionService;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.company.example.entity.SubDivision;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SubDivisionEdit extends AbstractEditor<SubDivision> {

    @Named("subDivisionFieldGroup.lead_subdivision")
    private LookupPickerField lead_subdivisionField;
    @Inject
    private DataManager dataManager;
    @Inject
    private Frame windowActions;

    @Override
    protected boolean preCommit() {
        return checkLooping(lead_subdivisionField.getValue());
    }

    @Named("subDivisionFieldGroup.departament_head")
    private PickerField departament_headField;
    @Inject
    private CollectionDatasource<SubDivision, UUID> subDivisionsDs;

    private boolean checkLooping(SubDivision subDiv){
        SubDivision sd = subDiv;
        SubDivision item = getItem();
        if(sd!=null)
        if(!sd.getId().equals(item.getId())){
            while(sd != null){
                if(item.getId().equals(sd.getId())){
                    showNotification("Select another Subdivision");
                    return false;
                }
                try {
                    sd = getSubDivision(sd.getId());
                }catch (Exception e){
                    return true;
                }
            }
        }else {
            return false;
        }
        return true;

    }
    private SubDivision getSubDivision(UUID subDivUUID){
        return dataManager.load(SubDivision.class).query("select e.lead_subdivision from example$SubDivision e where " +
                "e.id = :subDivUUID")
                .parameter("subDivUUID", subDivUUID)
                .one();
    }

    @Inject
    private EditSubDivisionService editSubDivisionService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);


        lead_subdivisionField.removeAllActions();
        lead_subdivisionField.addLookupAction();
        lead_subdivisionField.addOpenAction();
        lead_subdivisionField.addClearAction();
        lead_subdivisionField.getOpenAction().setEditScreenOpenType(WindowManager.OpenType.DIALOG);

        departament_headField.removeAllActions();
        departament_headField.addLookupAction();
        departament_headField.addOpenAction();
        departament_headField.addClearAction();
        departament_headField.getOpenAction().setEditScreenOpenType(WindowManager.OpenType.DIALOG);


        Entity div = WindowParams.ITEM.getEntity(params);
        setItem(div);

        List<SubDivision> list = editSubDivisionService.getSubDivisions();
        for(SubDivision d : list){
            subDivisionsDs.addItem(d);
        }
        subDivisionsDs.excludeItem(getItem());
        subDivisionsDs.refresh();

        Map<String, Object> key = new HashMap<>();
        if (getItem() != null) {
            key.put("subdiv_key", getItem());
            lead_subdivisionField.getLookupAction().setLookupScreenParams(key);
        }


        lead_subdivisionField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                    if(!checkLooping((SubDivision)e.getValue())){
                        windowActions.setEnabled(false);
                    }else{
                        windowActions.setEnabled(true);
                    }
            }else {
                windowActions.setEnabled(true);
            }
        });

    }
}