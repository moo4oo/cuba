package com.company.example.web.organizations;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.Organizations;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class OrganizationsEdit extends AbstractEditor<Organizations> {
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;
    boolean newItem = false;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

    }

    @Override
    protected void initNewItem(Organizations item) {
        super.initNewItem(item);
        newItem = true;
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number_organizations"));
        item.setCode(String.format("ОРГ%06d", item.getSerial_number()));
    }

    @Override
    protected boolean preClose(String actionId) {
        if(actionId.equals("windowClose") || actionId.equals("close")){
            if(getItem() != null) {
                Long number = getItem().getSerial_number();
                if (number != null) {
                    if(newItem)
                        uniqueNumbersHelperService.setNextUniqueNumber("serial_number_organizations", number-1);
                }
            }
        }
        return super.preClose(actionId);
    }
}