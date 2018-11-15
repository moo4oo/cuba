package com.company.example.web.outgoingdocuments;

import com.company.example.Title;
import com.company.example.entity.*;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.gui.procactions.ProcActionsFrame;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.reports.gui.actions.EditorPrintFormAction;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.*;

public class OutgoingDocumentsEdit extends AbstractEditor<OutgoingDocuments> {
    private static final String PROCESS_CODE = "contractApproval";
    @Inject
    private ProcActionsFrame procActionsFrame;
    @Inject
    private UniqueNumbersService uniqueNumbersService;
    @Named("mainFieldGroup.registration_number")
    private TextField registration_numberFieldMain;
    @Named("mainFieldGroup.date")
    private DateField dateFieldMain;
    @Named("registrationsFieldGroup.date")
    private DateField dateField;
    @Named("registrationsFieldGroup.registration_number")
    private TextField registration_numberField;
    @Named("mainFieldGroup.title")
    private TextField titleField;
    @Named("mainFieldGroup.state")
    private TextField stateField;
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
    @Inject
    private BpmEntitiesService bpmEntitiesService;
    @Inject
    private Button printCardInfoBtn;
    @Inject
    private Button registrationBtn;
    @Inject
    private Table<File> filesTable;



    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        printCardInfoBtn.setAction(new EditorPrintFormAction("docCardReport", this, null));
        titleField.setEditable(false);
        registration_numberField.setEditable(false);
        dateField.setEditable(false);
        authorField.setEditable(false);
        create_dateField.setEditable(false);
        change_dateField.setEditable(false);
        stateField.setEditable(false);
        dateFieldMain.setEditable(false);
        registration_numberFieldMain.setEditable(false);
        registration_numberField.setEditable(false);
        Entity docs = WindowParams.ITEM.getEntity(params);
        setItem(docs);
        if (getItem() != null) {
            initListeners(getItem());

            List<ProcTask> tasks = getDocTasks(getItem().getId());
            for(ProcTask task : tasks) {
                if (task.getEndDate() == null) {
                    User user = getCurrentTaskUser(task.getId(), getItem().getId());
                        if (task.getName().equals("Регистрация документов"))
                            if (user != null)
                                if (user.getId().equals(userSession.getUser().getId())) {
                                    registrationBtn.setVisible(true);
                                    registrationBtn.setEnabled(true);
                                    Action action = procActionsFrame.getCompleteProcTaskActions().get(0);
                                    registrationBtn.setAction(action);

                                }
                }
            }
        }

    }

    private User getCurrentTaskUser(UUID procTaskUUID, UUID docUUID) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("procTaskUUID", procTaskUUID);
        parameters.put("docUUID", docUUID);
        return dataManager.load(User.class).query("select e.procActor.user from bpm$ProcTask e " +
                "where e.procInstance.entity.entityId " +
                "= :docUUID and e.id = :procTaskUUID")
                .setParameters(parameters)
                .one();
    }

    private List<ProcTask> getDocTasks(UUID docUUID) {
        return dataManager.load(ProcTask.class).query("select e from bpm$ProcTask e " +
                "where e.procInstance.entity.entityId " +
                "= :docUUID")
                .parameter("docUUID", docUUID)
                .list();
    }

    @Override
    protected boolean preCommit() {
        if (getItem() != null)
            getItem().setChange_date(new Date());
        return super.preCommit();
    }

    @Override
    protected void initNewItem(OutgoingDocuments item) {
        super.initNewItem(item);
        item.setSerial_number(uniqueNumbersService.getNextNumber("serial_number_outgoing"));
        item.setRegistration_number(item.getSerial_number() + "");
        //item.setDate(new Date());
        item.setExecutor(getCurrentWorker(userSession.getUser().getId()));
        item.setAuthor(userSession.getUser());
        item.setCreate_date(new Date());
        initListeners(item);

    }

    private Workers getCurrentWorker(UUID userUUID) {
        return dataManager.load(Workers.class).query("select e from example$Workers e where " +
                "e.user.id = :userUUID")
                .parameter("userUUID", userUUID)
                .one();
    }

    private User getDevHeaderUser(User user) {
        return dataManager.load(User.class).query("select e.sub_division.departament_head.user from example$Workers e where " +
                "e.user.id = :userUUID")
                .parameter("userUUID", user.getId())
                .one();
    }


    @Inject
    private Metadata metadata;

    private void initListeners(OutgoingDocuments item) {
        Title title = new Title();
        document_typeField.addValueChangeListener(e -> {
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

        addresseeField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });
        topicField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                updateTitle(item, title);
            }
        });
        logField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                RegistrationLogs logs = (RegistrationLogs) e.getValue();
                SimpleDateFormat fd = new SimpleDateFormat(logs.getNumber_format().getId());
                String number = " ";
                if (logs.getNumber() != null)
                    number = String.format("%0" + logs.getNumber() + "d", item.getSerial_number());
                item.setRegistration_number("Исх - " + fd.format(item.getDate()) + " " + number);
                registration_numberField.setValue("Исх - " + fd.format(item.getDate()) + " " + number);

            }
        });

        affairField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                if (!e.getPrevValue().equals(e.getValue())) {
                    item.setAffair_date(new Date());
                    affair_dateField.setValue(new Date());
                }
            }
        });

        procActionsFrame.initializer()
                .standard()
                .setBeforeStartProcessPredicate(() -> {
                    if (commit()) {
                        ProcInstance procInstance = procActionsFrame.getProcInstance();
                        ProcActor initActor = createProcActor("init", procInstance, userSession.getUser());
                        ProcActor depActor = null;
                        try {
                            depActor = createProcActor("dev_head", procInstance, getDevHeaderUser(userSession.getUser()));
                        } catch (Exception e) {

                        }
                        ProcActor signActor = createProcActor("sign", procInstance, item.getSign().getUser());
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
        

    }

    private ProcActor createProcActor(String procRoleCode, ProcInstance procInstance, User user) {
        ProcActor initiatorProcActor = metadata.create(ProcActor.class);
        initiatorProcActor.setUser(user);
        ProcRole initiatorProcRole = bpmEntitiesService.findProcRole(PROCESS_CODE, procRoleCode, View.MINIMAL);
        initiatorProcActor.setProcRole(initiatorProcRole);
        initiatorProcActor.setProcInstance(procInstance);
        return initiatorProcActor;
    }

    private void updateTitle(OutgoingDocuments item, Title title) {
        if (document_typeField.getValue() != null)
            title.setDocType(((DocumentTypes) document_typeField.getValue()).getCode() + "");
        if (dateField.getValue() != null)
            title.setDate(dateField.getValue().toString() + "");
        if (topicField.getRawValue() != null)
            title.setTopic(topicField.getRawValue() + "");
        if (addresseeField.getValue() != null)
            title.setAddresse(((Organizations) addresseeField.getValue()).getTitle() + "");
        if (registration_numberFieldMain.getRawValue() != null)
            title.setRegNumber(registration_numberFieldMain.getRawValue() + "");
        item.setTitle(title.getStringTitle() + "");
        titleField.setValue(title.getStringTitle() + "");
    }

    public void onCreateButtonClick() {
        Map<String,Object> params = new HashMap<>();
        params.put("doc", getItem());
        //openWindow("example$File.edit", WindowManager.OpenType.DIALOG, params);
        Editor editor = openEditor("example$File.edit", new File(), WindowManager.OpenType.DIALOG, params);
        editor.addCloseWithCommitListener(() -> {
            filesTable.getDatasource().refresh();
            //getItem().setFiles
        });
    }
}