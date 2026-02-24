package com.annexes;

public class VoteTypeCount {
    private VoteType voteType;
    private int count;

    public VoteTypeCount(VoteType voteType, int count) {
        this.voteType = voteType;
        this.count = count;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "VoteTypeCount(voteType=" + voteType + ", count=" + count + ")";
    }
}
