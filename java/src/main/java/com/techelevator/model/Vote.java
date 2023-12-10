package com.techelevator.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Vote {
    @NotNull
    private User user;
    @Min(1)
    private int requestId;
    private boolean approved;

    public Vote() {
    }

    public Vote(User user, int requestId, boolean approved) {
        this.user = user;
        this.requestId = requestId;
        this.approved = approved;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return requestId == vote.requestId && approved == vote.approved && user.equals(vote.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, requestId, approved);
    }
}

