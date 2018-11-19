package com.company.example.service;

import com.company.example.ExampleTestContainer;
import com.company.example.Request.FinishTaskRequest;
import com.company.example.Request.StartTaskRequest;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.testsupport.TestContainer;
import mockit.Mock;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;





public class OutgoingDocumentsProcessServiceTest extends TestContainer {


    @ClassRule
    public static ExampleTestContainer cont = ExampleTestContainer.Common.INSTANCE;
    public FinishTaskRequest finishTaskBody;
    public StartTaskRequest startTaskBody;

   // public ProcessRuntimeService processRuntimeService;
   // public BpmEntitiesService bpmEntitiesService;
      OutgoingDocumentsProcessService bean;

    @Before
    public void initTest(){
        finishTaskBody = new FinishTaskRequest();
        finishTaskBody.setComment("test");
        finishTaskBody.setOutcome("test");
        List<String> matchers = new ArrayList<>();
        finishTaskBody.setMatchers(matchers);

        startTaskBody = new StartTaskRequest();
        startTaskBody.setDocId("d900434f-9e63-ce75-da8e-75c694ede0d1");
        startTaskBody.setInit("60885987-1b61-4247-94c7-dff348347f93");
        startTaskBody.setSign("60885987-1b61-4247-94c7-dff348347f93");
        List<String> m = new ArrayList<>();
        m.add("60885987-1b61-4247-94c7-dff348347f93");
        startTaskBody.setMatching(m);


       // bean = AppBeans.get(OutgoingDocumentsProcessService.NAME);
    }


    @Test
    public void getCurrentTasks() {

        //bean.getCurrentTasks();
    }



    @Test
    public void finishTask() {
       // bpmEntitiesService = AppBeans.get(BpmEntitiesService.class);
      //  processRuntimeService = cont.getSpringAppContext().getBean(ProcessRuntimeService.class);
        bean = AppBeans.get(OutgoingDocumentsProcessService.class);
        finishTaskBody = new FinishTaskRequest();
        finishTaskBody.setTaskId("ffa4a48a-0f52-45b5-263c-86b0cb9eb67a");
        finishTaskBody.setComment("test");
        finishTaskBody.setOutcome("test");
        List<String> matchers = new ArrayList<>();
        finishTaskBody.setMatchers(matchers);



        bean.finishTask(finishTaskBody);
    }

    @Test
    public void startProcInstance() {
        //bean.startProcInstance(startTaskBody);
    }
}