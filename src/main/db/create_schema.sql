create table achievements
(
    achievement_id serial       not null
        constraint achievements_pk
            primary key,
    name           varchar(255) not null,
    requirment     text         not null
);

create table authors
(
    author_id   serial       not null
        constraint authors_pk
            primary key,
    full_name   varchar(255) not null,
    description text
);

create table books
(
    book_id          serial                  not null
        constraint books_pk
            primary key,
    title            varchar(255)            not null,
    isbn             integer                 not null,
    release          date                    not null,
    pages            integer                 not null,
    file_path        text                    not null,
    photo_path       text                    not null,
    publishing_house varchar(255)            not null,
    rate_sum         integer                 not null,
    voters_count     integer                 not null,
    slug             varchar(255)            not null
        constraint books_ak_slug
            unique,
    creation_time    timestamp default now() not null
);

create index books_slug_index
    on books (slug);

create table books_authors
(
    books_authors_id serial  not null
        constraint books_authors_pk
            primary key,
    author_id        integer not null
        constraint books_authors_authors
            references authors,
    book_id          integer not null
        constraint books_authors_books
            references books
);

create index books_authors_author_id_index
    on books_authors (author_id);

create index books_authors_book_id_index
    on books_authors (book_id);

create table chat
(
    chat_id serial not null
        constraint chat_pk
            primary key
);

create table genres
(
    genre_id    serial      not null
        constraint genres_pk
            primary key,
    name        varchar(50) not null,
    description text
);

create table books_genres
(
    books_genres_id serial  not null
        constraint books_genres_pk
            primary key,
    genre_id        integer not null
        constraint book_genres_genres
            references genres,
    book_id         integer not null
        constraint books_genres_books
            references books
);

create index books_genres_genre_id_index
    on books_genres (genre_id);

create index books_genres_book_id_index
    on books_genres (book_id);

create table group_chat
(
    group_chat_id serial       not null
        constraint group_chat_pk
            primary key,
    name          varchar(255) not null,
    photo         bytea        not null,
    chat_id       integer      not null
        constraint group_chat_chat
            references chat
);

create index group_chat_chat_id_index
    on group_chat (chat_id);

create table roles
(
    role_id     serial      not null
        constraint roles_pk
            primary key,
    name        varchar(50) not null,
    description text        not null
);

create table users
(
    user_id       serial                  not null
        constraint users_pk
            primary key,
    password      varchar(255)            not null,
    email         varchar(255)            not null
        constraint users_ak_email
            unique,
    creation_time timestamp default now() not null,
    enabled       boolean   default true  not null,
    photo_path    text,
    full_name     varchar(255)            not null
);

create table activities
(
    activity_id   serial                  not null
        constraint activities_pk
            primary key,
    name          varchar(255)            not null,
    description   text                    not null,
    user_id       integer                 not null
        constraint activities_users
            references users,
    creation_time timestamp default now() not null
);

create index activities_user_id_index
    on activities (user_id);

create table announcements
(
    announcement_id serial                  not null
        constraint announcements_pk
            primary key,
    title           varchar(255)            not null,
    description     text                    not null,
    user_id         integer                 not null
        constraint announcements_users
            references users,
    book_id         integer                 not null
        constraint announcements_books
            references books,
    published       boolean   default false not null,
    creation_time   timestamp default now() not null
);

create index announcements_user_id_index
    on announcements (user_id);

create index announcements_book_id_index
    on announcements (book_id);

create table authorization_links
(
    link_id               serial                not null
        constraint authorization_links_pk
            primary key,
    token                 varchar(100)          not null,
    expiration            timestamp             not null,
    user_id               integer               not null
        constraint pass_reset_links_users
            references users,
    is_registration_token boolean               not null,
    used                  boolean default false not null
);

create index authorization_links_user_id_index
    on authorization_links (user_id);

create table book_overviews
(
    book_overview_id serial                  not null
        constraint book_overviews_pk
            primary key,
    description      text                    not null,
    book_id          integer                 not null
        constraint book_overview_books
            references books,
    user_id          integer                 not null
        constraint book_overviews_users
            references users,
    published        boolean   default false not null,
    creation_time    timestamp default now() not null
);

create index book_overviews_book_id_index
    on book_overviews (book_id);

create index book_overviews_user_id_index
    on book_overviews (user_id);

create table book_reviews
(
    book_review_id serial                  not null
        constraint book_reviews_pk
            primary key,
    rating         integer                 not null,
    description    text                    not null,
    user_id        integer                 not null
        constraint book_reviews_users
            references users,
    book_id        integer                 not null
        constraint book_reviews_books
            references books,
    published      boolean   default false not null,
    creation_time  timestamp default now() not null
);

create index book_reviews_user_id_index
    on book_reviews (user_id);

create index book_reviews_book_id_index
    on book_reviews (book_id);

create table friends
(
    friends_id    serial                  not null
        constraint friends_pk
            primary key,
    user1_id      integer                 not null
        constraint friends_users_2
            references users,
    user2_id      integer                 not null
        constraint friends_users_1
            references users,
    creation_time timestamp default now() not null
);

create index friends_user1_index
    on friends (user1_id);

create index friends_user2_index
    on friends (user2_id);

create table group_chat_users
(
    group_chat_users_id serial  not null
        constraint group_chat_users_pk
            primary key,
    user_id             integer not null
        constraint group_chat_users_users
            references users,
    group_chat_id       integer not null
        constraint group_chat_users_group_chat
            references group_chat
);

create index group_chat_users_user_id_index
    on group_chat_users (user_id);

create index group_chat_users_group_chat_id_index
    on group_chat_users (group_chat_id);

create table invitations
(
    invitation_id     serial                  not null
        constraint invitations_pk
            primary key,
    invitation_source integer                 not null
        constraint invitations_users_source
            references users,
    invitation_target integer                 not null
        constraint invitations_users_target
            references users,
    accepted          boolean   default false not null,
    creation_time     timestamp default now() not null
);

create index invitations_invitation_source
    on invitations (invitation_source);

create index invitations_invitation_target
    on invitations (invitation_target);

create table local_chat
(
    local_chat_id serial  not null
        constraint local_chat_pk
            primary key,
    user1_id      integer not null
        constraint local_chat_user1
            references users,
    user2_id      integer not null
        constraint local_chat_user2
            references users,
    chat_id       integer not null
        constraint local_chat_chat
            references chat
);

create index local_chat_chat_id_index
    on local_chat (chat_id);

create index local_chat_user1_id_index
    on local_chat (user1_id);

create index local_chat_user2_id_index
    on local_chat (user2_id);

create table messages
(
    message_id   serial                  not null
        constraint messages_pk
            primary key,
    user_id      integer                 not null
        constraint messages_users
            references users,
    chat_id      integer                 not null
        constraint messages_chat
            references chat,
    content      text                    not null,
    sending_time timestamp default now() not null
);

create index messages_chat_id_index
    on messages (chat_id);

create index messages_user_id_index
    on messages (user_id);

create table review_comments
(
    comment_id     serial                  not null
        constraint review_comments_pk
            primary key,
    author_id      integer                 not null
        constraint review_comments_users
            references users,
    book_review_id integer                 not null
        constraint review_comments_book_reviews
            references book_reviews,
    content        text                    not null,
    creation_time  timestamp default now() not null
);

create index review_comments_author_id_index
    on review_comments (author_id);

create index review_comments_review_id_index
    on review_comments (book_review_id);

create table searching_history
(
    searching_history_id serial                  not null
        constraint searching_history_pk
            primary key,
    book_id              integer                 not null
        constraint searching_history_books
            references books,
    user_id              integer                 not null
        constraint searching_history_users
            references users,
    creation_time        timestamp default now() not null
);

create index searching_history_book_id_index
    on searching_history (book_id);

create index searching_history_user_id_index
    on searching_history (user_id);

create table settings
(
    setting_id            serial  not null
        constraint settings_pk
            primary key,
    user_id               integer not null
        constraint settings_ak_user_id
            unique
        constraint settings_users
            references users,
    disable_notifications boolean not null,
    make_private          boolean not null
);

create index settings_user_id_index
    on settings (user_id);

create table users_achviments
(
    users_achviments_id serial                  not null
        constraint users_achviments_pk
            primary key,
    user_id             integer                 not null
        constraint users_achviments_users
            references users,
    achievements_id     integer                 not null
        constraint users_achviments_achviments
            references achievements,
    creation_time       timestamp default now() not null
);

create index users_achievements_user_id_index
    on users_achviments (user_id);

create index users_achievements_achievement_id_index
    on users_achviments (achievements_id);

create table users_books
(
    users_books_id serial                  not null
        constraint users_books_pk
            primary key,
    book_id        integer                 not null
        constraint users_books_books
            references books,
    user_id        integer                 not null
        constraint users_books_users
            references users,
    favorite       boolean   default false not null,
    read           boolean   default false not null,
    creation_time  timestamp default now() not null
);

create index users_books_book_id_index
    on users_books (book_id);

create index users_books_user_id_index
    on users_books (user_id);

create table users_roles
(
    users_roles_id serial  not null
        constraint users_roles_pk
            primary key,
    user_id        integer not null
        constraint users_roles_users
            references users,
    role_id        integer not null
        constraint users_roles_roles
            references roles
);

create index users_roles_user_id_index
    on users_roles (user_id);

create index users_roles_role_id_index
    on users_roles (role_id);
