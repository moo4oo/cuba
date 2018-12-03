package com.company.example.entity;

import javax.persistence.*;

import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.security.entity.User;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

@Listeners("example_OutgoingDocumentsListener")
@Table(name = "EXAMPLE_OUTGOING_DOCUMENTS")
@Entity(name = "example$OutgoingDocuments")
public class OutgoingDocuments extends StandardEntity {
    private static final long serialVersionUID = 3214497642282058730L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(name = "DOCUMENT_TYPE_ID")
    protected DocumentTypes document_type;

    @Column(name = "SERIAL_NUMBER")
    protected Long serial_number;

    @Column(name = "REGISTRATION_NUMBER")
    protected String registration_number;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_")
    protected Date date;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ADDRESSEE_ID")
    protected Organizations addressee;

    @Column(name = "FULL_NAME")
    protected String full_name;

    @Column(name = "TOPIC")
    protected String topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXECUTOR_ID")
    protected Workers executor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SIGN_ID")
    protected Workers sign;

    @Lob
    @Column(name = "DESCRIPTION")
    protected String description;

    @Column(name = "TITLE")
    protected String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR_ID")
    protected User author;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATE")
    protected Date create_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "CHANGE_DATE")
    protected Date change_date;

    @Column(name = "STATE")
    protected String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOG_ID")
    protected RegistrationLogs log;

    @Column(name = "DOCUMENT_DESCRIPTION")
    protected String document_description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AFFAIR_ID")
    protected AffairsNomenclature affair;

    @Temporal(TemporalType.DATE)
    @Column(name = "AFFAIR_DATE")
    protected Date affair_date;

    @JoinTable(name = "EXAMPLE_OUTGOING_DOCUMENTS_FILE_DESCRIPTOR_LINK",
        joinColumns = @JoinColumn(name = "OUTGOING_DOCUMENTS_ID"),
        inverseJoinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID"))
    @ManyToMany
    protected List<FileDescriptor> file_des;

    public DocState getState() {
        return state == null ? null : DocState.fromId(state);
    }

    public void setState(DocState state) {
        this.state = state == null ? null : state.getId();
    }


    public void setFile_des(List<FileDescriptor> file_des) {
        this.file_des = file_des;
    }

    public List<FileDescriptor> getFile_des() {
        return file_des;
    }










    public void setAffair(AffairsNomenclature affair) {
        this.affair = affair;
    }

    public AffairsNomenclature getAffair() {
        return affair;
    }




    public void setSerial_number(Long serial_number) {
        this.serial_number = serial_number;
    }

    public Long getSerial_number() {
        return serial_number;
    }










    public RegistrationLogs getLog() {
        return log;
    }

    public void setLog(RegistrationLogs log) {
        this.log = log;
    }




    public void setDocument_description(String document_description) {
        this.document_description = document_description;
    }

    public String getDocument_description() {
        return document_description;
    }

    public void setAffair_date(Date affair_date) {
        this.affair_date = affair_date;
    }

    public Date getAffair_date() {
        return affair_date;
    }


    public void setDocument_type(DocumentTypes document_type) {
        this.document_type = document_type;
    }

    public DocumentTypes getDocument_type() {
        return document_type;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setAddressee(Organizations addressee) {
        this.addressee = addressee;
    }

    public Organizations getAddressee() {
        return addressee;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setExecutor(Workers executor) {
        this.executor = executor;
    }

    public Workers getExecutor() {
        return executor;
    }

    public void setSign(Workers sign) {
        this.sign = sign;
    }

    public Workers getSign() {
        return sign;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setChange_date(Date change_date) {
        this.change_date = change_date;
    }

    public Date getChange_date() {
        return change_date;
    }


}