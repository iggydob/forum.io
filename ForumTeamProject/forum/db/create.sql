create table phone_numers
(
    phone_number_id int auto_increment
        primary key,
    phone_number    varchar(15) not null
);

create table tags
(
    tag_id   int auto_increment
        primary key,
    tag_name int not null
);

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
    phone_number varchar(15)  null
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

create table disliked_posts
(
    disliked_posts_id int auto_increment
        primary key,
    post_id           int not null,
    user_id           int not null,
    constraint disliked_posts_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint disliked_posts_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table liked_posts
(
    liked_posts_id int auto_increment
        primary key,
    post_id        int not null,
    user_id        int not null,
    constraint liked_posts_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint liked_posts_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table posts_tags
(
    posts_tags_id int auto_increment
        primary key,
    post_id       int not null,
    tag_id        int not null,
    constraint posts_tags_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint posts_tags_tags_tag_id_fk
        foreign key (tag_id) references tags (tag_id)
);

create table users_comments
(
    users_comments_id int auto_increment
        primary key,
    user_id           int not null,
    comment_id        int not null,
    constraint users_comments_comments_comment_id_fk
        foreign key (comment_id) references comments (comment_id),
    constraint users_comments_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_phone_numbers
(
    users_phone_numbers_id int auto_increment
        primary key,
    user_id                int not null,
    phone_number_id        int not null,
    constraint users_phone_numbers_phone_numbers_phone_number_id_fk
        foreign key (phone_number_id) references phone_numers (phone_number_id),
    constraint users_phone_numbers_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

create table users_posts
(
    users_posts_id int auto_increment
        primary key,
    user_id        int not null,
    post_id        int not null,
    constraint users_posts_posts_post_id_fk
        foreign key (post_id) references posts (post_id),
    constraint users_posts_users_user_id_fk
        foreign key (user_id) references users (user_id)
);

