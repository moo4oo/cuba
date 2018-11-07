alter table EXAMPLE_OUTGOING_DOCUMENTS rename column comments to comments__u72768 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS rename column result_ to result___u66390 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS rename column end_date to end_date__u56476 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS rename column start_date to start_date__u63704 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS rename column matching_id to matching_id__u91650 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS rename column affair_id to affair_id__u07522 ;
alter table EXAMPLE_OUTGOING_DOCUMENTS add column DOC_AFFAIR_ID uuid ;
