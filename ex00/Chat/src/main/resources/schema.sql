drop schema if exists chat cascade;
create schema if not exists chat;

create table if not exists chat.user (
    id serial primary key,
    login text not null unique,
    password text not null
);

create table if not exists chat.chatroom (
    id serial primary key,
    name text not null unique,
    owner int not null,
    foreign key (owner) references chat.user(id)
);

create table if not exists chat.message (
    id serial primary key,
    author int not null,
    room int not null,
    text text not null,
    dateTime timestamp not null,
    foreign key (author) references chat.user(id),
    foreign key (room) references chat.chatroom(id)
);
