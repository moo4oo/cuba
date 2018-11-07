alter table EXAMPLE_FILE add constraint FK_EXAMPLE_FILE_ON_FILE foreign key (FILE_ID) references SYS_FILE(ID);
create index IDX_EXAMPLE_FILE_ON_FILE on EXAMPLE_FILE (FILE_ID);
