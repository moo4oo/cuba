update EXAMPLE_AFFAIRS_NOMENCLATURE set SERIAL_NUMBER = 0 where SERIAL_NUMBER is null ;
alter table EXAMPLE_AFFAIRS_NOMENCLATURE alter column SERIAL_NUMBER set not null ;