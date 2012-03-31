create table oceanrunner_statisticsresult (
  runDate                   timestamp,
  status                    integer,
  comments                  varchar(255),
  classundertestName        varchar(255),
  methodundertestName       varchar(255),
  environement              varchar(255),
  constraint ck_oceanrunner_statisticsresult_status check (status in (0,1,2,3)))
;



