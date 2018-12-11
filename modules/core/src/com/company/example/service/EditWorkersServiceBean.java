package com.company.example.service;

import com.company.example.entity.Workers;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(EditWorkersService.NAME)
public class EditWorkersServiceBean implements EditWorkersService {

    
    @Override
    public Map<String, String> setFirstLastNames(User user) {
        Map<String, String> map = new HashMap<>();
        return null;
    }

    @Inject
    private DataManager dataManager;

    @Override
    public FileDescriptor getPhoto(UUID workerId) {

        Workers d = dataManager.load(LoadContext.create(Workers.class).setQuery(LoadContext.createQuery(
                "select e from example$Workers e where e.id = :id").setParameter("id", workerId)).setView("workers-view"));
        return d.getPhoto();
    }
}