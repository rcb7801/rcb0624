create table if not exists tool_types(id identity primary key,
                                      type varchar(255) not null,
                                      daily_charge number not null,
                                      weekday_charge boolean,
                                      weekend_charge boolean,
                                      holiday_charge boolean);
truncate table tool_types;

insert into tool_types(type, daily_charge, weekday_charge, weekend_charge, holiday_charge)
values('Ladder', 199, TRUE, TRUE, FALSE);

insert into tool_types(type, daily_charge, weekday_charge, weekend_charge, holiday_charge)
values('Chainsaw', 149, TRUE, FALSE, TRUE);

insert into tool_types(type, daily_charge, weekday_charge, weekend_charge, holiday_charge)
values('Jackhammer', 299, TRUE, FALSE, FALSE);

insert into tool_types(type, daily_charge, weekday_charge, weekend_charge, holiday_charge)
values('Promotional', 1000, FALSE, FALSE, FALSE);

create table if not exists tools(id identity primary key,
                                 code varchar(255) not null,
                                 brand varchar(255) not null,
                                 tool_type_id int not null);

alter table tools
    add foreign key (tool_type_id)
        references tool_types(id);

truncate table tools;
insert into tools(code,brand,tool_type_id) values('CHNS', 'Stihl',
                                                  (select id from tool_types where type='Chainsaw'));
insert into tools(code,brand,tool_type_id) values('LADW', 'Werner',
                                                  (select id from tool_types where type='Ladder'));
insert into tools(code,brand,tool_type_id) values('JAKD', 'Dewalt',
                                                  (select id from tool_types where type='Jackhammer'));
insert into tools(code,brand,tool_type_id) values('JAKR', 'Rigid',
                                                  (select id from tool_types where type='Jackhammer'));
insert into tools(code,brand,tool_type_id) values('PROM', 'Any',
                                                  (select id from tool_types where type='Promotional'));