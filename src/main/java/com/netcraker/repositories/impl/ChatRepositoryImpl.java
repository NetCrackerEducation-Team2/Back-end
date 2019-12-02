package com.netcraker.repositories.impl;

import com.netcraker.model.ChatMessage;
import com.netcraker.model.mapper.ChatMessageRowMapper;
import com.netcraker.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sqlQueries.properties")
public class ChatRepositoryImpl implements ChatRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChatRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Value("${message.findByUsersId}")
    private String sqlFindByUsersId;


    @Override
    public List<ChatMessage> listMessage(int user1_id, int user2_id) {
        Object[] params = {user1_id, user2_id};
        return jdbcTemplate.query(sqlFindByUsersId, params, new ChatMessageRowMapper());
    }
}
