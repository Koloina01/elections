package com.annexes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<VoteTypeCount> countVotesByType() {

        List<VoteTypeCount> results = new ArrayList<>();

        String sql = """
                SELECT vote_type, COUNT(id) AS nombre_votes
                FROM vote
                GROUP BY vote_type
                ORDER BY CASE vote_type
                    WHEN 'VALID' THEN 1
                    WHEN 'BLANK' THEN 2
                    WHEN 'NULL' THEN 3
                END
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                VoteType voteType = VoteType.valueOf(rs.getString("vote_type"));
                int count = rs.getInt("nombre_votes");

                results.add(new VoteTypeCount(voteType, count));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting votes by type", e);
        }

        return results;
    }
}
