package com.company.example.service;

import com.company.example.ExampleTestContainer;
import com.haulmont.cuba.core.global.AppBeans;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class UniqueNumbersHelperServiceBeanTest {

    @ClassRule
    public static ExampleTestContainer cont = ExampleTestContainer.Common.INSTANCE;
    UniqueNumbersHelperService bean;
    @Before
    public void init(){
        bean = AppBeans.get(UniqueNumbersHelperService.class);
    }
    @Test
    public void getNextUniqueNumber() {

        assertNotEquals(bean.getNextUniqueNumber("test"), bean.getNextUniqueNumber("test"));


    }
}