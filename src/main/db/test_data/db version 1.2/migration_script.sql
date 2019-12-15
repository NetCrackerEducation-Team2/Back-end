-- migration script for migrate from previous version to version specified by commit 74758b68941be1727a1e54ebd7dc8e5beb51c311 (version 1.2)
alter table achievements add column table_name varchar(255) not null;
alter table achievements add column description text;
alter table achievements add column sql_query text not null;

alter table friends rename column user1_id to user_id;

alter table local_chat rename column user1_id to user_id;
alter table review_comments rename column author_id to user_id;

alter table users_achviments rename to users_achievements;
alter table users_achievements rename column users_achviments_id to users_achievements_id;
alter table users_achievements rename column achievements_id to achievement_id;

ALTER TABLE notification_objects ALTER COLUMN creation_time TYPE timestamp with time zone;