alter table EXAMPLE_OUTGOING_DOCUMENTS_PROC_INSTANCE_LINK add constraint FK_OUTDOCPROINS_ON_OUTGOING_DOCUMENTS foreign key (OUTGOING_DOCUMENTS_ID) references EXAMPLE_OUTGOING_DOCUMENTS(ID);
alter table EXAMPLE_OUTGOING_DOCUMENTS_PROC_INSTANCE_LINK add constraint FK_OUTDOCPROINS_ON_PROC_INSTANCE foreign key (PROC_INSTANCE_ID) references BPM_PROC_INSTANCE(ID);
