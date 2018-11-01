package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.StandardEntity;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.chile.core.annotations.NamePattern;
import java.math.BigDecimal;
import com.haulmont.chile.core.annotations.NumberFormat;

@NamePattern("%s|name")
@Table(name = "EXAMPLE_DOCUMENT_TYPES")
@Entity(name = "example$DocumentTypes")
public class DocumentTypes extends StandardEntity {
    private static final long serialVersionUID = 2842398324940977102L;

    @NotNull
    @Column(name = "CODE", nullable = false)
    protected String code;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;



    @NumberFormat(pattern = "ВД000000")
    @Column(name = "SERIAL_NUMBER")
    protected Long serial_number;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}