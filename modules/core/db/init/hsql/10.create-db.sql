-- begin EXAMPLE_WORKERS
create table EXAMPLE_WORKERS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PERSONNEL_NUMBER varchar(255) not null,
    USER_ID varchar(36),
    SECOND_NAME varchar(255) not null,
    FIRST_NAME varchar(255) not null,
    PATRONYMIC varchar(255),
    EMAIL varchar(255),
    PHONE varchar(255),
    PHOTO_ID varchar(36),
    --
    primary key (ID)
)^
-- end EXAMPLE_WORKERS
-- begin EXAMPLE_SUB_DIVISION
create table EXAMPLE_SUB_DIVISION (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    NAME varchar(255) not null,
    LEAD_SUBDIVISION_ID varchar(36),
    DEPARTAMENT_HEAD_ID varchar(36),
    --
    primary key (ID)
)^
-- end EXAMPLE_SUB_DIVISION
-- begin EXAMPLE_DOCUMENT_TYPES
create table EXAMPLE_DOCUMENT_TYPES (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    NAME varchar(255) not null,
    --
    primary key (ID)
)^
-- end EXAMPLE_DOCUMENT_TYPES
-- begin EXAMPLE_ORGANIZATIONS
create table EXAMPLE_ORGANIZATIONS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    SHORT_TITLE varchar(255) not null,
    TITLE varchar(255) not null,
    LAW_ADDRESS varchar(255),
    MAIL_ADDRESS varchar(255),
    --
    primary key (ID)
)^
-- end EXAMPLE_ORGANIZATIONS
-- begin EXAMPLE_REGISTRATION_LOGS
create table EXAMPLE_REGISTRATION_LOGS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    TITLE varchar(255),
    NUMBER_FORMAT varchar(255) not null,
    NUMBER_ varchar(255),
    --
    primary key (ID)
)^
-- end EXAMPLE_REGISTRATION_LOGS
-- begin EXAMPLE_AFFAIRS_NOMENCLATURE
create table EXAMPLE_AFFAIRS_NOMENCLATURE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    TITLE varchar(255),
    --
    primary key (ID)
)^
-- end EXAMPLE_AFFAIRS_NOMENCLATURE
-- begin EXAMPLE_OUTGOING_DOCUMENTS
create table EXAMPLE_OUTGOING_DOCUMENTS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DOCUMENT_TYPE_ID varchar(36) not null,
    REGISTRATION_NUMBER varchar(255),
    DATE_ date,
    ADDRESSEE_ID varchar(36) not null,
    FULL_NAME varchar(255),
    TOPIC varchar(255),
    EXECUTOR_ID varchar(36),
    SIGN_ID varchar(36),
    DESCRIPTION longvarchar,
    TITLE varchar(255) not null,
    AUTHOR_ID varchar(36),
    CREATE_DATE date not null,
    CHANGE_DATE date,
    STATE integer,
    FILE_ID varchar(36),
    DOCUMENT_DESCRIPTION varchar(255),
    AFFAIR_DATE date,
    START_DATE date,
    END_DATE date,
    RESULT_ varchar(255),
    COMMENTS longvarchar,
    --
    primary key (ID)
)^
-- end EXAMPLE_OUTGOING_DOCUMENTS
-- begin EXAMPLE_OUTGOING_DOCUMENTS_AFFAIRS_NOMENCLATURE_LINK
create table EXAMPLE_OUTGOING_DOCUMENTS_AFFAIRS_NOMENCLATURE_LINK (
    OUTGOING_DOCUMENTS_ID varchar(36) not null,
    AFFAIRS_NOMENCLATURE_ID varchar(36) not null,
    primary key (OUTGOING_DOCUMENTS_ID, AFFAIRS_NOMENCLATURE_ID)
)^
-- end EXAMPLE_OUTGOING_DOCUMENTS_AFFAIRS_NOMENCLATURE_LINK
-- begin EXAMPLE_OUTGOING_DOCUMENTS_WORKERS_LINK
create table EXAMPLE_OUTGOING_DOCUMENTS_WORKERS_LINK (
    OUTGOING_DOCUMENTS_ID varchar(36) not null,
    WORKERS_ID varchar(36) not null,
    primary key (OUTGOING_DOCUMENTS_ID, WORKERS_ID)
)^
-- end EXAMPLE_OUTGOING_DOCUMENTS_WORKERS_LINK
-- begin EXAMPLE_OUTGOING_DOCUMENTS_REGISTRATION_LOGS_LINK
create table EXAMPLE_OUTGOING_DOCUMENTS_REGISTRATION_LOGS_LINK (
    OUTGOING_DOCUMENTS_ID varchar(36) not null,
    REGISTRATION_LOGS_ID varchar(36) not null,
    primary key (OUTGOING_DOCUMENTS_ID, REGISTRATION_LOGS_ID)
)^
-- end EXAMPLE_OUTGOING_DOCUMENTS_REGISTRATION_LOGS_LINK
