-- begin EXAMPLE_WORKERS
create table EXAMPLE_WORKERS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PERSONNEL_NUMBER varchar(255) not null,
    USER_ID uuid,
    SECOND_NAME varchar(255) not null,
    FIRST_NAME varchar(255) not null,
    PATRONYMIC varchar(255),
    SUB_DIVISION_ID uuid,
    EMAIL varchar(255),
    PHONE varchar(255),
    PHOTO_ID uuid,
    --
    primary key (ID)
)^
-- end EXAMPLE_WORKERS
-- begin EXAMPLE_SUB_DIVISION
create table EXAMPLE_SUB_DIVISION (
    ID uuid,
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
    LEAD_SUBDIVISION_ID uuid,
    DEPARTAMENT_HEAD_ID uuid,
    --
    primary key (ID)
)^
-- end EXAMPLE_SUB_DIVISION
-- begin EXAMPLE_DOCUMENT_TYPES
create table EXAMPLE_DOCUMENT_TYPES (
    ID uuid,
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
    SERIAL_NUMBER bigint,
    --
    primary key (ID)
)^
-- end EXAMPLE_DOCUMENT_TYPES
-- begin EXAMPLE_ORGANIZATIONS
create table EXAMPLE_ORGANIZATIONS (
    ID uuid,
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
    SERIAL_NUMBER bigint,
    --
    primary key (ID)
)^
-- end EXAMPLE_ORGANIZATIONS
-- begin EXAMPLE_REGISTRATION_LOGS
create table EXAMPLE_REGISTRATION_LOGS (
    ID uuid,
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
    NUMBER_FORMAT text not null,
    NUMBER_ varchar(255),
    SERIAL_NUMBER bigint,
    --
    primary key (ID)
)^
-- end EXAMPLE_REGISTRATION_LOGS

-- begin EXAMPLE_OUTGOING_DOCUMENTS
create table EXAMPLE_OUTGOING_DOCUMENTS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DOCUMENT_TYPE_ID uuid,
    SERIAL_NUMBER bigint,
    REGISTRATION_NUMBER varchar(255),
    DATE_ date,
    ADDRESSEE_ID uuid,
    FULL_NAME varchar(255),
    TOPIC varchar(255),
    EXECUTOR_ID uuid,
    SIGN_ID uuid,
    DESCRIPTION text,
    TITLE varchar(255),
    AUTHOR_ID uuid,
    CREATE_DATE date,
    CHANGE_DATE date,
    STATE varchar(50),
    LOG_ID uuid,
    DOCUMENT_DESCRIPTION varchar(255),
    AFFAIR_ID uuid,
    AFFAIR_DATE date,
    --
    primary key (ID)
)^
-- end EXAMPLE_OUTGOING_DOCUMENTS

-- begin EXAMPLE_AFFAIR_NOMENCLATURE
create table EXAMPLE_AFFAIR_NOMENCLATURE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    SERIAL_NUMBER bigint not null,
    TITLE varchar(255),
    --
    primary key (ID)
)^
-- end EXAMPLE_AFFAIR_NOMENCLATURE
-- begin EXAMPLE_OUTGOING_DOCUMENTS_FILE_DESCRIPTOR_LINK
create table EXAMPLE_OUTGOING_DOCUMENTS_FILE_DESCRIPTOR_LINK (
    OUTGOING_DOCUMENTS_ID uuid,
    FILE_DESCRIPTOR_ID uuid,
    primary key (OUTGOING_DOCUMENTS_ID, FILE_DESCRIPTOR_ID)
)^
-- end EXAMPLE_OUTGOING_DOCUMENTS_FILE_DESCRIPTOR_LINK
