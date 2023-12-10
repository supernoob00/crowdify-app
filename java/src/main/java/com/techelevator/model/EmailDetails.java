package com.techelevator.model;

public class EmailDetails {
    private final String recipient;
    private final String msgBody;
    private final String subject;

    public EmailDetails(String recipient, String msgBody, String subject) {
        this.recipient = recipient;
        this.msgBody = msgBody;
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getSubject() {
        return subject;
    }
}
