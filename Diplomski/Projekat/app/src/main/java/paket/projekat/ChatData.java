package paket.projekat;

public class ChatData {
    String message, receiver, sender, dateAndTime, messageID, dateAndTimeUpdate, type;
    boolean isSeen;

    public ChatData() {}

    public ChatData(String message, String receiver, String sender, String dateAndTime, String messageID, String dateAndTimeUpdate, String type, boolean isSeen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.dateAndTime = dateAndTime;
        this.messageID = messageID;
        this.dateAndTimeUpdate = dateAndTimeUpdate;
        this.type = type;
        this.isSeen = isSeen;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setDateAndTimeUpdate(String dateAndTimeUpdate) {
        this.dateAndTimeUpdate = dateAndTimeUpdate;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getDateAndTimeUpdate() {
        return dateAndTimeUpdate;
    }

    public String getType() {
        return type;
    }

    public boolean isSeen() {
        return isSeen;
    }
}
