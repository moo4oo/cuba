package com.company.example.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|title")
@Table(name = "EXAMPLE_AFFAIRS_NOMENCLATURE")
@Entity(name = "example$AffairsNomenclature")
public class AffairsNomenclature extends StandardEntity {
    private static final long serialVersionUID = -2822776024895394574L;

    @NotNull
    @Column(name = "CODE", nullable = false)
    protected String code;

    @Column(name = "SERIAL_NUMBER", nullable = false)
    protected Long serial_number;

    @Column(name = "TITLE")
    protected String title;

    public void setSerial_number(Long serial_number) {
        this.serial_number = serial_number;
    }

    public Long getSerial_number() {
        return serial_number;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


}