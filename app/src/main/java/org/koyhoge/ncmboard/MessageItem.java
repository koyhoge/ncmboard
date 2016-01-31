package org.koyhoge.ncmboard;

import java.util.Date;

public class MessageItem {
    private String message_;
    private String userName_;
    private String userId_;
    private Date timestamp_;

    public void setMessage(String message) {
        message_ = message;
    }

    public String getMessage() {
        return message_;
    }

    public void setUserName(String userName) {
        userName_ = userName;
    }

    public String getUserName() {
        return userName_;
    }

    public void setUserId(String userId) {
        userId_ = userId;
    }

    public String getUserId() {
        return userId_;
    }

    public void setTimestamp(Date timestamp) {
        timestamp_ = timestamp;
    }

    public Date getTimestamp() {
        return timestamp_;
    }
}
