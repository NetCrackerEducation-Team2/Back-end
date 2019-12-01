package com.netcraker.repositories.impl;

import com.netcraker.model.FriendInvitation;
import com.netcraker.model.mapper.FriendInvitationRowMapper;
import com.netcraker.repositories.FriendInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor
public class FriendInvitationRepositoryImpl implements FriendInvitationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final FriendInvitationRowMapper rowMapper = FriendInvitationRowMapper.builder().build();
    @Value("${friendInvitation.getById}")
    private String sqlGetById;
    @Value("${friendInvitation.insert}")
    private String sqlInsert;
    @Value("${friendInvitation.update}")
    private String sqlUpdate;
    @Value("${friendInvitation.delete}")
    private String sqlDelete;

    @Override
    public Optional<FriendInvitation> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlGetById, rowMapper, id));
        } catch (IncorrectResultSizeDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<FriendInvitation> insert(FriendInvitation invitation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(conn -> {
                PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps = setPreparedStatementParams(ps, invitation, 1);
                return ps;
            }, keyHolder);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
        return getById((Integer) keyHolder.getKeys().get("invitation_id"));
    }

    @Override
    public Optional<FriendInvitation> update(FriendInvitation entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    /**
     * Sets all fields of invitation(exclude id) as parameters of prepared statement.
     *
     * @param ps               prepared statement to be initialized
     * @param invitation       data source
     * @param paramsStartIndex first index that will be used for prepared statement param
     */
    private PreparedStatement setPreparedStatementParams(PreparedStatement ps, FriendInvitation invitation, int paramsStartIndex) throws SQLException {
        ps.setInt(paramsStartIndex++, invitation.getInvitationSource());
        ps.setInt(paramsStartIndex++, invitation.getInvitationTarget());
        ps.setBoolean(paramsStartIndex++, invitation.getAccepted());
        ps.setTimestamp(paramsStartIndex++, invitation.getCreationTime());
        return ps;
    }
}
