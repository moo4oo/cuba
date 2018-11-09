package com.company.example.web.subdivision;

import com.company.example.entity.SubDivision;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class SubDivisionBrowse extends AbstractLookup {

    @Inject
    private CollectionDatasource<SubDivision, UUID> subDivisionsDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        if(params.containsKey("subdiv_key")) {
            SubDivision subDivision = (SubDivision)params.get("subdiv_key");
            if (subDivision != null) {
                subDivisionsDs.excludeItem(subDivision);
            }
        }
    }

}