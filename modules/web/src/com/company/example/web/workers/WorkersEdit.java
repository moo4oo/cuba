package com.company.example.web.workers;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.company.example.entity.Workers;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WorkersEdit extends AbstractEditor<Workers> {


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
    @Named("fieldGroup.patronymic")
    private TextField patronymicField;
    @Named("fieldGroup.sub_division")
    private PickerField sub_divisionField;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        sub_divisionField.removeAllActions();
        sub_divisionField.addLookupAction();
        sub_divisionField.addOpenAction();
        sub_divisionField.addClearAction();
        sub_divisionField.getOpenAction().setEditScreenOpenType(WindowManager.OpenType.DIALOG);
        

        Workers worker = (Workers) WindowParams.ITEM.getEntity(params);
        setItem(worker);
        if(getItem() != null) {
            if (getItem().getPhoto() != null) {
                displayPhoto(getItem());
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
        String firstName = user.getFirstName();
        String secondName = user.getLastName();
        String middleName = user.getMiddleName();
        if(firstName == null){
            firstName = " ";
        }
        if(secondName == null){
            secondName = " ";
        }
        if(middleName == null){
            middleName = " ";
        }
        first_nameField.setValue(firstName);
        second_nameField.setValue(secondName);
        patronymicField.setValue(middleName);


    }
    private void displayPhoto(Workers worker){
        workersPhotoView.setSource(FileDescriptorResource.class).setFileDescriptor(worker.getPhoto());
        workersPhotoView.setVisible(true);
    }
}