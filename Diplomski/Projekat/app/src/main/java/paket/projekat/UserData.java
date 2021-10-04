package paket.projekat;

public class UserData {
    private String userId;
    private String firstName;
    private String secondName;
    private String gender;
    private String dateOfBirth;
    private String userName;
    private String email;
    private String password;
    private String numbOfTelephone;
    private String imageURL;
    private String onlineStatus;
    private String typingTo;

    public UserData(String userId, String firstName, String secondName, String gender, String dateOfBirth, String userName, String email, String password, String numbOfTelephone, String imageURL, String onlineStatus, String typingTo) {
        this.userId = userId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.numbOfTelephone = numbOfTelephone;
        this.imageURL = imageURL;
        this.onlineStatus = onlineStatus;
        this.typingTo = typingTo;
    }

    public UserData() {}

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNumbOfTelephone(String numbOfTelephone) {
        this.numbOfTelephone = numbOfTelephone;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public void setTypingTo(String typingTo) {
        this.typingTo = typingTo;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNumbOfTelephone() {
        return numbOfTelephone;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public String getTypingTo() {
        return typingTo;
    }
}
