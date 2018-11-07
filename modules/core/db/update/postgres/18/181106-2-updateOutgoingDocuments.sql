alter table EXAMPLE_OUTGOING_DOCUMENTS rename column doc_affair_id to doc_affair_id__u89283 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS add column AFFAIR_ID uuid ;
