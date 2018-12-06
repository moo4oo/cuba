package com.company.example.web.subdivision;

import com.company.example.service.EditSubDivisionService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.company.example.entity.SubDivision;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SubDivisionEdit extends AbstractEditor<SubDivision> {

    @Inject
    private DataManager dataManager;
    @Inject
    private Frame windowActions;
    @Inject
    private LookupPickerField leadDivisionPickerField;

    @Override
    protected boolean preCommit() {
        return checkLooping(leadDivisionPickerField.getValue());
    }

    @Inject
    private CollectionDatasource<SubDivision, UUID> subDivisionsDs;
    @Inject
    private LookupPickerField depHeadPickerField;

    private boolean checkLooping(SubDivision subDiv){
        SubDivision sd = subDiv;
        SubDivision item = getItem();
        if(sd != null && !sd.getId().equals(item.getId())){
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
            leadDivisionPickerField.getLookupAction().setLookupScreenParams(key);
        }


        leadDivisionPickerField.addValueChangeListener(e -> {
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