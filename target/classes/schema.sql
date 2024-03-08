-- drop all tables 
-- ( in reverse order of creation due to foreign key constraints )

drop table money_transfer;

drop table person;

drop table person_account;

-- create tables;




create table person (
	person_id varchar(25) not null primary key,
	name varchar(250) not null,
	balance integer not null
);

create table person_account (
    account_number varchar(25) primary key,
    account_balance integer not null,
    account_type varchar(50),
    person_id varchar(25) not null,
    constraint fk_person foreign key (person_id) references person(person_id)
);

CREATE TABLE money_transfer (
    money_transfer_id varchar(25) NOT NULL PRIMARY KEY,
    created_time timestamp NOT NULL,
    transfer_time timestamp,
    from_person_id varchar(25) NOT NULL,
    to_person_id varchar(25) NOT NULL,
    from_account_id varchar(25) NOT NULL,
    to_account_id varchar(25) NOT NULL,
    amount integer NOT NULL,
    purpose varchar(100) NOT NULL,
    status varchar(10) NOT NULL,
    status_details varchar(250) NOT NULL,
    CONSTRAINT fk_money_transfer_from FOREIGN KEY (from_person_id) REFERENCES person(person_id),
    CONSTRAINT fk_money_transfer_to FOREIGN KEY (to_person_id) REFERENCES person(person_id),
    CONSTRAINT fk_money_transfer_from_account FOREIGN KEY (from_account_id) REFERENCES person_account(account_number),
    CONSTRAINT fk_money_transfer_to_account FOREIGN KEY (to_account_id) REFERENCES person_account(account_number)
);

--Data Section

insert into person(person_id, name, balance)
values ('1', 'Jay', 2000),
	   ('2', 'Meet', 3000),
	   ('3', 'Aditya', 3000);
	   
insert into person_account(account_number, account_balance, account_type, person_id)
values('A01', 1000, 'Saving', '2'),
		('A02', 500, 'FD', '1'),
		('A03', 2000, 'Savings', '3'),
		('A04', 700, 'FD', '2');
