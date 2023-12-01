package com.techelevator.model;

public class Vote {
    private int userId;
    private int requestId;
    private boolean approved;

    public Vote() {
    }

    public Vote(int userId, int requestId, boolean approved) {
        this.userId = userId;
        this.requestId = requestId;
        this.approved = approved;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
