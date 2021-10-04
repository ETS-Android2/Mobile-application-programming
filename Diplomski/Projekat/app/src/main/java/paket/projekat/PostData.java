package paket.projekat;

public class PostData {
    String userId, postId, postTitle, postDescription, postImageUrl, postTime, postLikes, postComments;

    public PostData() {}

    public PostData(String userId, String postId, String postTitle, String postDescription, String postImageUrl, String postTime, String postLikes, String postComments) {
        this.userId = userId;
//        this.firstName = firstName;
//        this.secondName = secondName;
//        this.userProfileImageUrl = userProfileImageUrl;
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.postImageUrl = postImageUrl;
        this.postTime = postTime;
        this.postLikes = postLikes;
        this.postComments = postComments;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostLikes() {
        return postLikes;
    }

    public void setPostLikes(String postLikes) {
        this.postLikes = postLikes;
    }

    public String getPostComments() {
        return postComments;
    }

    public void setPostComments(String postComments) {
        this.postComments = postComments;
    }
}
