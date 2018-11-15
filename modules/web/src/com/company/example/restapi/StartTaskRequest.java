
package com.company.example.restapi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartTaskRequest {

    private String docId;
    private String init;
    private String sign;
    private List<String> matching = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<String> getMatching() {
        return matching;
    }

    public void setMatching(List<String> matching) {
        this.matching = matching;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
