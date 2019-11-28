package com.netcraker.repositories.impl;

import com.netcraker.model.PublishingHouse;
import com.netcraker.model.mapper.GenreRowMapper;
import com.netcraker.model.mapper.PublishingHouseRowMapper;
import com.netcraker.repositories.PublishingHouseRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@PropertySource("classpath:sqlQueries.properties")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublishingHouseRepositoryImpl implements PublishingHouseRepository {

    @Value("${publishingHouses.getAll}")
    private String sqlGetAll;
    private static final Logger logger = LoggerFactory.getLogger(PublishingHouseRepositoryImpl.class);
    private final @NonNull JdbcTemplate jdbcTemplate;

    @Override
    public List<PublishingHouse> getPublishingHouses() {
        return jdbcTemplate.query(sqlGetAll, new PublishingHouseRowMapper());
    }

    @Override
    public Optional<PublishingHouse> getById(int id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<PublishingHouse> insert(PublishingHouse entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<PublishingHouse> update(PublishingHouse entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException();
    }
}
