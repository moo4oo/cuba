package com.company.example.web.outgoingdocuments;

import com.company.example.entity.*;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class OutgoingDocumentsEdit extends AbstractEditor<OutgoingDocuments> {
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Named("registrationsFieldGroup.registration_number")
    private TextField registration_numberField;
    @Named("registrationsFieldGroup.date")
    private DateField dateField;
    @Named("mainFieldGroup.title")
    private TextField titleField;
    @Named("mainFieldGroup.state")
    private LookupField stateField;
    @Named("mainFieldGroup.change_date")
    private DateField change_dateField;
    @Named("mainFieldGroup.create_date")
    private DateField create_dateField;
    @Named("mainFieldGroup.author")
    private PickerField authorField;
    @Named("mainFieldGroup.document_type")
    private PickerField document_typeField;
    @Named("mainFieldGroup.addressee")
    private PickerField addresseeField;
    @Inject
    private UserSession userSession;
    @Named("mainFieldGroup.topic")
    private TextField topicField;
    @Inject
    private DataManager dataManager;
    @Named("registrationsFieldGroup.log")
    private PickerField logField;
    @Named("registrationsFieldGroup.affair")
    private PickerField affairField;
    @Named("registrationsFieldGroup.affair_date")
    private DateField affair_dateField;


    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        titleField.setEditable(false);
        registration_numberField.setEditable(false);
        dateField.setEditable(false);
        authorField.setEditable(false);
        create_dateField.setEditable(false);
        change_dateField.setEditable(false);
        stateField.setEditable(false);
        dateField.setEditable(false);
        registration_numberField.setEditable(false);
        if(getItem() != null)
        initListeners(getItem());
    }

    @Override
    protected boolean preCommit() {
        if(getItem() != null)
        getItem().setDate(new Date());
        return super.preCommit();
    }

    @Override
    protected void initNewItem(OutgoingDocuments item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersService.getNextNumber("serial_number_outgoing"));
        item.setRegistration_number(item.getSerial_number() + "");
        item.setDate(new Date());
        item.setExecutor(getCurrentWorker(userSession.getUser().getId()));
        item.setAuthor(userSession.getUser());
        item.setCreate_date(new Date());
        initListeners(item);
    }

    private Workers getCurrentWorker(UUID userUUID){
        return dataManager.load(Workers.class).query("select e from example$Workers e where " +
                "e.user.id = :userUUID")
                .parameter("userUUID", userUUID)
                .one();
    }


    private void initListeners(OutgoingDocuments item){
        Title title = new Title();
        document_typeField.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                title.setDocType(((DocumentTypes) e.getValue()).getCode());
                item.setTitle(title.getStringTitle());
            }
        });
        registration_numberField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                title.setRegNumber((String)e.getValue());
                item.setTitle(title.getStringTitle());
            }
        });
        dateField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                title.setDate(dateField.getValue().toString());
                item.setTitle(title.getStringTitle());
            }
        });
        addresseeField.addValueChangeListener(e ->{
            if(e.getValue() != null){
                title.setAddresse(((Organizations)e.getValue()).getShort_title());
                item.setTitle(title.getStringTitle());
            }
        });
        topicField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                title.setTopic(topicField.getRawValue());
                item.setTitle(title.getStringTitle());
            }
        });
        logField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                RegistrationLogs logs = (RegistrationLogs)e.getValue();
                SimpleDateFormat fd = new SimpleDateFormat(logs.getNumber_format().getId());
                String number = " ";
                if(item.getLog().getNumber() != null)
                number = String.format("%0"+item.getLog().getNumber()+"d", item.getSerial_number());
                item.setRegistration_number("Исх - " + fd.format(item.getDate()) + " " + number);
            }
        });

        affairField.addValueChangeListener(e -> {
            if(e.getValue() != null){
                affair_dateField.setValue(new Date());
            }
        });

    }
    private class Title{
        private String docType = "";
        private String regNumber = "";
        private String date = "";
        private String addresse = "";
        private String topic = "";
        public String getStringTitle(){
           return docType + " №" + regNumber + " от " + date + " в " + addresse + ", " + topic;
        }
        public String getDocType() {
            return docType;
        }
        public void setDocType(String docType) {
            this.docType = docType;
        }
        public String getRegNumber() {
            return regNumber;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAddresse() {
            return addresse;
        }

        public void setAddresse(String addresse) {
            this.addresse = addresse;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}