package com.company.example.listener;


import com.haulmont.cuba.security.entity.User;

import java.util.Map;

public interface EditWorkersService {
    String NAME = "example_EditWorkersService";

    public Map<String, String> setFirstLastNames(User user);
}