package com.company.example.restapi;

import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.SubDivision;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessFormService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.restapi.Converter;
import com.haulmont.cuba.restapi.JSONConverter;
import com.haulmont.cuba.security.app.UserSessionService;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(value = "/restapi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OutgoingDocumentsProcessController {
    @Inject
    private DataManager dataManager;
    @Inject
    private Metadata metadata;
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private BpmEntitiesService bpmEntitiesService;

    @Inject
    private ProcessRuntimeService processRuntimeService;
    private String CODE = "contractApproval";

    @RequestMapping(value = "/getcurrenttasks", method = RequestMethod.POST)
    @ResponseBody
    public String getCurrentTasks() {
        LoadContext<ProcTask> docsLoadContext = new LoadContext<>(ProcTask.class)
                .setQuery(LoadContext.createQuery("select e from bpm$ProcTask e where e.procActor.user.id = :id")
                        .setParameter("id", userSessionSource.getUserSession().getUser().getId()))
                .setView("procTask-view");
        List<ProcTask> entities = dataManager.loadList(docsLoadContext);
        MetaClass metaClass = metadata.getClassNN(ProcTask.class);
        Converter converter = new JSONConverter();
        try {
            return converter.process((List) entities, metaClass, docsLoadContext.getView());
        } catch (Exception e) {
            return "";
        }
    }
    @RequestMapping(value = "/finishtask", method = RequestMethod.POST)
    @ResponseBody
    public String finishTask(@RequestBody FinishTaskRequest taskBody){
        try {

            ProcTask procTask = dataManager.load(LoadContext.create(ProcTask.class).setId(UUID.fromString(taskBody.getTaskId())).setView("procTask-view"));
            if(procTask.getName()!=null)
            if(procTask.getName().equals("Доработка документа")) {
                if(taskBody.getOutcome().equals("To renegotiate"))
                if (taskBody.getMatchers() != null) {
                    ProcInstance procInstance = procTask.getProcInstance();
                    ProcRole procRole = null;
                    for (ProcActor actor : procInstance.getProcActors()) {
                        if (actor.getProcRole().getCode().equals("matching")) {
                            procRole = actor.getProcRole();
                        }
                    }

                    for (String id : taskBody.getMatchers()) {
                        ProcActor actor = metadata.create(ProcActor.class);
                        actor.setProcRole(procRole);
                        actor.setUser(dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(id))));
                        actor.setProcInstance(procInstance);
                        dataManager.commit(actor);
                    }
                }
            }
            processRuntimeService.completeProcTask(procTask, taskBody.getOutcome(), taskBody.getComment(), null);


            return "task finished";
        }catch (Exception e) {
            return "fail task finish";
        }
    }

    @RequestMapping(value = "/startprocinstance", method = RequestMethod.POST, consumes =
            {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public String startProcInstance(@RequestBody StartTaskRequest taskBody) {
        User init = dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(taskBody.getInit())));
        User sign = dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(taskBody.getSign())));

        SubDivision initSubDivision = dataManager.load(LoadContext.create(SubDivision.class)
                .setQuery(LoadContext
                        .createQuery("select e.sub_division from example$Workers e where e.user.id = :id")
                        .setParameter("id", init.getId()))
        .setView("subDivision-view"));
        Workers subDivHead = null;
        if(initSubDivision != null)
        subDivHead = initSubDivision.getDepartament_head();
        User dev_head = null;
        if(subDivHead != null)
        dev_head = subDivHead.getUser();
        List<User> matching = new ArrayList<>();
        for (String matcher : taskBody.getMatching()) {
            matching.add(dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(matcher))));
        }
        BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(CODE)
                .addProcActor("init", init)
                .addProcActor("sign", sign);
        if(dev_head != null){
                procInstanceDetails.addProcActor("dev_head", dev_head);
        }
        for (User user : matching) {
            procInstanceDetails.addProcActor("matching", user);
        }

        ProcInstance procInstance = bpmEntitiesService.createProcInstance(procInstanceDetails);
        processRuntimeService.startProcess(procInstance, "start process", null);

        MetaClass metaClass = metadata.getClassNN(OutgoingDocuments.class);
        Converter converter = new JSONConverter();
        try {
            //return converter.process((List) entities, metaClass, docsLoadContext.getView());
            return "success";
        } catch (Exception e) {
            return "";
        }
    }
}
