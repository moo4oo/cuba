package com.company.example.web.subdivision;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.SubDivision;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.security.entity.Group;

import javax.inject.Inject;
import javax.inject.Named;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SubDivisionEdit extends AbstractEditor<SubDivision> {

    @Named("fieldGroup.lead_subdivision")
    private PickerField lead_subdivisionField;


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        SubDivision subDivision = (SubDivision) WindowParams.ITEM.getEntity(params);
        if(subDivision != null) {
            lead_subdivisionField.getLookupAction().setLookupScreenParams(ParamsMap.of("subdiv_key", subDivision));
        }
        lead_subdivisionField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChanged(ValueChangeEvent e) {
                
            }
        });


    }
}