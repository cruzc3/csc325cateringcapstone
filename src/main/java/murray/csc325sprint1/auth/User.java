// src/main/java/murray/csc325sprint1/auth/User.java
package murray.csc325sprint1.auth;

public class User {
    private String uid;
    private String email;
    private String username;
    private String fullName;
    private String userType;

    // Constructors, getters, and setters
    public User() {}

    public User(String uid, String email, String username, String fullName, String userType) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.userType = userType;
    }

    // Getters and setters for all fields
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}