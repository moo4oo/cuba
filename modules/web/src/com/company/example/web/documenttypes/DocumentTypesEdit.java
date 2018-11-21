package com.company.example.web.documenttypes;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.DocumentTypes;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class DocumentTypesEdit extends AbstractEditor<DocumentTypes> {

    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;
    @Named("fieldGroup.code")
    private TextField codeField;
    boolean newItem = false;

    @Override
    protected boolean preClose(String actionId) {
        if(actionId.equals("windowClose") || actionId.equals("close")){
            if(getItem() != null) {
                Long number = getItem().getSerial_number();
                if (number != null) {
                    if(newItem)
                    uniqueNumbersHelperService.setNextUniqueNumber("serial_number", number-1);
                }
            }
        }
        return super.preClose(actionId);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        codeField.setEditable(false);
    }

    @Override
    protected void initNewItem(DocumentTypes item) {
        super.initNewItem(item);
        newItem = true;
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number"));
        item.setCode(String.format("ВД%06d", item.getSerial_number()));
    }
}