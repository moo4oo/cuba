package com.company.example.service;

import com.haulmont.cuba.core.app.UniqueNumbersAPI;
import org.springframework.stereotype.Service;


import javax.inject.Inject;

@Service(UniqueNumbersHelperService.NAME)
public class UniqueNumbersHelperServiceBean implements UniqueNumbersHelperService {

    @Inject
    private UniqueNumbersAPI uniqueNumbersAPI;

    @Override
    public long getNextUniqueNumber(String domain) {
        return uniqueNumbersAPI.getNextNumber(domain);
    }

    @Override
    public void setNextUniqueNumber(String domain, long number) {
        
        uniqueNumbersAPI.setCurrentNumber(domain, number);
    }
}