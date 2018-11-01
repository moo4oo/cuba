package com.company.example.web.documenttypes;

import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.DocumentTypes;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Inject;
import javax.inject.Named;

public class DocumentTypesEdit extends AbstractEditor<DocumentTypes> {

    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @Named("fieldGroup.code")
    private TextField codeField;

    @Override
    protected void initNewItem(DocumentTypes item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersService.getNextNumber("serial_number"));
        item.setCode(String.format("ВД%06d", item.getSerial_number()));
    }
}