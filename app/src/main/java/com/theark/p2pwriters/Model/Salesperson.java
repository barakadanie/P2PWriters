package com.theark.p2pwriters.Model;

// Your package declaration here

public class Salesperson {

    private String name;
    private String phoneNumber;
    private String emailAddress;
    private String imageUrl;  // Add this if you want to include an image for the salesperson

    // Default constructor required for Firestore
    public Salesperson() {
        // Default constructor required for calls to DataSnapshot.getValue(Salesperson.class)
    }

    // Constructor with parameters
    public Salesperson(String name, String phoneNumber, String emailAddress, String imageUrl) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.imageUrl = imageUrl;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter methods (if needed)

    // You can add additional methods or modify the structure based on your needs
}
