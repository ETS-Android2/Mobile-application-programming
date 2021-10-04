package paket.projekat;

public class CommentData {
    String comment;
    String commentId;
    String commentTime;
    String userId;

    public CommentData() {}

    public CommentData(String comment, String commentId, String commentTime, String userId) {
        this.comment = comment;
        this.commentId = commentId;
        this.commentTime = commentTime;
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
