package com.company.example.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum MacroEnum implements EnumClass<String> {
    dd_MM("dd.MM"),
    MM_yyyy("MM.yyyy"),
    dd_MM_yy("dd.MM.yy"),
    dd_MM_yyyy("dd.MM.yyyy");

    private String id;

    MacroEnum(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static MacroEnum fromId(String id) {
        for (MacroEnum at : MacroEnum.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}