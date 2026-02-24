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

    public List<CandidateVoteCount> countValidVotesByCandidate() {

        List<CandidateVoteCount> results = new ArrayList<>();

        String sql = """
                    SELECT
                        c.name AS candidate_name,
                        COUNT(v.id) AS valid_vote
                    FROM candidate c
                    LEFT JOIN vote v
                        ON c.id = v.candidate_id
                        AND v.vote_type = 'VALID'
                    GROUP BY c.name
                    ORDER BY c.name
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("candidate_name");
                int count = rs.getInt("valid_vote");

                results.add(new CandidateVoteCount(name, count));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error counting valid votes by candidate", e);
        }

        return results;
    }

    public VoteSummary computeVoteSummary() {
        String sql = """
                    SELECT
                        SUM(CASE WHEN vote_type = 'VALID' THEN 1 ELSE 0 END) AS valid_count,
                        SUM(CASE WHEN vote_type = 'BLANK' THEN 1 ELSE 0 END) AS blank_count,
                        SUM(CASE WHEN vote_type = 'NULL' THEN 1 ELSE 0 END) AS null_count
                    FROM vote
                """;

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int validCount = rs.getInt("valid_count");
                int blankCount = rs.getInt("blank_count");
                int nullCount = rs.getInt("null_count");

                return new VoteSummary(validCount, blankCount, nullCount);
            } else {

                return new VoteSummary(0, 0, 0);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error computing vote summary", e);
        }
    }

    public double computeTurnoutRate() {
        String sql = """
            SELECT
                (SELECT COUNT(id) FROM vote) AS votes_count,
                (SELECT COUNT(id) FROM voter) AS total_voters
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int votesCount = rs.getInt("votes_count");
                int totalVoters = rs.getInt("total_voters");

                if (totalVoters == 0) {
                    return 0.0;
                }

                return (double) votesCount / totalVoters * 100;
            } else {
                return 0.0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error computing turnout rate", e);
        }
    }
}
