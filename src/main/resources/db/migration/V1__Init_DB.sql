create table hibernate_sequence (next_val bigint) engine=InnoDB;

insert into hibernate_sequence values ( 1 );

create table product (
    id bigint not null,
    count integer not null,
    description varchar(255),
    filename varchar(255),
    price integer,
    producttitle varchar(255),
    raiting integer not null,
    user_id bigint,
    primary key (id)
    );

create table user (
    id bigint not null,
    activation_code varchar(255),
    active bit not null,
    email varchar(255),
    password varchar(255) not null,
    username varchar(255) not null,
    primary key (id)
);

    create table user_role (
    user_id bigint not null,
    roles varchar(255)
);

alter table product
    add constraint FK979liw4xk18ncpl87u4tygx2u
    foreign key (user_id) references user (id);
alter table user_role
    add constraint FK859n2jvi8ivhui0rl0esws6o
    foreign key (user_id) references user (id);
