alter table EXAMPLE_FILE add constraint FK_EXAMPLE_FILE_ON_OUT_DOC foreign key (OUT_DOC_ID) references EXAMPLE_OUTGOING_DOCUMENTS(ID);
create index IDX_EXAMPLE_FILE_ON_OUT_DOC on EXAMPLE_FILE (OUT_DOC_ID);
