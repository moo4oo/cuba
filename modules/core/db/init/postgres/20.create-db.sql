-- begin EXAMPLE_WORKERS
alter table EXAMPLE_WORKERS add constraint FK_EXAMPLE_WORKERS_ON_USER foreign key (USER_ID) references SEC_USER(ID)^
alter table EXAMPLE_WORKERS add constraint FK_EXAMPLE_WORKERS_ON_PHOTO foreign key (PHOTO_ID) references SYS_FILE(ID)^
create index IDX_EXAMPLE_WORKERS_ON_USER on EXAMPLE_WORKERS (USER_ID)^
create index IDX_EXAMPLE_WORKERS_ON_PHOTO on EXAMPLE_WORKERS (PHOTO_ID)^
-- end EXAMPLE_WORKERS
-- begin EXAMPLE_SUB_DIVISION
alter table EXAMPLE_SUB_DIVISION add constraint FK_EXAMPLE_SUB_DIVISION_ON_LEAD_SUBDIVISION foreign key (LEAD_SUBDIVISION_ID) references EXAMPLE_SUB_DIVISION(ID)^
alter table EXAMPLE_SUB_DIVISION add constraint FK_EXAMPLE_SUB_DIVISION_ON_DEPARTAMENT_HEAD foreign key (DEPARTAMENT_HEAD_ID) references EXAMPLE_WORKERS(ID)^
create index IDX_EXAMPLE_SUB_DIVISION_ON_LEAD_SUBDIVISION on EXAMPLE_SUB_DIVISION (LEAD_SUBDIVISION_ID)^
create index IDX_EXAMPLE_SUB_DIVISION_ON_DEPARTAMENT_HEAD on EXAMPLE_SUB_DIVISION (DEPARTAMENT_HEAD_ID)^
-- end EXAMPLE_SUB_DIVISION
-- begin EXAMPLE_OUTGOING_DOCUMENTS
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_DOCUMENT_TYPE foreign key (DOCUMENT_TYPE_ID) references EXAMPLE_DOCUMENT_TYPES(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_ADDRESSEE foreign key (ADDRESSEE_ID) references EXAMPLE_ORGANIZATIONS(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_EXECUTOR foreign key (EXECUTOR_ID) references EXAMPLE_WORKERS(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_SIGN foreign key (SIGN_ID) references EXAMPLE_WORKERS(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_AUTHOR foreign key (AUTHOR_ID) references SEC_USER(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_FILE foreign key (FILE_ID) references SYS_FILE(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_LOG foreign key (LOG_ID) references EXAMPLE_REGISTRATION_LOGS(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_AFFAIR foreign key (AFFAIR_ID) references EXAMPLE_AFFAIRS_NOMENCLATURE(ID)^
alter table EXAMPLE_OUTGOING_DOCUMENTS add constraint FK_EXAMPLE_OUTGOING_DOCUMENTS_ON_MATCHING foreign key (MATCHING_ID) references EXAMPLE_WORKERS(ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_DOCUMENT_TYPE on EXAMPLE_OUTGOING_DOCUMENTS (DOCUMENT_TYPE_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_ADDRESSEE on EXAMPLE_OUTGOING_DOCUMENTS (ADDRESSEE_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_EXECUTOR on EXAMPLE_OUTGOING_DOCUMENTS (EXECUTOR_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_SIGN on EXAMPLE_OUTGOING_DOCUMENTS (SIGN_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_AUTHOR on EXAMPLE_OUTGOING_DOCUMENTS (AUTHOR_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_FILE on EXAMPLE_OUTGOING_DOCUMENTS (FILE_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_LOG on EXAMPLE_OUTGOING_DOCUMENTS (LOG_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_AFFAIR on EXAMPLE_OUTGOING_DOCUMENTS (AFFAIR_ID)^
create index IDX_EXAMPLE_OUTGOING_DOCUMENTS_ON_MATCHING on EXAMPLE_OUTGOING_DOCUMENTS (MATCHING_ID)^
-- end EXAMPLE_OUTGOING_DOCUMENTS
