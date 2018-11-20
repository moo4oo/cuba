package com.company.example.web.subdivision;

import com.company.example.entity.SubDivision;
import com.company.example.service.EditSubDivisionService;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SubDivisionBrowse extends AbstractLookup {

    @Inject
    private CollectionDatasource<SubDivision, UUID> subDivisionsDs;
    @Inject
    private EditSubDivisionService editSubDivisionService;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        List<SubDivision> list = editSubDivisionService.getSubDivisions();
        for(SubDivision d : list){
            subDivisionsDs.addItem(d);
        }
        if(params.containsKey("subdiv_key")) {
            SubDivision subDivision = (SubDivision)params.get("subdiv_key");
            if (subDivision != null) {
                subDivisionsDs.excludeItem(subDivision);
            }
        }
        subDivisionsDs.refresh();



    }

}