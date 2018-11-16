package com.company.example.service;

import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(EditWorkersService.NAME)
public class EditWorkersServiceBean implements EditWorkersService {

    @Override
    public Map<String, String> setFirstLastNames(User user) {
        Map<String, String> map = new HashMap<>();
        return null;
    }
}