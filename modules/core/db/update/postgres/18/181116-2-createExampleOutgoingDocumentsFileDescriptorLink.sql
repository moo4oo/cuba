alter table EXAMPLE_OUTGOING_DOCUMENTS_FILE_DESCRIPTOR_LINK add constraint FK_OUTDOCFILDES_ON_OUTGOING_DOCUMENTS foreign key (OUTGOING_DOCUMENTS_ID) references EXAMPLE_OUTGOING_DOCUMENTS(ID);
alter table EXAMPLE_OUTGOING_DOCUMENTS_FILE_DESCRIPTOR_LINK add constraint FK_OUTDOCFILDES_ON_FILE_DESCRIPTOR foreign key (FILE_DESCRIPTOR_ID) references SYS_FILE(ID);
