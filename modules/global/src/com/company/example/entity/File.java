package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.FileDescriptor;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

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