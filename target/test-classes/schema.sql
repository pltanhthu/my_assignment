create table if not exists sensor_readings(
  id int not null auto_increment,
  sensor_id varchar(100) not null,
  timestamp timestamp not null,
  temperature double not null,
  humidity double not null,
  primary key ( id )
);