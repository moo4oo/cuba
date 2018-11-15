package com.company.example.portal.controllers;

import com.company.example.Title;
import com.company.example.entity.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.portal.security.PortalSessionProvider;
import com.haulmont.cuba.restapi.Converter;
import com.haulmont.cuba.restapi.JSONConverter;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class OutgoingDocumentsController {


    @Inject
    private Metadata metadata;
    @Inject
    private DataService dataService;
    @Inject
    private DataManager dataManager;
    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @RequestMapping(value = "/alloutdoc", method = RequestMethod.GET)
    @ResponseBody
    public String outDocs() {
        LoadContext<OutgoingDocuments> docsLoadContext = new LoadContext<>(OutgoingDocuments.class)
                .setQuery(LoadContext.createQuery("select e from example$OutgoingDocuments e"))
                .setView("main-outgoingDocuments-view");
        List<OutgoingDocuments> entities = dataService.loadList(docsLoadContext);
        MetaClass metaClass = metadata.getClassNN(OutgoingDocuments.class);
        Converter converter = new JSONConverter();//conversionFactory.getConverter("json");
        try {
            return converter.process((List) entities, metaClass, docsLoadContext.getView());
        } catch (Exception e) {
            return "";
        }
    }

    @RequestMapping(value = "/finddoc", method = RequestMethod.GET)
    @ResponseBody
    public String outDoc(@RequestParam(value = "docid") String id) {
        UUID docUUID = UUID.fromString(id);
        LoadContext<OutgoingDocuments> docLoadContext = new LoadContext<>(OutgoingDocuments.class)
                .setQuery(LoadContext.createQuery("select e from example$OutgoingDocuments e where e.id = :docUUID")
                        .setParameter("docUUID", docUUID))
                .setView("main-outgoingDocuments-view");
        OutgoingDocuments entity = dataService.load(docLoadContext);
        MetaClass metaClass = metadata.getClassNN(OutgoingDocuments.class);
        Converter converter = new JSONConverter();
        try {
            return converter.process(entity, metaClass, docLoadContext.getView());
        } catch (Exception e) {
            return "";
        }
    }

    @RequestMapping(value = "/deletedoc", method = RequestMethod.GET)
    @ResponseBody
    public String deleteDoc(@RequestParam(value = "docid") String id) {
        UUID docUUID = UUID.fromString(id);
        LoadContext<OutgoingDocuments> docLoadContext = new LoadContext<>(OutgoingDocuments.class)
                .setQuery(LoadContext.createQuery("select e from example$OutgoingDocuments e where e.id = :docUUID")
                        .setParameter("docUUID", docUUID));
        OutgoingDocuments entity = dataService.load(docLoadContext);
        try {
            dataManager.remove(entity);
            return "success";
        } catch (Exception e) {
            return "";
        }
    }

    @RequestMapping(value = "/editdoc", method = RequestMethod.GET)
    public String edit(@RequestParam(value = "docid") String id, Model model) {
            LoadContext doc = LoadContext.create(OutgoingDocuments.class).setView("main-outgoingDocuments-view");
            doc.setQueryString("select e from example$OutgoingDocuments e where e.id = :id").setParameter("id", UUID.fromString(id));
            model.addAttribute("doc", dataService.load(doc));


        return "edit";

    }

    @RequestMapping(value = "/createdoc", method = RequestMethod.GET)
    public String edit(Model model) {
        return "create";

    }


    @PostMapping("/createoutdoc")
    @ResponseBody
    public String createoutdoc(@RequestParam(value = "document_type") String docTypeId,
                               @RequestParam(value = "addressee") String addresseeId,
                               @RequestParam(value = "full_name") String full_name,
                               @RequestParam(value = "topic") String topic,
                               @RequestParam(value = "sign") String signId,
                               @RequestParam(value = "file") String fileId,
                               @RequestParam(value = "description") String description,
                               @RequestParam(value = "log") String logId,
                               @RequestParam(value = "document_description") String document_description,
                               @RequestParam(value = "affair") String affairId) {
        Title t = new Title();
        OutgoingDocuments doc = submitCreateDoc(t, dataManager.create(OutgoingDocuments.class), docTypeId,
                addresseeId, full_name, topic, signId, fileId, description, logId, document_description, affairId);
        MetaClass metaClass = metadata.getClassNN(OutgoingDocuments.class);
        Converter converter = new JSONConverter();

        try {
            doc.setSerial_number(uniqueNumbersService.getNextNumber("serial_number_outgoing"));
            doc.setRegistration_number(doc.getSerial_number() + "");
            doc.setTitle(t.getStringTitle());
            dataManager.commit(doc);
            Metadata metadata = AppBeans.get(Metadata.NAME);
            View view = metadata.getViewRepository().getView(metaClass, "main-outgoingDocuments-view");
            return converter.process(doc, metaClass, view);
        } catch (Exception e) {
            return "fail";
        }
    }

    private OutgoingDocuments submitCreateDoc(Title t, OutgoingDocuments doc, String docTypeId, String addresseeId, String full_name, String topic,
                                              String signId, String fileId, String description,
                                              String logId, String document_description, String affairId) {
        doc.setAuthor(PortalSessionProvider.getUserSession().getUser());
        if (docTypeId != null && !docTypeId.equals("")) {
            doc.setDocument_type(dataManager.load(LoadContext.create(DocumentTypes.class).setId(UUID.fromString(docTypeId))));
            t.setDocType(doc.getDocument_type().getCode());
        }

        if (addresseeId != null && !addresseeId.equals("")) {
            doc.setAddressee(dataManager.load(LoadContext.create(Organizations.class).setId(UUID.fromString(addresseeId))));
            t.setAddresse(doc.getAddressee().getTitle());
        }

        if (full_name != null && !full_name.equals(""))
            doc.setFull_name(full_name);

        if (topic != null && !topic.equals("")) {
            doc.setTopic(topic);
            t.setTopic(topic);
        }

        User user = PortalSessionProvider.getUserSession().getUser();
        doc.setExecutor(dataManager.load(Workers.class).query("select e from example$Workers e where e.user.id = :userId")
                .parameter("userId", user.getId()).one());

        if (signId != null && !signId.equals(""))
            doc.setSign(dataManager.load(LoadContext.create(Workers.class).setId(UUID.fromString(signId))));

        doc.setCreate_date(new Date());

        //if(fileId != null && !fileId.equals(""))
        //doc.setFile(dataManager.load(LoadContext.create(FileDescriptor.class).setId(fileId)));

        if (description != null && !description.equals(""))
            doc.setDescription(description);

        if (logId != null && !logId.equals(""))
            doc.setLog(dataManager.load(LoadContext.create(RegistrationLogs.class).setId(UUID.fromString(logId))));

        if (document_description != null && !document_description.equals(""))
            doc.setDocument_description(document_description);

        if (affairId != null && !affairId.equals("")) {
            doc.setAffair(dataManager.load(LoadContext.create(AffairsNomenclature.class).setId(UUID.fromString(affairId))));
            doc.setAffair_date(new Date());
        }

        return doc;
    }

    private OutgoingDocuments submitEditDoc(Title t, OutgoingDocuments doc, String docTypeId, String addresseeId, String full_name, String topic, String executorId,
                                            String signId, String fileId, String description,
                                            String logId, String document_description, String affairId) {
        if (docTypeId != null && !docTypeId.equals("") && !doc.getDocument_type().getId().toString().equals(docTypeId)) {
            doc.setDocument_type(dataManager.load(LoadContext.create(DocumentTypes.class).setId(UUID.fromString(docTypeId))));
            t.setDocType(doc.getDocument_type().getCode());
        }

        if (addresseeId != null && !addresseeId.equals("") && !doc.getAddressee().getId().toString().equals(addresseeId)) {
            doc.setAddressee(dataManager.load(LoadContext.create(Organizations.class).setId(UUID.fromString(addresseeId))));
            t.setAddresse(doc.getAddressee().getTitle());
        }

        if (full_name != null && !full_name.equals("") && !doc.getFull_name().equals(full_name))
            doc.setFull_name(full_name);

        if (topic != null && !topic.equals("") && !doc.getTopic().equals(topic)) {
            doc.setTopic(topic);
            t.setTopic(topic);
        }

        if (executorId != null && !executorId.equals("") && !doc.getExecutor().getId().toString().equals(executorId))
            doc.setExecutor(dataManager.load(LoadContext.create(Workers.class).setId(UUID.fromString(executorId))));

        if (signId != null && !signId.equals("") && !doc.getSign().getId().toString().equals(signId))
            doc.setSign(dataManager.load(LoadContext.create(Workers.class).setId(UUID.fromString(signId))));

        //if(fileId != null && !fileId.equals("") )
        //doc.setFile(dataManager.load(LoadContext.create(FileDescriptor.class).setId(fileId)));

        if (description != null && !description.equals("") && !doc.getDescription().equals(description))
            doc.setDescription(description);

        if (logId != null && !logId.equals("") && !doc.getLog().getId().toString().equals(logId))
            doc.setLog(dataManager.load(LoadContext.create(RegistrationLogs.class).setId(UUID.fromString(logId))));

        if (document_description != null && !document_description.equals("") && !doc.getDocument_description().equals(document_description))
            doc.setDocument_description(document_description);

        if (affairId != null && !affairId.equals("") && !doc.getAffair().getId().toString().equals(affairId))
            doc.setAffair(dataManager.load(LoadContext.create(AffairsNomenclature.class).setId(UUID.fromString(affairId))));
        return doc;
    }

    @PostMapping("/editoutdoc")
    @ResponseBody
    public String editoutDoc(@RequestParam(value = "id") String docId,
                             @RequestParam(value = "document_type") String docTypeId,
                             @RequestParam(value = "addressee") String addresseeId,
                             @RequestParam(value = "full_name") String full_name,
                             @RequestParam(value = "topic") String topic,
                             @RequestParam(value = "executor") String executorId,
                             @RequestParam(value = "sign") String signId,
                             @RequestParam(value = "file") String fileId,
                             @RequestParam(value = "description") String description,
                             @RequestParam(value = "log") String logId,
                             @RequestParam(value = "document_description") String document_description,
                             @RequestParam(value = "affair") String affairId) {
        Title t = new Title();
        LoadContext<OutgoingDocuments> docLoadContext = new LoadContext<>(OutgoingDocuments.class)
                .setQuery(LoadContext.createQuery("select e from example$OutgoingDocuments e where e.id = :docId")
                        .setParameter("docId", UUID.fromString(docId)))
                .setView("main-outgoingDocuments-view");
        OutgoingDocuments doc = submitEditDoc(t, dataManager.load(docLoadContext),
                docTypeId, addresseeId, full_name, topic, executorId, signId, fileId, description, logId, document_description, affairId);


        MetaClass metaClass = metadata.getClassNN(OutgoingDocuments.class);
        Converter converter = new JSONConverter();
        try {
            doc.setTitle(t.getStringTitle());
            doc.setChange_date(new Date());
            dataManager.commit(doc);
            return converter.process(doc, metaClass, docLoadContext.getView());
        } catch (Exception e) {
            return "fail";
        }

    }

    @RequestMapping(value = "/addfile", method = RequestMethod.GET)
    public String add(Model model){

        return "addfile";
    }
    @PostMapping("/addfiletodoc")
    @ResponseBody
    public String addFileToDoc(@RequestParam(value = "doc") String docId,
                               @RequestParam(value = "file") String fileId){

        LoadContext<File> fileLoadContext = new LoadContext<>(File.class)
                .setQuery(LoadContext.createQuery("select e from example$File e where e.id = :fileId")
                        .setParameter("fileId", UUID.fromString(fileId)))
                .setView("file-view");
        LoadContext<OutgoingDocuments> docLoadContext = new LoadContext<>(OutgoingDocuments.class)
                .setQuery(LoadContext.createQuery("select e from example$OutgoingDocuments e where e.id = :docId")
                        .setParameter("docId", UUID.fromString(docId)))
                .setView("main-outgoingDocuments-view");
        File file = dataManager.load(fileLoadContext);
        file.setOutDoc(dataManager.load(docLoadContext));
        MetaClass metaClass = metadata.getClassNN(File.class);
        Converter converter = new JSONConverter();
        try {

            dataManager.commit(file);
            return converter.process(file, metaClass, fileLoadContext.getView());
        } catch (Exception e) {
            return "fail";
        }
    }




}
