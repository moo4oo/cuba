alter table EXAMPLE_MATCHING_TABLE add constraint FK_EXAMPLE_MATCHING_TABLE_ON_MATCHER foreign key (MATCHER_ID) references EXAMPLE_WORKERS(ID);
alter table EXAMPLE_MATCHING_TABLE add constraint FK_EXAMPLE_MATCHING_TABLE_ON_OUTGOING_DOCUMENTS foreign key (OUTGOING_DOCUMENTS_ID) references EXAMPLE_OUTGOING_DOCUMENTS(ID);
create index IDX_EXAMPLE_MATCHING_TABLE_ON_MATCHER on EXAMPLE_MATCHING_TABLE (MATCHER_ID);
create index IDX_EXAMPLE_MATCHING_TABLE_ON_OUTGOING_DOCUMENTS on EXAMPLE_MATCHING_TABLE (OUTGOING_DOCUMENTS_ID);
