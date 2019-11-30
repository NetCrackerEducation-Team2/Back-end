create table notification_types
(
    notification_type_id integer       not null
        constraint entity_types_pk
            primary key,
    notification_type_name  varchar(255)            not null
);

insert into notification_types (notification_type_id, notification_type_name) values (10, 'announcements');
insert into notification_types (notification_type_id, notification_type_name) values (11, 'book_reviews');
insert into notification_types (notification_type_id, notification_type_name) values (12, 'book_overviews');
insert into notification_types (notification_type_id, notification_type_name) values (13, 'invitations');
insert into notification_types (notification_type_id, notification_type_name) values (14, 'activities');
insert into notification_types (notification_type_id, notification_type_name) values (15, 'achievements');

create table notification_messages
(
    notification_message_id integer       not null
        constraint notification_actions_pk
            primary key,
    notification_message_text  varchar(255)            not null
);

insert into notification_messages (notification_message_id, notification_message_text) values (10, 'Hello');
insert into notification_messages (notification_message_id, notification_message_text) values (11, 'You have new invitation to friend.');
insert into notification_messages (notification_message_id, notification_message_text) values (12, 'You created new announcement. Please wait until our moderator will publish your announcement.');
insert into notification_messages (notification_message_id, notification_message_text) values (13, 'You created new book review. Please wait until our moderator will publish your book review.');
insert into notification_messages (notification_message_id, notification_message_text) values (14, 'Your book review was published by moderator.');
insert into notification_messages (notification_message_id, notification_message_text) values (15, 'Your announcement was published by moderator.');
insert into notification_messages (notification_message_id, notification_message_text) values (16, 'Your book review was published by moderator.');

create table notification_objects
(
    notification_object_id serial       not null
        constraint notification_objects_pk
            primary key,
    notification_type_id          integer not null
        constraint notification_objects_notification_types
            references notification_types,
    entity_id          integer not null,
    user_id          integer not null
        constraint notifications_objects_user
            references users,
    notification_message_id          integer not null
        constraint notification_objects_notification_messages
            references notification_messages,
    creation_time timestamp default now() not null,
    send_all boolean default false not null,
    is_read boolean default false not null
);
insert into notification_objects (notification_type_id, entity_id, user_id, notification_message_id) values (10,1,1,12);
create table notifications
(
    notification_id serial       not null
        constraint notifications_pk
            primary key,
    notification_object_id          integer not null
        constraint notifications_notification_object
            references notification_objects,
    notifier_id          integer not null
        constraint notifications_notifier
            references users,
    is_read boolean default false not null
);
insert into notifications (notification_object_id, notifier_id) values (1, 1);