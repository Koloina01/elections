package com.annexes;

import java.util.Objects;

public class Vote {
    private int id;
    private Candidat candidate;
    private Voter voter;
    private VoteType voteType;

    public Vote() {
    }

    public Vote(int id, Candidat candidate, Voter voter, VoteType voteType) {
        this.id = id;
        this.candidate = candidate;
        this.voter = voter;
        this.voteType = voteType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Candidat getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidat candidate) {
        this.candidate = candidate;
    }

    public Voter getVoter() {
        return voter;
    }

    public void setVoter(Voter voter) {
        this.voter = voter;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    
    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", candidate=" + candidate +
                ", voter=" + voter +
                ", voteType=" + voteType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote)) return false;
        Vote vote = (Vote) o;
        return id == vote.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
