package com.company.example.web.organizations;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.Organizations;

import javax.inject.Inject;

public class OrganizationsEdit extends AbstractEditor<Organizations> {
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;

    @Override
    protected void initNewItem(Organizations item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number_organizations"));
        item.setCode(String.format("ОРГ%06d", item.getSerial_number()));
    }
}