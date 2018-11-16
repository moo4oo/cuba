package com.company.example.web.documenttypes;

import com.company.example.listener.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.DocumentTypes;

import javax.inject.Inject;

public class DocumentTypesEdit extends AbstractEditor<DocumentTypes> {

    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;

    @Override
    protected void initNewItem(DocumentTypes item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number"));
        item.setCode(String.format("ВД%06d", item.getSerial_number()));
    }
}