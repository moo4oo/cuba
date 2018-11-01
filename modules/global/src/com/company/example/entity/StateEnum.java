package com.company.example.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum StateEnum implements EnumClass<Integer> {

    New(10),
    in_coordination(20),
    at_the_signing(30),
    on_completion(40),
    Registred(50);

    private Integer id;

    StateEnum(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static StateEnum fromId(Integer id) {
        for (StateEnum at : StateEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}