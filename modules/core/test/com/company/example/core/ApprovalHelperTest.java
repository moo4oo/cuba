package com.company.example.core;

import com.company.example.ExampleTestContainer;
import com.company.example.entity.OutgoingDocuments;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ApprovalHelperTest {

    @ClassRule
    public static ExampleTestContainer cont = ExampleTestContainer.Common.INSTANCE;
    ApprovalHelper bean;
    OutgoingDocuments doc;
    DataManager dataManager;
    @Before
    public void init(){
        bean = AppBeans.get(ApprovalHelper.class);
        dataManager = AppBeans.get(DataManager.class);
        doc = dataManager.load(LoadContext.create(OutgoingDocuments.class).setQuery(LoadContext.createQuery(
                "select e from example$OutgoingDocuments e")));
    }
    @Test
    public void updateState() {
        if(doc != null) {
            bean.updateState(doc.getId(), "TEST");
            OutgoingDocuments d = dataManager.load(LoadContext.create(OutgoingDocuments.class).setId(doc.getId()));
            if (d != null)
                assertEquals("TEST", d.getState());
        }

    }
}