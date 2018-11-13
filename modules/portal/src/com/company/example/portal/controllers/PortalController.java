package com.company.example.portal.controllers;

import com.company.example.entity.OutgoingDocuments;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.portal.security.PortalSessionProvider;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;


@Controller
public class PortalController {

    @Inject
    protected DataService dataService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        if (PortalSessionProvider.getUserSession().isAuthenticated()) {
            LoadContext l = new LoadContext(User.class);
            LoadContext docs = LoadContext.create(OutgoingDocuments.class).setView("main-outgoingDocuments-view");
            docs.setQueryString("select e from example$OutgoingDocuments e");
            l.setQueryString("select u from sec$User u");
            model.addAttribute("users", dataService.loadList(l));
            model.addAttribute("outdocs", dataService.loadList(docs));

        }
        return "index";
    }

}
