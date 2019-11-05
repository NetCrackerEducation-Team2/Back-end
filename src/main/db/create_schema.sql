-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2019-11-05 10:12:19.253

-- tables
-- Table: achievements
CREATE TABLE achievements (
    achievement_id serial  NOT NULL,
    name varchar(255)  NOT NULL,
    requirment text  NOT NULL,
    CONSTRAINT achievements_pk PRIMARY KEY (achievement_id)
);

-- Table: activities
CREATE TABLE activities (
    activity_id serial  NOT NULL,
    name varchar(255)  NOT NULL,
    description text  NOT NULL,
    user_id int  NOT NULL,
    creation_time timestamp  NOT NULL,
    CONSTRAINT activities_pk PRIMARY KEY (activity_id)
);

CREATE INDEX activities_user_id_index on activities (user_id ASC);

-- Table: announcement_tags
CREATE TABLE announcement_tags (
    anounce_tags_id serial  NOT NULL,
    announcement_id int  NOT NULL,
    tag_id int  NOT NULL,
    CONSTRAINT announcement_tags_pk PRIMARY KEY (anounce_tags_id)
);

CREATE INDEX announcement_tags_announcement_id_index on announcement_tags (announcement_id ASC);

CREATE INDEX announcement_tags_tag_id_index on announcement_tags (tag_id ASC);

-- Table: announcements
CREATE TABLE announcements (
    announcement_id serial  NOT NULL,
    title varchar(255)  NOT NULL,
    description text  NOT NULL,
    user_id int  NOT NULL,
    published boolean  NOT NULL,
    creation_time timestamp  NOT NULL,
    book_id int  NOT NULL,
    CONSTRAINT announcements_pk PRIMARY KEY (announcement_id)
);

CREATE INDEX announcements_user_id_index on announcements (user_id ASC);

CREATE INDEX announcements_book_id_index on announcements (book_id ASC);

-- Table: authorization_links
CREATE TABLE authorization_links (
    link_id serial  NOT NULL,
    token varchar(100)  NOT NULL,
    expiration timestamp  NOT NULL,
    used boolean  NOT NULL,
    user_id int  NOT NULL,
    is_registration_token boolean  NOT NULL,
    CONSTRAINT authorization_links_pk PRIMARY KEY (link_id)
);

CREATE INDEX authorization_links_user_id_index on authorization_links (user_id ASC);

-- Table: authors
CREATE TABLE authors (
    author_id serial  NOT NULL,
    full_name varchar(255)  NOT NULL,
    description text  NULL,
    CONSTRAINT authors_pk PRIMARY KEY (author_id)
);

-- Table: book_overviews
CREATE TABLE book_overviews (
    book_overview_id serial  NOT NULL,
    description text  NOT NULL,
    book_id int  NOT NULL,
    user_id int  NOT NULL,
    published boolean  NOT NULL,
    creation_time timestamp  NOT NULL,
    CONSTRAINT book_overviews_pk PRIMARY KEY (book_overview_id)
);

CREATE INDEX book_overviews_book_id_index on book_overviews (book_id ASC);

CREATE INDEX book_overviews_user_id_index on book_overviews (user_id ASC);

-- Table: book_reviews
CREATE TABLE book_reviews (
    book_review_id serial  NOT NULL,
    rating int  NOT NULL,
    description text  NOT NULL,
    user_id int  NOT NULL,
    published boolean  NOT NULL,
    creation_time timestamp  NOT NULL,
    book_id int  NOT NULL,
    CONSTRAINT book_reviews_pk PRIMARY KEY (book_review_id)
);

CREATE INDEX book_reviews_user_id_index on book_reviews (user_id ASC);

CREATE INDEX book_reviews_book_id_index on book_reviews (book_id ASC);

-- Table: books
CREATE TABLE books (
    book_id serial  NOT NULL,
    title varchar(255)  NOT NULL,
    isbn int  NOT NULL,
    release date  NOT NULL,
    pages int  NOT NULL,
    file_path text  NOT NULL,
    photo_path text  NOT NULL,
    publishing_house varchar(255)  NOT NULL,
    rate_sum int  NOT NULL,
    voters_count int  NOT NULL,
    creation_time timestamp  NOT NULL,
    slug varchar(255)  NOT NULL,
    CONSTRAINT books_ak_slug UNIQUE (slug) NOT DEFERRABLE  INITIALLY IMMEDIATE,
    CONSTRAINT books_pk PRIMARY KEY (book_id)
);

CREATE INDEX books_slug_index on books (slug ASC);

-- Table: books_authors
CREATE TABLE books_authors (
    books_authors_id serial  NOT NULL,
    author_id int  NOT NULL,
    book_id int  NOT NULL,
    CONSTRAINT books_authors_pk PRIMARY KEY (books_authors_id)
);

CREATE INDEX books_authors_author_id_index on books_authors (author_id ASC);

CREATE INDEX books_authors_book_id_index on books_authors (book_id ASC);

-- Table: books_genres
CREATE TABLE books_genres (
    books_genres_id serial  NOT NULL,
    genre_id int  NOT NULL,
    book_id int  NOT NULL,
    CONSTRAINT books_genres_pk PRIMARY KEY (books_genres_id)
);

CREATE INDEX books_genres_genre_id_index on books_genres (genre_id ASC);

CREATE INDEX books_genres_book_id_index on books_genres (book_id ASC);

-- Table: chat
CREATE TABLE chat (
    chat_id serial  NOT NULL,
    CONSTRAINT chat_pk PRIMARY KEY (chat_id)
);

-- Table: friends
CREATE TABLE friends (
    friends_id serial  NOT NULL,
    user1_id int  NOT NULL,
    user2_id int  NOT NULL,
    creation_time timestamp  NOT NULL,
    CONSTRAINT friends_pk PRIMARY KEY (friends_id)
);

CREATE INDEX friends_user1_index on friends (user1_id ASC);

CREATE INDEX friends_user2_index on friends (user2_id ASC);

-- Table: genres
CREATE TABLE genres (
    genre_id serial  NOT NULL,
    name varchar(50)  NOT NULL,
    description text  NULL,
    CONSTRAINT genres_pk PRIMARY KEY (genre_id)
);

-- Table: group_chat
CREATE TABLE group_chat (
    group_chat_id serial  NOT NULL,
    name varchar(255)  NOT NULL,
    photo bytea  NOT NULL,
    chat_id int  NOT NULL,
    CONSTRAINT group_chat_pk PRIMARY KEY (group_chat_id)
);

CREATE INDEX group_chat_chat_id_index on group_chat (chat_id ASC);

-- Table: group_chat_users
CREATE TABLE group_chat_users (
    group_chat_users_id serial  NOT NULL,
    user_id int  NOT NULL,
    group_chat_id int  NOT NULL,
    CONSTRAINT group_chat_users_pk PRIMARY KEY (group_chat_users_id)
);

CREATE INDEX group_chat_users_user_id_index on group_chat_users (user_id ASC);

CREATE INDEX group_chat_users_group_chat_id_index on group_chat_users (group_chat_id ASC);

-- Table: invitations
CREATE TABLE invitations (
    invitation_id serial  NOT NULL,
    invitation_source int  NOT NULL,
    invitation_target int  NOT NULL,
    creation_time timestamp  NOT NULL,
    accepted boolean  NOT NULL,
    CONSTRAINT invitations_pk PRIMARY KEY (invitation_id)
);

CREATE INDEX invitations_invitation_source on invitations (invitation_source ASC);

CREATE INDEX invitations_invitation_target on invitations (invitation_target ASC);

-- Table: local_chat
CREATE TABLE local_chat (
    local_chat_id serial  NOT NULL,
    user1_id int  NOT NULL,
    user2_id int  NOT NULL,
    chat_id int  NOT NULL,
    CONSTRAINT local_chat_pk PRIMARY KEY (local_chat_id)
);

CREATE INDEX local_chat_chat_id_index on local_chat (chat_id ASC);

CREATE INDEX local_chat_user1_id_index on local_chat (user1_id ASC);

CREATE INDEX local_chat_user2_id_index on local_chat (user2_id ASC);

-- Table: messages
CREATE TABLE messages (
    message_id serial  NOT NULL,
    sending_time timestamp  NOT NULL,
    user_id int  NOT NULL,
    chat_id int  NOT NULL,
    content text  NOT NULL,
    CONSTRAINT messages_pk PRIMARY KEY (message_id)
);

CREATE INDEX messages_chat_id_index on messages (chat_id ASC);

CREATE INDEX messages_user_id_index on messages (user_id ASC);

-- Table: review_comments
CREATE TABLE review_comments (
    comment_id serial  NOT NULL,
    author_id int  NOT NULL,
    book_review_id int  NOT NULL,
    creation_time timestamp  NOT NULL,
    content text  NOT NULL,
    CONSTRAINT review_comments_pk PRIMARY KEY (comment_id)
);

CREATE INDEX review_comments_author_id_index on review_comments (author_id ASC);

CREATE INDEX review_comments_review_id_index on review_comments (book_review_id ASC);

-- Table: roles
CREATE TABLE roles (
    role_id serial  NOT NULL,
    name varchar(50)  NOT NULL,
    description text  NOT NULL,
    CONSTRAINT roles_pk PRIMARY KEY (role_id)
);

-- Table: searching_history
CREATE TABLE searching_history (
    searching_history_id serial  NOT NULL,
    book_id int  NOT NULL,
    user_id int  NOT NULL,
    creation_time timestamp  NOT NULL,
    CONSTRAINT searching_history_pk PRIMARY KEY (searching_history_id)
);

CREATE INDEX searching_history_book_id_index on searching_history (book_id ASC);

CREATE INDEX searching_history_user_id_index on searching_history (user_id ASC);

-- Table: settings
CREATE TABLE settings (
    setting_id serial  NOT NULL,
    user_id int  NOT NULL,
    disable_notifications boolean  NOT NULL,
    make_private boolean  NOT NULL,
    CONSTRAINT settings_pk PRIMARY KEY (setting_id)
);

CREATE INDEX settings_user_id_index on settings (user_id ASC);

-- Table: tags
CREATE TABLE tags (
    tag_id serial  NOT NULL,
    name varchar(255)  NOT NULL,
    description text  NOT NULL,
    CONSTRAINT tags_pk PRIMARY KEY (tag_id)
);

-- Table: users
CREATE TABLE users (
    user_id serial  NOT NULL,
    first_name varchar(70)  NOT NULL,
    last_name varchar(70)  NOT NULL,
    password varchar(255)  NOT NULL,
    email varchar(255)  NOT NULL,
    creation_time timestamp  NOT NULL,
    enabled boolean  NOT NULL,
    photo_path text  NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id)
);

-- Table: users_achviments
CREATE TABLE users_achviments (
    users_achviments_id serial  NOT NULL,
    user_id int  NOT NULL,
    achievements_id int  NOT NULL,
    creation_time timestamp  NOT NULL,
    CONSTRAINT users_achviments_pk PRIMARY KEY (users_achviments_id)
);

CREATE INDEX users_achievements_user_id_index on users_achviments (user_id ASC);

CREATE INDEX users_achievements_achievement_id_index on users_achviments (achievements_id ASC);

-- Table: users_books
CREATE TABLE users_books (
    users_books_id serial  NOT NULL,
    book_id int  NOT NULL,
    user_id int  NOT NULL,
    favorite boolean  NOT NULL,
    read boolean  NOT NULL,
    creation_time timestamp  NOT NULL,
    CONSTRAINT users_books_pk PRIMARY KEY (users_books_id)
);

CREATE INDEX users_books_book_id_index on users_books (book_id ASC);

CREATE INDEX users_books_user_id_index on users_books (user_id ASC);

-- Table: users_roles
CREATE TABLE users_roles (
    users_roles_id serial  NOT NULL,
    user_id int  NOT NULL,
    role_id int  NOT NULL,
    CONSTRAINT users_roles_pk PRIMARY KEY (users_roles_id)
);

CREATE INDEX users_roles_user_id_index on users_roles (user_id ASC);

CREATE INDEX users_roles_role_id_index on users_roles (role_id ASC);

-- foreign keys
-- Reference: activities_users (table: activities)
ALTER TABLE activities ADD CONSTRAINT activities_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: announcement_tags_announcements (table: announcement_tags)
ALTER TABLE announcement_tags ADD CONSTRAINT announcement_tags_announcements
    FOREIGN KEY (announcement_id)
    REFERENCES announcements (announcement_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: announcement_tags_tags (table: announcement_tags)
ALTER TABLE announcement_tags ADD CONSTRAINT announcement_tags_tags
    FOREIGN KEY (tag_id)
    REFERENCES tags (tag_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: announcements_books (table: announcements)
ALTER TABLE announcements ADD CONSTRAINT announcements_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: announcements_users (table: announcements)
ALTER TABLE announcements ADD CONSTRAINT announcements_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: book_genres_genres (table: books_genres)
ALTER TABLE books_genres ADD CONSTRAINT book_genres_genres
    FOREIGN KEY (genre_id)
    REFERENCES genres (genre_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: book_overview_books (table: book_overviews)
ALTER TABLE book_overviews ADD CONSTRAINT book_overview_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: book_overviews_users (table: book_overviews)
ALTER TABLE book_overviews ADD CONSTRAINT book_overviews_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: book_reviews_books (table: book_reviews)
ALTER TABLE book_reviews ADD CONSTRAINT book_reviews_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: book_reviews_users (table: book_reviews)
ALTER TABLE book_reviews ADD CONSTRAINT book_reviews_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: books_authors_authors (table: books_authors)
ALTER TABLE books_authors ADD CONSTRAINT books_authors_authors
    FOREIGN KEY (author_id)
    REFERENCES authors (author_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: books_authors_books (table: books_authors)
ALTER TABLE books_authors ADD CONSTRAINT books_authors_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: books_genres_books (table: books_genres)
ALTER TABLE books_genres ADD CONSTRAINT books_genres_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: friends_users_1 (table: friends)
ALTER TABLE friends ADD CONSTRAINT friends_users_1
    FOREIGN KEY (user2_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: friends_users_2 (table: friends)
ALTER TABLE friends ADD CONSTRAINT friends_users_2
    FOREIGN KEY (user1_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: group_chat_chat (table: group_chat)
ALTER TABLE group_chat ADD CONSTRAINT group_chat_chat
    FOREIGN KEY (chat_id)
    REFERENCES chat (chat_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: group_chat_users_group_chat (table: group_chat_users)
ALTER TABLE group_chat_users ADD CONSTRAINT group_chat_users_group_chat
    FOREIGN KEY (group_chat_id)
    REFERENCES group_chat (group_chat_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: group_chat_users_users (table: group_chat_users)
ALTER TABLE group_chat_users ADD CONSTRAINT group_chat_users_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: invitations_users_source (table: invitations)
ALTER TABLE invitations ADD CONSTRAINT invitations_users_source
    FOREIGN KEY (invitation_source)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: invitations_users_target (table: invitations)
ALTER TABLE invitations ADD CONSTRAINT invitations_users_target
    FOREIGN KEY (invitation_target)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: local_chat_chat (table: local_chat)
ALTER TABLE local_chat ADD CONSTRAINT local_chat_chat
    FOREIGN KEY (chat_id)
    REFERENCES chat (chat_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: local_chat_user1 (table: local_chat)
ALTER TABLE local_chat ADD CONSTRAINT local_chat_user1
    FOREIGN KEY (user1_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: local_chat_user2 (table: local_chat)
ALTER TABLE local_chat ADD CONSTRAINT local_chat_user2
    FOREIGN KEY (user2_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: messages_chat (table: messages)
ALTER TABLE messages ADD CONSTRAINT messages_chat
    FOREIGN KEY (chat_id)
    REFERENCES chat (chat_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: messages_users (table: messages)
ALTER TABLE messages ADD CONSTRAINT messages_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: pass_reset_links_users (table: authorization_links)
ALTER TABLE authorization_links ADD CONSTRAINT pass_reset_links_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_comments_book_reviews (table: review_comments)
ALTER TABLE review_comments ADD CONSTRAINT review_comments_book_reviews
    FOREIGN KEY (book_review_id)
    REFERENCES book_reviews (book_review_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: review_comments_users (table: review_comments)
ALTER TABLE review_comments ADD CONSTRAINT review_comments_users
    FOREIGN KEY (author_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: searching_history_books (table: searching_history)
ALTER TABLE searching_history ADD CONSTRAINT searching_history_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: searching_history_users (table: searching_history)
ALTER TABLE searching_history ADD CONSTRAINT searching_history_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: settings_users (table: settings)
ALTER TABLE settings ADD CONSTRAINT settings_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_achviments_achviments (table: users_achviments)
ALTER TABLE users_achviments ADD CONSTRAINT users_achviments_achviments
    FOREIGN KEY (achievements_id)
    REFERENCES achievements (achievement_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_achviments_users (table: users_achviments)
ALTER TABLE users_achviments ADD CONSTRAINT users_achviments_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_books_books (table: users_books)
ALTER TABLE users_books ADD CONSTRAINT users_books_books
    FOREIGN KEY (book_id)
    REFERENCES books (book_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_books_users (table: users_books)
ALTER TABLE users_books ADD CONSTRAINT users_books_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_roles_roles (table: users_roles)
ALTER TABLE users_roles ADD CONSTRAINT users_roles_roles
    FOREIGN KEY (role_id)
    REFERENCES roles (role_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: users_roles_users (table: users_roles)
ALTER TABLE users_roles ADD CONSTRAINT users_roles_users
    FOREIGN KEY (user_id)
    REFERENCES users (user_id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- End of file.

