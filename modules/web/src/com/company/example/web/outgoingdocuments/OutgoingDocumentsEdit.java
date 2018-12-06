package com.company.example.web.outgoingdocuments;

import com.company.example.Title;
import com.company.example.entity.*;
import com.company.example.service.OutgoingDocumentsService;
import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.gui.action.CompleteProcTaskAction;
import com.haulmont.bpm.gui.action.StartProcessAction;
import com.haulmont.bpm.gui.procactions.ProcActionsFrame;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.FileStorageException;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.upload.FileUploadingAPI;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.reports.gui.actions.EditorPrintFormAction;

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
    private boolean newItem = false;
    @Inject
    private ExportDisplay exportDisplay;
    @Inject
    private FileMultiUploadField multiUpload;
    @Inject
    private FileUploadingAPI fileUploadingAPI;
    @Inject
    private LookupPickerField docTypePickerField;
    @Inject
    private LookupPickerField addresseePickerField;
    @Inject
    private GroupBoxLayout procActionsBox;
    @Inject
    private Button startProcButton;
    @Inject
    private TextField registrationNumberTextArea;
    @Inject
    private Datasource<OutgoingDocuments> outgoingDocumentsesDs;
    @Inject
    private CollectionDatasource<ProcTask, UUID> procTasksDs;
    @Inject
    private Table<ProcTask> procTasksTable;
    private UUID currentUserId;
    private User currentUser;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        currentUser = userSession.getCurrentOrSubstitutedUser();
        currentUserId = currentUser.getId();
        printCardInfoBtn.setAction(new EditorPrintFormAction("docCardReport", this, null));
        if (params.containsKey("ITEM") && !PersistenceHelper.isNew(params.get("ITEM"))) {
            setItem((OutgoingDocuments) params.get("ITEM"));
            initListeners(getItem());
            List<ProcTask> tasks = outgoingDocumentsService.getActiveDocTasks(getItem().getId());
            for (ProcTask task : tasks) {
                if (task.getEndDate() == null) {
                    User user = outgoingDocumentsService.getCurrentTaskUser(task.getId(), getItem().getId());
                    if (user == null) {
                        procActionsFrame.setVisible(false);
                    } else if(user.getId().equals(currentUserId) && task.getName().equals("Регистрация документов")) {
                            registrationBtn.setVisible(true);
                            Action action = procActionsFrame.getCompleteProcTaskActions().get(0);
                            registrationBtn.setAction(action);
                        }

                }
            }
        }
        if (procActionsFrame.getCompleteProcTaskActions().isEmpty()) {
            if (procActionsFrame.getStartProcessAction() == null) {
                procActionsBox.setVisible(false);
            } else {
                procActionsBox.setVisible(false);
                if (getItem().getAuthor() != null && getItem().getAuthor().getId().equals(currentUserId)) {
                    StartProcessAction action = procActionsFrame.getStartProcessAction();
                    action.addAfterActionListener(() -> {
                        startProcButton.setVisible(false);
                        procActionsFrame.setVisible(true);
                        procActionsBox.setVisible(true);
                    });
                    startProcButton.setAction(action);
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
        item.setSerial_number(uniqueNumbersHelperService.getNextUniqueNumber("outgoing_doc"));
        item.setRegistration_number(item.getSerial_number() + "");
        item.setExecutor(outgoingDocumentsService.getCurrentWorker(currentUserId));
        item.setAuthor(currentUser);
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
                .setAfterCompleteTaskListener(() -> outgoingDocumentsesDs.refresh())
                .setBeforeStartProcessPredicate(() -> {
                    if (commit()) {
                        ProcInstance procInstance = procActionsFrame.getProcInstance();
                        ProcActor initActor = outgoingDocumentsService.createProcActor("init", procInstance, currentUser);
                        ProcActor depActor = null;
                        try {
                            depActor = outgoingDocumentsService.createProcActor("dev_head", procInstance, outgoingDocumentsService.getDevHeaderUser(currentUser));
                        } catch (Exception e) {
                            showNotification(e.getMessage());
                        }
                        ProcActor signActor = null;
                        if (item.getSign() != null) {
                            signActor = outgoingDocumentsService.createProcActor("sign", procInstance, item.getSign().getUser());
                        }
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