package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.Lob;

@NamePattern("%s|number_format")
@Table(name = "EXAMPLE_REGISTRATION_LOGS")
@Entity(name = "example$RegistrationLogs")
public class RegistrationLogs extends StandardEntity {
    private static final long serialVersionUID = 3329588260718939141L;

    @NotNull
    @Column(name = "CODE", nullable = false)
    protected String code;

    @Column(name = "TITLE")
    protected String title;

    @Lob
    @NotNull
    @Column(name = "NUMBER_FORMAT", nullable = false)
    protected String number_format;

    @Column(name = "NUMBER_")
    protected String number;

    @Column(name = "SERIAL_NUMBER")
    protected Long serial_number;

    public String getNumber_format() {
        return number_format;
    }

    public void setNumber_format(String number_format) {
        this.number_format = number_format;
    }



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

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }


}