package com.company.example.web.affairsnomenclature;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.AffairsNomenclature;
import com.haulmont.cuba.gui.components.TextField;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class AffairsNomenclatureEdit extends AbstractEditor<AffairsNomenclature> {


    @Named("fieldGroup.code")
    private TextField codeField;
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        codeField.setEditable(false);
    }

    @Override
    protected void initNewItem(AffairsNomenclature item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("serial_number_nomenclature"));
        item.setCode(String.format("НД%06d", item.getSerial_number()));
    }

    @Override
    protected boolean preClose(String actionId) {
        if(actionId.equals("windowClose") || actionId.equals("close")){
            if(getItem() != null) {
                Long number = getItem().getSerial_number();
                if (number != null && PersistenceHelper.isNew(getItem()))
                        uniqueNumbersHelperService.setNextUniqueNumber("serial_number_nomenclature", number-1);

            }
        }
        return super.preClose(actionId);
    }
}