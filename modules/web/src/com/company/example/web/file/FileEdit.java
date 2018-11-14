package com.company.example.web.file;

import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.company.example.entity.File;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.PickerField;

import javax.inject.Named;
import java.util.Date;
import java.util.Map;

public class FileEdit extends AbstractEditor<File> {
    @Named("fieldGroup.create_time")
    private DateField create_timeField;
    @Named("fieldGroup.update_time")
    private DateField update_timeField;
    @Named("fieldGroup.outDoc")
    private PickerField outDocField;

    @Override
    protected boolean preCommit() {
        if(getItem() != null){
           getItem().setUpdate_time(new Date());
        }
        return super.preCommit();
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        setItem(WindowParams.ITEM.getEntity(params));
        if(params.containsKey("doc")){
            outDocField.setValue(params.get("doc"));
            outDocField.setEditable(false);
        }
        create_timeField.setEditable(false);
        update_timeField.setEditable(false);


    }

    @Override
    protected void initNewItem(File item) {
        super.initNewItem(item);
        item.setCreate_time(new Date());
    }
}