create table EXAMPLE_MATCHING_TABLE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    MATCHER_ID uuid,
    START_DATE date,
    END_DATE date,
    RESULT_ varchar(255),
    COMMENT_ varchar(255),
    OUTGOING_DOCUMENTS_ID uuid,
    --
    primary key (ID)
);
