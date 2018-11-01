alter table EXAMPLE_WORKERS add constraint FK_EXAMPLE_WORKERS_ON_USER foreign key (USER_ID) references SEC_USER(ID);
create index IDX_EXAMPLE_WORKERS_ON_USER on EXAMPLE_WORKERS (USER_ID);
