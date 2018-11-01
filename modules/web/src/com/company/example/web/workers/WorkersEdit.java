package com.company.example.web.workers;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.company.example.entity.Workers;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.Objects;

public class WorkersEdit extends AbstractEditor<Workers> {


    @Inject
    private FieldGroup fieldGroup;
    @Named("fieldGroup.user")
    private PickerField userField;
    @Named("fieldGroup.second_name")
    private TextField second_nameField;
    @Named("fieldGroup.first_name")
    private TextField first_nameField;
    @Named("fieldGroup.photo")
    private FileUploadField photoField;
    @Inject
    private Image workersPhotoView;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        Workers worker = (Workers) WindowParams.ITEM.getEntity(params);
        if(worker != null) {
            if (worker.getPhoto() != null) {
                displayPhoto(worker);
            }
        }
        userField.addValueChangeListener(e -> {
            if(!Objects.equals(e.getValue(), e.getPrevValue())) {
                User user = (User) e.getValue();
                if (user != null) {
                    setFirstLastNames(user);
                }
            }
        });


        photoField.addFileUploadSucceedListener(e -> {
            if(e.getFileName() != null){
                Workers w = getItem();
                displayPhoto(w);
            }
        });

    }
    private void setFirstLastNames(User user){
        first_nameField.setValue(user.getFirstName());
        second_nameField.setValue(user.getLastName());

    }
    private void displayPhoto(Workers worker){
        workersPhotoView.setSource(FileDescriptorResource.class).setFileDescriptor(worker.getPhoto());
        workersPhotoView.setVisible(true);
    }
}