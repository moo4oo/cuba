package com.company.example.web.subdivision;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.SubDivision;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.PickerField;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

public class SubDivisionEdit extends AbstractEditor<SubDivision> {

    @Named("fieldGroup.lead_subdivision")
    private PickerField lead_subdivisionField;
    @Inject
    private DataManager dataManager;
    @Inject
    private Frame windowActions;

    @Override
    protected boolean preCommit() {
        return checkLooping(lead_subdivisionField.getValue());
    }

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

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        Entity div = WindowParams.ITEM.getEntity(params);
        setItem(div);

        SubDivision subDivision = (SubDivision) WindowParams.ITEM.getEntity(params);
        if (subDivision != null) {
            lead_subdivisionField.getLookupAction().setLookupScreenParams(ParamsMap.of("subdiv_key", subDivision));
        }
        lead_subdivisionField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                    if(!checkLooping((SubDivision)e.getValue())){
                        windowActions.setEnabled(false);
                    }else{
                        windowActions.setEnabled(true);
                    }
            }
        });
    }
}