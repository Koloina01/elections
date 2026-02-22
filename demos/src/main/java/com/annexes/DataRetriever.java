package com.annexes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataRetriever {

    public long countAllVotes() {

        String sql = "SELECT COUNT(id) AS total_votes FROM vote";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getLong("total_votes");
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error while counting votes", e);
        }
    }
}
