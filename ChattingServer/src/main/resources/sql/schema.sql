create table if not exists messageRequest
(
    message_sequence bigint auto_increment,
    user_name        varchar(20)   not null,
    content          varchar(1000) not null,
    created_at       timestamp     not null,
    updated_at       timestamp     not null,

    primary key (message_sequence)
) engine = innodb
  default charset = utf8mb4
  collate utf8mb4_0900_ai_ci;


create table if not exists message_user
(
    user_id    bigint auto_increment,
    username   varchar(20)  not null,
    password   varchar(255) not null,
    connection_invite_code varchar(20) not null,
    connection_count int not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,
    primary key (user_id),
    constraint unique_username unique (username),
    constraint unique_connection_invite_code unique (connection_invite_code)
) engine = innodb
  default charset = utf8mb4
  collate utf8mb4_0900_ai_ci;

create table if not exists user_connection
(
    partner_a_user_id bigint not null,
    partner_b_user_id bigint not null,
    status varchar(20) not null,
    inviter_user_id bigint not null,
    created_at timestamp    not null,
    updated_at timestamp    not null,

    primary key (partner_a_user_id, partner_b_user_id),
    index idx_partner_b_user_id (partner_b_user_id),
    index idx_partner_a_user_id_status (partner_a_user_id, status),
    index idx_partner_b_user_id_status (partner_b_user_id, status)
) engine = innodb
  default charset = utf8mb4
  collate utf8mb4_0900_ai_ci;

-- Insert test user (username: devuser, password: devpass)
-- BCrypt hash generated for password 'devpass'
INSERT INTO message_user (username, password, created_at, updated_at)
SELECT 'devuser', '$2a$10$/E6xCBDobZRMqm0uoQGHWe6V4rjuxVLzOc4yMA3APnDmU39/Vk0AW', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM message_user WHERE username = 'devuser');
