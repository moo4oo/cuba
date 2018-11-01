package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.ManyToOne;

@NamePattern("%s %s|first_name,second_name")
@Table(name = "EXAMPLE_WORKERS")
@Entity(name = "example$Workers")
public class Workers extends StandardEntity {
    private static final long serialVersionUID = -17131495757742330L;

    @NotNull
    @Column(name = "PERSONNEL_NUMBER", nullable = false)
    protected String personnel_number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @NotNull
    @Column(name = "SECOND_NAME", nullable = false)
    protected String second_name;

    @NotNull
    @Column(name = "FIRST_NAME", nullable = false)
    protected String first_name;

    @Column(name = "PATRONYMIC")
    protected String patronymic;

    @Column(name = "EMAIL")
    protected String email;

    @Column(name = "PHONE")
    protected String phone;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    protected FileDescriptor photo;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setPersonnel_number(String personnel_number) {
        this.personnel_number = personnel_number;
    }

    public String getPersonnel_number() {
        return personnel_number;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhoto(FileDescriptor photo) {
        this.photo = photo;
    }

    public FileDescriptor getPhoto() {
        return photo;
    }


}