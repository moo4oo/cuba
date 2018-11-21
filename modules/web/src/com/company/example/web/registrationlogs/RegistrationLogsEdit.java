package com.company.example.web.registrationlogs;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.RegistrationLogs;
import javax.inject.Inject;

public class RegistrationLogsEdit extends AbstractEditor<RegistrationLogs> {
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;
    boolean newItem = false;

    @Override
    protected void initNewItem(RegistrationLogs item) {
        super.initNewItem(item);
        newItem = true;
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number_reg_logs"));
        item.setCode(String.format("Ж%06d", item.getSerial_number()));
    }

    @Override
    protected boolean preClose(String actionId) {
        if(actionId.equals("windowClose") || actionId.equals("close")){
            if(getItem() != null) {
                Long number = getItem().getSerial_number();
                if (number != null) {
                    if(newItem)
                        uniqueNumbersHelperService.setNextUniqueNumber("serial_number_reg_logs", number-1);
                }
            }
        }
        return super.preClose(actionId);

    }
}