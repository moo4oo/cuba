alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_AFFAIR foreign key (AFFAIR_ID) references EXAMPLE_AFFAIRS_NOMENCLATURE(ID);
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_AFFAIR on EXAMPLE_OUTGOING_DOCUMENTS (AFFAIR_ID);
