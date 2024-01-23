create table users
(
    user_id      int auto_increment
        primary key,
    first_name   varchar(32)  not null,
    last_name    varchar(32)  not null,
    username     varchar(50)  not null,
    password     varchar(20)  not null,
    email        varchar(100) not null,
    is_admin     tinyint(1)   null,
    is_banned    tinyint(1)   null,
    phone_number varchar(15)  not null
);

create table posts
(
    post_id       int auto_increment
        primary key,
    created_by_id int           null,
    title         varchar(64)   not null,
    content       varchar(8192) not null,
    creation_date timestamp     not null,
    `like`        int           not null,
    dislike       int           not null,
    constraint posts_users_user_id_fk
        foreign key (created_by_id) references users (user_id)
);

create table comments
(
    comment_id    int auto_increment
        primary key,
    created_by_id int       null,
    content       text      not null,
    post_id       int       null,
    creation_date timestamp not null,
    `like`        int       not null,
    dislike       int       not null,
    constraint comments_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint comments_users_user_id_fk
        foreign key (created_by_id) references users (user_id)
);

