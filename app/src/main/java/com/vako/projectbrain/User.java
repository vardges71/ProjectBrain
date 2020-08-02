package com.vako.projectbrain;

public class User {

    private String userID;
    private String userName;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String location;

    private String ideaID;
    private String ideaTitle;
    private String ideaContext;
    private String ideaContent;
    private String createdDate;

    private String ideaCount;

    public User(String userID,
                String userName,
                String userEmail,
                String firstName,
                String lastName,
                String location,
                String ideaTitle,
                String ideaContext,
                String ideaContent,
                String createdDate) {

        this.userID = userID;
        this.userName = userName;
        this.userEmail = userEmail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.ideaTitle = ideaTitle;
        this.ideaContext = ideaContext;
        this.ideaContent = ideaContent;
        this.createdDate = createdDate;
    }

    public User(String email) {

        this.userEmail = email;
    }

    public User(String userName, String firstName, String lastName, String location) {

        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }

    public User(String userID, String userName, String firstName, String lastName, String location, String ideaID, String ideaTitle, String ideaContext, String ideaCount) {

        this.userID = userID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.ideaID = ideaID;
        this.ideaTitle = ideaTitle;
        this.ideaContext = ideaContext;
        this.ideaCount = ideaCount;
    }

    public User(String ideaTitle, String ideaContext, String modifiedDate) {

        this.ideaTitle = ideaTitle;
        this.ideaContext = ideaContext;
        this.createdDate = modifiedDate;
    }


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

    public String getIdeaTitle() { return ideaTitle; }
    public void setIdeaTitle(String ideaTitle) { this.ideaTitle = ideaTitle; }

    public String getIdeaContext() { return ideaContext; }
    public void setIdeaContext(String ideaContext) { this.ideaContext = ideaContext; }

    public String getIdeaContent() { return ideaContent; }
    public void setIdeaContent(String ideaContent) { this.ideaContent = ideaContent; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getIdeaCount() { return ideaCount; }
    public void setIdeaCount(String ideaCount) { this.ideaCount = ideaCount; }

    public String getIdeaID() { return ideaID; }
    public void setIdeaID(String ideaID) { this.ideaID = ideaID; }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                ", ideaID='" + ideaID + '\'' +
                ", ideaTitle='" + ideaTitle + '\'' +
                ", ideaContext='" + ideaContext + '\'' +
                ", ideaContent='" + ideaContent + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", ideaCount='" + ideaCount + '\'' +
                '}';
    }
}
