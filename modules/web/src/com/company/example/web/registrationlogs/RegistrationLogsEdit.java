package com.company.example.web.registrationlogs;

import com.company.example.listener.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.RegistrationLogs;
import javax.inject.Inject;

public class RegistrationLogsEdit extends AbstractEditor<RegistrationLogs> {
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;

    @Override
    protected void initNewItem(RegistrationLogs item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number_reg_logs"));
        item.setCode(String.format("Ж%06d", item.getSerial_number()));
    }
}