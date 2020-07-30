package com.vako.projectbrain;

public class User {

    private String userID;
    private String userName;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String location;
    private Idea idea;

    public User(String userID, String userName, String userEmail, String firstName, String lastName, String location, Idea idea) {

        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.idea = idea;
    }


    public User(String email) {

        this.userEmail = email;
    }

    public User() {

    }

    public User(String userName, String firstName, String lastName, String location) {

        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }

//    public User(String userName, String firstName, String lastName, String location, String idea) {
//
//    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Idea getIdea() { return idea; }
    public void setIdea(Idea idea) { this.idea = idea; }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                ", idea=" + idea +
                '}';
    }
}
