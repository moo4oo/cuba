package com.company.example.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DocState implements EnumClass<String> {

    New("New"),
    OnAgreed("On agreed"),
    ForRevision("For revision"),
    OnSign("On sign"),
    OnRegistration("On registration"),
    Registered("Registered");

    private String id;

    DocState(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static DocState fromId(String id) {
        for (DocState at : DocState.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}