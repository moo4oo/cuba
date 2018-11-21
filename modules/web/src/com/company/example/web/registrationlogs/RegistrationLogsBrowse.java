package com.company.example.web.registrationlogs;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;

import javax.inject.Inject;
import java.util.Map;

public class RegistrationLogsBrowse extends AbstractLookup {
    @Inject
    private Button editBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

    }
}