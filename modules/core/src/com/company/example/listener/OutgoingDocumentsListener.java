package com.company.example.listener;

import com.company.example.Title;
import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.RegistrationLogs;
import com.company.example.entity.Workers;
import com.company.example.service.OutgoingDocumentsService;
import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Component;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;

import javax.inject.Inject;
import java.util.Date;

@Component("example_OutgoingDocumentsListener")
public class OutgoingDocumentsListener implements BeforeInsertEntityListener<OutgoingDocuments>, BeforeUpdateEntityListener<OutgoingDocuments> {

    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;
    @Inject
    private DataManager dataManager;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private OutgoingDocumentsService outgoingDocumentsService;

    @Override
    public void onBeforeInsert(OutgoingDocuments entity, EntityManager entityManager) {
        if(entity.getSerial_number() == null) {
            entity.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("outgoing_doc"));
            entity.setRegistration_number(entity.getSerial_number() + "");
        }
        entity.setCreate_date(new Date());
        Title title = new Title();

        String date = null;
        if(entity.getDate() != null){
            date = entity.getDate().toString();
        }
        entity.setTitle(getTitleString(title, entity.getTopic(), entity.getAddressee().getShort_title(),
                entity.getDocument_type().getName(), date, entity.getRegistration_number()));
        User user = userSessionSource.getUserSession().getUser();
        Workers executor = dataManager.load(LoadContext.create(Workers.class).setView("workers-view").setQuery(LoadContext.createQuery(
                "select e from example$Workers e where e.user.id = :userId").setParameter("userId", user.getId())));
        entity.setExecutor(executor);
        entity.setAuthor(user);

    }

    private String getTitleString(Title title, String topic, String addresseTitle, String docType, String date, String regNumber) {
        if (topic != null)
            title.setTopic(topic);
        if (addresseTitle != null)
            title.setAddresse(addresseTitle);
        if (docType != null)
            title.setDocType(docType);
        if (date != null)
            title.setDate(date);
        if (regNumber != null)
            title.setRegNumber(regNumber);
        return title.getStringTitle();
    }



    @Override
    public void onBeforeUpdate(OutgoingDocuments entity, EntityManager entityManager) {
        OutgoingDocuments doc = dataManager.load(LoadContext.create(OutgoingDocuments.class).setView("main-outgoingDocuments-view").setId(entity.getId()));
        Title title = new Title();
        String topic = null;
        String addresseTitle = null;
        String docType = null;
        String date = null;
        String regNumber = null;
        if (entity.getTopic() != null) {
            topic = entity.getTopic();
        } else {
            topic = doc.getTopic();
        }
        addresseTitle = doc.getAddressee().getShort_title();
        if (entity.getAddressee() != null)
            if (entity.getAddressee().getShort_title() != null)
                addresseTitle = entity.getAddressee().getShort_title();
        docType = doc.getDocument_type().getName();
        if (entity.getDocument_type() != null)
            if (entity.getDocument_type().getName() != null)
                docType = entity.getDocument_type().getName();
        if (entity.getDate() != null) {
            date = entity.getDate().toString();
        } else {
            if(doc.getDate() != null)
            date = doc.getDate().toString();
        }
        if(entity.getRegistration_number() != null){
            regNumber = entity.getRegistration_number();
        }else {
            regNumber = doc.getRegistration_number();
        }
        if(entity.getLog() != null){
            RegistrationLogs logs = entity.getLog();
            String f = logs.getNumber_format();
            String result = outgoingDocumentsService.gerRegNumber(f, entity.getDate(), logs.getNumber(), entity.getSerial_number());
            entity.setRegistration_number(result);
        }
        if(entity.getAffair() != null)
            entity.setAffair_date(new Date());
        entity.setTitle(getTitleString(title, topic, addresseTitle, docType, date, regNumber));
        entity.setChange_date(new Date());

    }

}