alter table EXAMPLE_OUTGOING_DOCUMENTS rename column file_id to file_id__u22339 ;
drop index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_FILE ;
alter table EXAMPLE_OUTGOING_DOCUMENTS drop constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_FILE ;
