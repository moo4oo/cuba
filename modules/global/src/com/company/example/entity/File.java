package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.FileDescriptor;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@NamePattern("%s|file")
@Table(name = "EXAMPLE_FILE")
@Entity(name = "example$File")
public class File extends StandardEntity {
    private static final long serialVersionUID = 5719406040947696442L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OUT_DOC_ID")
    protected OutgoingDocuments outDoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_ID")
    protected FileDescriptor file;

    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_TIME")
    protected Date create_time;

    @Temporal(TemporalType.DATE)
    @Column(name = "UPDATE_TIME")
    protected Date update_time;

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }


    public void setOutDoc(OutgoingDocuments outDoc) {
        this.outDoc = outDoc;
    }

    public OutgoingDocuments getOutDoc() {
        return outDoc;
    }

    public void setFile(FileDescriptor file) {
        this.file = file;
    }

    public FileDescriptor getFile() {
        return file;
    }


}