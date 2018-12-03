package com.company.example.web.workers;

import com.company.example.entity.MacroEnum;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.company.example.entity.Workers;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.entity.User;
import org.apache.commons.fileupload.FileUpload;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WorkersEdit extends AbstractEditor<Workers> {


    @Inject
    private TextField patronymicField;
    @Inject
    private TextField secondNameField;
    @Inject
    private TextField firstNameField;
    @Inject
    private PickerField userField;
    @Inject
    private FileUploadField photoField;
    @Inject
    private Image workersPhotoView;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private DataSupplier dataSupplier;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
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

        photoField.addFileUploadSucceedListener(e ->{
            if(e.getFileName() != null){
                FileDescriptor fd = photoField.getFileDescriptor();
                try{
                    fileUploadingAPI.putFileIntoStorage(photoField.getFileId(), fd);
                }catch (FileStorageException err){
                    throw new RuntimeException("Error saving file to FileStorage", err);
                }
                getItem().setPhoto(dataSupplier.commit(fd));
                displayPhoto(getItem());
            }
        });
        photoField.addAfterValueClearListener(event -> {
            getItem().setPhoto(null);
            workersPhotoView.setVisible(false);
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
        firstNameField.setValue(firstName);
        secondNameField.setValue(secondName);
        patronymicField.setValue(middleName);


    }
    private void displayPhoto(Workers worker){
        if(worker.getPhoto() != null) {
            workersPhotoView.setSource(FileDescriptorResource.class).setFileDescriptor(worker.getPhoto());
            workersPhotoView.setVisible(true);
        }
    }
}