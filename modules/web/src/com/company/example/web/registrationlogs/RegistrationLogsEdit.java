package com.company.example.web.registrationlogs;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.RegistrationLogs;

import javax.inject.Inject;

public class RegistrationLogsEdit extends AbstractEditor<RegistrationLogs> {
    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @Override
    protected void initNewItem(RegistrationLogs item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersService.getNextNumber("serial_number_reg_logs"));
        item.setCode(String.format("Ж%06d", item.getSerial_number()));
    }
}