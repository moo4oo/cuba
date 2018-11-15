package com.company.example.portal.controllers;

import com.company.example.entity.OutgoingDocuments;
import com.company.example.entity.Workers;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.portal.security.PortalSessionProvider;
import com.haulmont.cuba.restapi.Converter;
import com.haulmont.cuba.restapi.JSONConverter;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

@Controller
public class TasksController {

    @Inject
    private Metadata metadata;
    @Inject
    private DataService dataService;
    @Inject
    private DataManager dataManager;
    private String PROCESS_CODE = "contractApproval";


    @RequestMapping(value = "/gettasks", method = RequestMethod.GET)
    @ResponseBody
    public String getTasks(){
        UUID userId = PortalSessionProvider.getUserSession().getUser().getId();
        LoadContext<ProcTask> taskContext = new LoadContext<>(ProcTask.class)
                .setQuery(LoadContext.createQuery("select e from bpm$ProcTask e where e.procActor.user.id = :userId")
                        .setParameter("userId", userId))
                .setView("procTask-view");
        MetaClass metaClass = metadata.getClassNN(ProcTask.class);
        Converter converter = new JSONConverter();
        try {
            return converter.process((List)dataService.loadList(taskContext), metaClass, taskContext.getView());
        } catch (Exception e) {
            return "fail";
        }
    }
    @RequestMapping(value = "/startprocess", method = RequestMethod.GET)
    @ResponseBody
    public String startProcess(@RequestParam(value = "docid") String docId,
                               @RequestParam(value = "sign") String signId,
                               @RequestParam(value = "matching") List<String> matchId){
        User user = PortalSessionProvider.getUserSession().getUser();
        Workers worker = dataManager.load(LoadContext.create(Workers.class).setQuery(LoadContext.createQuery(
                "select e from example$Workers e where e.user.id = :userId").setParameter("userId", user.getId()))
                .setView("workers-view"));
        BpmEntitiesService.ProcInstanceDetails procInstanceDetails = new BpmEntitiesService.ProcInstanceDetails(PROCESS_CODE)
                .addProcActor("init", worker.getUser())
                .addProcActor("dev_head", worker.getSub_division().getDepartament_head().getUser())
                .addProcActor("sign", dataManager.load(LoadContext.create(User.class).setId(UUID.fromString(signId))));
        for(String id : matchId){
            procInstanceDetails.addProcActor("matching", dataManager.load(LoadContext.create(User.class)
                    .setId(UUID.fromString(id))));
        }
        procInstanceDetails.setEntity(dataManager.load(LoadContext.create(OutgoingDocuments.class).setId(UUID.fromString(docId))));



        MetaClass metaClass = metadata.getClassNN(ProcTask.class);
        Converter converter = new JSONConverter();
        try {
            return "";// converter.process((List)dataManager.loadList(taskContext), metaClass, taskContext.getView());
        } catch (Exception e) {
            return "fail";
        }
    }
}
