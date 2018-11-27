package com.company.example.web.outgoingdocuments;

import com.company.example.Title;
import com.company.example.entity.*;
import com.company.example.service.OutgoingDocumentsService;
import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.gui.procactions.ProcActionsFrame;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.reports.gui.actions.EditorPrintFormAction;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class OutgoingDocumentsEdit extends AbstractEditor<OutgoingDocuments> {
    private static final String PROCESS_CODE = "contractApproval";

    @Inject
    private ProcActionsFrame procActionsFrame;
    @Inject
    private UniqueNumbersHelperService uniqueNumbersHelperService;
    @Inject
    @Named("registrationsFieldGroup.date")
    private DateField dateField;
    @Named("registrationsFieldGroup.registration_number")
    private TextField registration_numberField;
    @Inject
    private ResizableTextArea titleTextArea;
    @Inject
    private UserSession userSession;
    @Inject
    private ResizableTextArea topicTextArea;
    @Named("registrationsFieldGroup.log")
    private PickerField logField;
    @Named("registrationsFieldGroup.affair")
    private PickerField affairField;
    @Named("registrationsFieldGroup.affair_date")
    private DateField affair_dateField;
    @Inject
    private Button printCardInfoBtn;
    @Inject
    private Button registrationBtn;
    @Inject
    private Table<FileDescriptor> filesTable;
    @Inject
    private OutgoingDocumentsService outgoingDocumentsService;
    @Inject
    private LookupPickerField signPickerField;
    @Inject
    private LookupPickerField executorPickerField;
    private boolean newItem = false;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private FileMultiUploadField multiUpload;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private PickerField authorPickerField;
    @Inject
    private DateField changeDateField;
    @Inject
    private DateField createDateField;
    @Inject
    private TextField stateTextField;
    @Inject
    private TextField registrationNumberTextArea;
    @Inject
    private LookupPickerField docTypePickerField;
    @Inject
    private LookupPickerField addresseePickerField;
    @Inject
    private DateField regDateField;
    @Inject
    private GroupBoxLayout procActionsBox;
    @Inject
    private Button startProcButton;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        signPickerField.removeAllActions();
        signPickerField.addLookupAction();
        signPickerField.addOpenAction();
        signPickerField.addClearAction();
        signPickerField.getOpenAction().setEditScreenOpenType(WindowManager.OpenType.DIALOG);

        executorPickerField.removeAllActions();
        executorPickerField.addLookupAction();
        executorPickerField.addOpenAction();
        executorPickerField.addClearAction();
        executorPickerField.getOpenAction().setEditScreenOpenType(WindowManager.OpenType.DIALOG);


        printCardInfoBtn.setAction(new EditorPrintFormAction("docCardReport", this, null));
        startProcButton.setVisible(false);
        startProcButton.setEnabled(false);
        titleTextArea.setEditable(false);
        registration_numberField.setEditable(false);
        dateField.setEditable(false);
        regDateField.setEditable(false);
        authorPickerField.setEditable(false);
        createDateField.setEditable(false);
        changeDateField.setEditable(false);
        stateTextField.setEditable(false);
        registrationNumberTextArea.setEditable(false);
        registration_numberField.setEditable(false);
        Entity docs = WindowParams.ITEM.getEntity(params);
        setItem(docs);
        if (getItem() != null) {
            if(!newItem) {
                initListeners(getItem());
                List<ProcTask> tasks = outgoingDocumentsService.getDocTasks(getItem().getId());
                for (ProcTask task : tasks) {
                    if (task.getEndDate() == null) {
                        User user = outgoingDocumentsService.getCurrentTaskUser(task.getId(), getItem().getId());
                        if (user == null) {
                            procActionsFrame.setVisible(false);
                        }
                        if (user != null && user.getId().equals(userSession.getUser().getId()) && task.getName().equals("Регистрация документов")) {
                            registrationBtn.setVisible(true);
                            registrationBtn.setEnabled(true);
                            Action action = procActionsFrame.getCompleteProcTaskActions().get(0);
                            registrationBtn.setAction(action);

                        }
                    }
                }
            }
            if(procActionsFrame.getCompleteProcTaskActions().isEmpty()){
                if(procActionsFrame.getStartProcessAction() == null){
                    procActionsBox.setVisible(false);
                }else{
                    procActionsBox.setVisible(false);
                    if(getItem().getAuthor().getId().equals(userSession.getUser().getId())) {
                        startProcButton.setEnabled(true);
                        startProcButton.setVisible(true);
                        startProcButton.setAction(procActionsFrame.getStartProcessAction());
                    }
                    
                }
            }

        }

    }

    @Override
    protected boolean preClose(String actionId) {
        if (actionId.equals("windowClose") || actionId.equals("close") && getItem() != null) {
                Long number = getItem().getSerial_number();
                if (number != null && newItem) {
                        uniqueNumbersHelperService.setNextUniqueNumber("outgoing_doc", number - 1);
                }
        }
        return super.preClose(actionId);
    }

    @Override
    protected boolean preCommit() {
        if (getItem() != null)
            getItem().setChange_date(new Date());
        for (Object o : filesTable.getDatasource().getItems().toArray()) {
            FileDescriptor fd = (FileDescriptor) o;
            if (!getItem().getFile_des().contains(fd))
                getItem().getFile_des().add(fd);
        }
        return super.preCommit();
    }


    @Override
    protected void initNewItem(OutgoingDocuments item) {
        super.initNewItem(item);
        newItem = true;
        if (item.getSerial_number() == null)
            item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("outgoing_doc"));
        item.setRegistration_number(item.getSerial_number() + "");
        item.setExecutor(outgoingDocumentsService.getCurrentWorker(userSession.getUser().getId()));
        item.setAuthor(userSession.getUser());
        item.setCreate_date(new Date());
        initListeners(item);


    }


    private void initListeners(OutgoingDocuments item) {
        Title title = new Title();
        docTypePickerField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });
        registration_numberField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });
        dateField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });

        addresseePickerField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });
        topicTextArea.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });
        logField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                RegistrationLogs logs = (RegistrationLogs) e.getValue();
                String f = logs.getNumber_format();
                String result = outgoingDocumentsService.getRegNumber(f, item.getDate(), logs.getNumber(), item.getSerial_number());
                registrationNumberTextArea.setValue(result);
            }
        });

        affairField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                item.setAffair_date(new Date());
                affair_dateField.setValue(new Date());

            }
        });

        procActionsFrame.initializer()
                .standard()
                .setBeforeStartProcessPredicate(() -> {
                    if (commit()) {
                        ProcInstance procInstance = procActionsFrame.getProcInstance();
                        ProcActor initActor = outgoingDocumentsService.createProcActor("init", procInstance, userSession.getUser());
                        ProcActor depActor = null;
                        try {
                            depActor = outgoingDocumentsService.createProcActor("dev_head", procInstance, outgoingDocumentsService.getDevHeaderUser(userSession.getUser()));
                        } catch (Exception e) {

                        }
                        ProcActor signActor = outgoingDocumentsService.createProcActor("sign", procInstance, item.getSign().getUser());
                        Set<ProcActor> procActors = new HashSet<>();
                        procActors.add(initActor);
                        if (depActor != null)
                            procActors.add(depActor);
                        if (signActor != null)
                            procActors.add(signActor);
                        procInstance.setProcActors(procActors);
                        return true;
                    }
                    return false;
                })
                .init(PROCESS_CODE, item);


        multiUpload.addQueueUploadCompleteListener(() -> {
            for (Map.Entry<UUID, String> entry : multiUpload.getUploadsMap().entrySet()) {
                UUID fileId = entry.getKey();
                String fileName = entry.getValue();
                FileDescriptor f = fileUploadingAPI.getFileDescriptor(fileId, fileName);
                if (f != null) {
                    try {
                        fileUploadingAPI.putFileIntoStorage(fileId, f);
                        filesTable.getDatasource().includeItem(f);
                    } catch (FileStorageException e) {
                        new RuntimeException("Error saving file to FileStorage", e);
                    }
                }
            }
            filesTable.getDatasource().refresh();
        });

        if (item.getFile_des() == null)
            item.setFile_des(new ArrayList<>());
        for (FileDescriptor fd : item.getFile_des()) {
            filesTable.getDatasource().includeItem(fd);
        }



    }

    private void updateTitle(OutgoingDocuments item, Title title) {
        if (docTypePickerField.getValue() != null)
            title.setDocType(((DocumentTypes) docTypePickerField.getValue()).getCode() + "");
        if (dateField.getValue() != null)
            title.setDate(dateField.getValue().toString() + "");
        if (topicTextArea.getRawValue() != null)
            title.setTopic(topicTextArea.getRawValue() + "");
        if (addresseePickerField.getValue() != null)
            title.setAddresse(((Organizations) addresseePickerField.getValue()).getTitle() + "");
        if (registrationNumberTextArea.getRawValue() != null)
            title.setRegNumber(registrationNumberTextArea.getRawValue() + "");
        item.setTitle(title.getStringTitle() + "");
        titleTextArea.setValue(title.getStringTitle() + "");
    }


    public void onDownloadButtonClick() {
        if (filesTable.getSingleSelected() != null) {
            FileDescriptor fd = filesTable.getSingleSelected();
            exportDisplay.show(fd);

        }

    }
}