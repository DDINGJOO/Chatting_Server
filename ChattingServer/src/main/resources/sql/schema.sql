create table if not exists message(
    message_sequence bigint auto_increment,
    user_name varchar(20) not null,
    content varchar(1000) not null ,
    created_at timestamp not null,
    updated_at timestamp not null,

    primary key(message_sequence)
) engine=innodb default charset=utf8mb4 collate utf8mb4_0900_ai_ci;
