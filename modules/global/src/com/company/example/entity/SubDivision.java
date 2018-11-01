package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@NamePattern("%s|name")
@Table(name = "EXAMPLE_SUB_DIVISION")
@Entity(name = "example$SubDivision")
public class SubDivision extends StandardEntity {
    private static final long serialVersionUID = -5047484354769319745L;

    @NotNull
    @Column(name = "CODE", nullable = false)
    protected String code;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEAD_SUBDIVISION_ID")
    protected SubDivision lead_subdivision;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTAMENT_HEAD_ID")
    protected Workers departament_head;

    public void setLead_subdivision(SubDivision lead_subdivision) {
        this.lead_subdivision = lead_subdivision;
    }

    public SubDivision getLead_subdivision() {
        return lead_subdivision;
    }

    public void setDepartament_head(Workers departament_head) {
        this.departament_head = departament_head;
    }

    public Workers getDepartament_head() {
        return departament_head;
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