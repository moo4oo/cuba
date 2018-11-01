package com.company.example.web.organizations;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.Organizations;

import javax.inject.Inject;

public class OrganizationsEdit extends AbstractEditor<Organizations> {
    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @Override
    protected void initNewItem(Organizations item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersService.getNextNumber("serial_number_organizations"));
        item.setCode(String.format("ОРГ%06d", item.getSerial_number()));
    }
}