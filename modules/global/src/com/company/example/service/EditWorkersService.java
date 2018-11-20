package com.company.example.service;


import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.security.entity.User;

import java.util.Map;
import java.util.UUID;

public interface EditWorkersService {
    String NAME = "example_EditWorkersService";

    public Map<String, String> setFirstLastNames(User user);
    public FileDescriptor getPhoto(UUID workerId);
}