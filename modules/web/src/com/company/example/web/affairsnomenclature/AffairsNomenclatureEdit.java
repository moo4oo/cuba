package com.company.example.web.affairsnomenclature;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.AffairsNomenclature;

import javax.inject.Inject;

public class AffairsNomenclatureEdit extends AbstractEditor<AffairsNomenclature> {


    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;

    @Override
    protected void initNewItem(AffairsNomenclature item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number_nomenclature"));
        item.setCode(String.format("НД%06d", item.getSerial_number()));
    }
}