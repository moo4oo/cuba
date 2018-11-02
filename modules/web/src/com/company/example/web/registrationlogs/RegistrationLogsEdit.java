package com.company.example.web.registrationlogs;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.RegistrationLogs;
import com.haulmont.cuba.gui.components.MaskedField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.TextInputField;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class RegistrationLogsEdit extends AbstractEditor<RegistrationLogs> {
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Named("fieldGroup.number")
    private TextField numberField;


    @Override
    protected void initNewItem(RegistrationLogs item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersService.getNextNumber("serial_number_reg_logs"));
        item.setCode(String.format("Ж%06d", item.getSerial_number()));
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);


    }
}