package com.netcraker.model.mapper;

import com.netcraker.model.AuthorizationLinks;
import com.netcraker.model.PublishingHouse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PublishingHouseRowMapper implements RowMapper<PublishingHouse> {
    @Override
    public PublishingHouse mapRow(ResultSet resultSet, int i) throws SQLException {
        PublishingHouse publishingHouse = new PublishingHouse();
        publishingHouse.setPublishingHouseId(resultSet.getInt("publishing_house_id"));
        publishingHouse.setName(resultSet.getString("name"));
        return publishingHouse;
    }
}
