package com.company.example.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

@Table(name = "EXAMPLE_MATCHING_TABLE")
@Entity(name = "example$MatchingTable")
public class MatchingTable extends StandardEntity {
    private static final long serialVersionUID = 1871479068906174162L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MATCHER_ID")
    protected Workers matcher;

    @Temporal(TemporalType.DATE)
    @Column(name = "START_DATE")
    protected Date start_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "END_DATE")
    protected Date end_date;

    @Column(name = "RESULT_")
    protected String result;

    @Column(name = "COMMENT_")
    protected String comment;


    public void setMatcher(Workers matcher) {
        this.matcher = matcher;
    }

    public Workers getMatcher() {
        return matcher;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }


}