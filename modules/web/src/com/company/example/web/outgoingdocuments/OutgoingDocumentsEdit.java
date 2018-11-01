package com.company.example.web.outgoingdocuments;

import com.company.example.entity.Workers;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.gui.components.*;
import com.company.example.entity.OutgoingDocuments;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.UUID;

public class OutgoingDocumentsEdit extends AbstractEditor<OutgoingDocuments> {
    @Inject
    private FieldGroup documentsFieldGroup;
    @Inject
    private FieldGroup mainFieldGroup;
    @Inject
    private FieldGroup matchingFieldGroup;
    @Inject
    private FieldGroup registrationsFieldGroup;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Named("mainFieldGroup.registration_number")
    private TextField registration_numberField;
    @Named("mainFieldGroup.date")
    private DateField dateField;
    @Named("mainFieldGroup.title")
    private TextField titleField;
    @Named("mainFieldGroup.executor")
    private PickerField executorField;
    @Named("mainFieldGroup.state")
    private LookupField stateField;
    @Named("mainFieldGroup.change_date")
    private DateField change_dateField;
    @Named("mainFieldGroup.create_date")
    private DateField create_dateField;
    @Named("mainFieldGroup.author")
    private PickerField authorField;
    @Inject
    private GroupDatasource<Workers, UUID> workersesDs;
    @Inject
    private UserSession userSession;

    @Override
    protected void initNewItem(OutgoingDocuments item) {
        super.initNewItem(item);

        item.setRegistration_number(uniqueNumbersService.getNextNumber("serial_number_outgoing")+"");
        item.setDate(new Date());
        item.setExecutor(workersesDs.getItem(userSession.getUser().getId()));



    }
}