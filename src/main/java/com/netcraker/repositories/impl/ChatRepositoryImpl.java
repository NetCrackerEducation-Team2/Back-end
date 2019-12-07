package com.netcraker.repositories.impl;

import com.netcraker.model.Chat;
import com.netcraker.model.mapper.ChatRowMapper;
import com.netcraker.model.mapper.MessageRowMapper;
import com.netcraker.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@PropertySource("classpath:sqlQueries.properties")
public class ChatRepositoryImpl implements ChatRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChatRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

//    @Value("${message.findByUsersId}")
//    private String sqlFindByUsersId;

    @Value("${chat.insert}")
    private String sqlCreateChat;

    @Value("${chat.createLocalChat}")
    private String sqlCreateLocalChat;

    @Value("${chat.findByChatId}")
    private String sqlFindByChatId;

    @Value("${chat.findByUsersId}")
    private String sqlFindByUsersId;


//    @Override
//    public List<Chat> listMessage(int user1_id, int user2_id) {
//        Object[] params = {user1_id, user2_id};
//        return jdbcTemplate.query(sqlFindByUsersId, params, new MessageRowMapper());
//    }

    @Override
    public List<Chat> listMessage(int user1_id, int user2_id) {
        return null;
    }

    @Override
    public int insert() {
        return jdbcTemplate.queryForObject(sqlCreateChat, Integer.class);
    }

    @Override
    public Optional<Chat> findLocalChat(int chatId) {
        Object[] params = {chatId};
        List<Chat> chats = jdbcTemplate.query(sqlFindByChatId, params, new ChatRowMapper());
        return chats.isEmpty() ? Optional.empty() : Optional.of(chats.get(0));
    }

    @Override
    public Optional<Chat> findLocalChat(int[] users) {
        Object[] params = {users[0], users[1], users[1], users[0]};
        List<Chat> chats = jdbcTemplate.query(sqlFindByUsersId, params, new ChatRowMapper());
        return chats.isEmpty() ? Optional.empty() : Optional.of(chats.get(0));
    }

    @Override
    public Optional<Chat> createLocalChat(int[] param) {
        Object[] params = {param[0], param[1], param[2]};
        jdbcTemplate.update(sqlCreateLocalChat, params);
        return findLocalChat(param[2]);
    }
}
