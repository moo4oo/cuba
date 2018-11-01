package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|short_title")
@Table(name = "EXAMPLE_ORGANIZATIONS")
@Entity(name = "example$Organizations")
public class Organizations extends StandardEntity {
    private static final long serialVersionUID = 9012060821183184663L;

    @NotNull
    @Column(name = "CODE", nullable = false)
    protected String code;

    @NotNull
    @Column(name = "SHORT_TITLE", nullable = false)
    protected String short_title;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    protected String title;

    @Column(name = "LAW_ADDRESS")
    protected String law_address;

    @Column(name = "MAIL_ADDRESS")
    protected String mail_address;

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

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getShort_title() {
        return short_title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLaw_address(String law_address) {
        this.law_address = law_address;
    }

    public String getLaw_address() {
        return law_address;
    }

    public void setMail_address(String mail_address) {
        this.mail_address = mail_address;
    }

    public String getMail_address() {
        return mail_address;
    }


}