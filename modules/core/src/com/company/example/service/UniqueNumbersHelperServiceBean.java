package com.company.example.service;

import com.company.example.service.UniqueNumbersHelperService;
import com.haulmont.cuba.core.app.UniqueNumbersService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(UniqueNumbersHelperService.NAME)
public class UniqueNumbersHelperServiceBean implements UniqueNumbersHelperService {

    @Inject
    private UniqueNumbersService uniqueNumbersService;

    @Override
    public long getNextUniqueNumber(String domain) {
        return uniqueNumbersService.getNextNumber(domain);
    }
}