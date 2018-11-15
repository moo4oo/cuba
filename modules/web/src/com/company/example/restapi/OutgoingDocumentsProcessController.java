package com.company.example.restapi;

import com.company.example.entity.OutgoingDocuments;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.restapi.Converter;
import com.haulmont.cuba.restapi.JSONConverter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@Controller
@RequestMapping(value = "/restapi", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OutgoingDocumentsProcessController {
    @Inject
    private DataService dataService;
    @Inject
    private Metadata metadata;

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @ResponseBody
    public String outDocs() {
        LoadContext<OutgoingDocuments> docsLoadContext = new LoadContext<>(OutgoingDocuments.class)
                .setQuery(LoadContext.createQuery("select e from example$OutgoingDocuments e"))
                .setView("main-outgoingDocuments-view");
        List<OutgoingDocuments> entities = dataService.loadList(docsLoadContext);
        MetaClass metaClass = metadata.getClassNN(OutgoingDocuments.class);
        Converter converter = new JSONConverter();
        try {
            return converter.process((List) entities, metaClass, docsLoadContext.getView());
        } catch (Exception e) {
            return "";
        }
    }
}
