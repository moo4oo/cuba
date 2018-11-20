package com.company.example.service;

import com.company.example.entity.SubDivision;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(EditSubDivisionService.NAME)
public class EditSubDivisionServiceBean implements EditSubDivisionService {

    @Inject
    private DataManager dataManager;

    @Override
    public List<SubDivision> getSubDivisions() {

        return dataManager.loadList(LoadContext.create(SubDivision.class).setQuery(LoadContext.createQuery(
                "select e from example$SubDivision e"))
        .setView("subDivision-view"));
    }
}