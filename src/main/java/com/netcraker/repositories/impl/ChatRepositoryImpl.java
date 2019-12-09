package com.netcraker.repositories.impl;

import com.netcraker.model.Chat;
import com.netcraker.model.Message;
import com.netcraker.model.mapper.ChatRowMapper;
import com.netcraker.model.mapper.GroupChatReferenceRowMapper;
import com.netcraker.model.mapper.GroupChatRowMapper;
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

    @Value("${chat.findMessageByChatId}")
    private String sqlFindMessageByChatId;

    @Value("${chat.insert}")
    private String sqlCreateChat;

    @Value("${chat.insert.groupChat}")
    private String sqlCreateGroupChat;

    @Value("${chat.insert.reference}")
    private String sqlCreateGroupChatReference;

    @Value("${chat.createLocalChat}")
    private String sqlCreateLocalChat;

    @Value("${chat.findByChatId}")
    private String sqlFindByChatId;

    @Value("${chat.findByGroupChatId}")
    private String sqlFindByGroupChatId;

    @Value("${chat.findByChatName}")
    private String sqlFindByChatName;

    @Value("${chat.findReferenceById}")
    private String sqlFindReferenceById;

    @Value("${chat.findAllChats}")
    private String sqlFindAllReferencesById;

    @Value("${chat.findByUsersId}")
    private String sqlFindByUsersId;

    @Value("${chat.sendMessage}")
    private String sqlCreateMessage;

    @Value("${chat.findMessageById}")
    private String sqlFindByMessageId;

    @Override
    public List<Message> listMessage(int chat_id) {
        Object[] params = { chat_id };
        return jdbcTemplate.query(sqlFindMessageByChatId, params, new MessageRowMapper());
    }

    @Override
    public Optional<Chat> findGroupChat(String chatName) {
        Object[] params = {chatName};
        List<Chat> chats = jdbcTemplate.query(sqlFindByChatName, params, new GroupChatRowMapper());
        return chats.isEmpty() ? Optional.empty() : Optional.of(chats.get(0));
    }
    @Override
    public Optional<Chat> findGroupChatById(int groupChatId) {
        Object[] params = {groupChatId};
        List<Chat> chats = jdbcTemplate.query(sqlFindByGroupChatId, params, new GroupChatRowMapper());
        return chats.isEmpty() ? Optional.empty() : Optional.of(chats.get(0));
    }

    @Override
    public Optional<Chat> createGroupChatReference(int userId, int chatId) {
        Object[] params = {userId, chatId};
        int groupChatUsersId = jdbcTemplate.queryForObject(sqlCreateGroupChatReference, params, Integer.class);
        return findReferenceGroupChat(groupChatUsersId);
    }

    @Override
    public Optional<Chat>findReferenceGroupChat(int groupChatUsersId) {
        Object[] params = {groupChatUsersId};
        List<Chat> chats = jdbcTemplate.query(sqlFindReferenceById, params, new GroupChatReferenceRowMapper());
        return chats.isEmpty() ? Optional.empty() : Optional.of(chats.get(0));
    }

    @Override
    public List<Chat> getGroupChatsReference(int userCurrentId) {
        Object[] params = {userCurrentId};
        return jdbcTemplate.query(sqlFindAllReferencesById, params, new GroupChatReferenceRowMapper());
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

    @Override
    public Optional<Chat> createGroupChat(String chatName, int chatId) {
        Object[] params = {chatName, chatId};
        jdbcTemplate.update(sqlCreateGroupChat, params);
        return findGroupChat(chatName);
    }

    @Override
    public Optional<Message> findMessage(int message_id) {
        Object[] params = { message_id };
        List<Message> messages = jdbcTemplate.query(sqlFindByMessageId, params, new MessageRowMapper());
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.get(0));
    }

    @Override
    public Optional<Message> sendMessage(int [] param, String content) {
        Object[] params = {param[0], param[1], content};
        int messageId = jdbcTemplate.queryForObject(sqlCreateMessage, params, Integer.class);
        return findMessage(messageId);
    }
}
