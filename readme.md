CREATE TABLE public.tb_user (
id int8 NOT NULL DEFAULT nextval('seq_tb_user'::regclass),
name varchar(100) NOT NULL,
age int NOT NULL,
address varchar(1000) NOT NULL,
email_address varchar(1000) NOT NULL,
CONSTRAINT pk_user PRIMARY KEY (id)
);


insert into public.tb_user(name, age, address, EMAIL_ADDRESS)
values('Potato', 45, 'New york', 'potato@naver.com');

insert into public.tb_user(name, age, address, EMAIL_ADDRESS)
values('mary', 3, 'America', 'mary@hotmail.com');

insert into public.tb_user(name, age, address, EMAIL_ADDRESS)
values('June', 28, 'Seoul', 'june@hotmail.com');

insert into public.tb_user(name, age, address, EMAIL_ADDRESS)
values('Paul', 31, 'New york', 'paul@hotmail.com');


select  * from public.tb_user