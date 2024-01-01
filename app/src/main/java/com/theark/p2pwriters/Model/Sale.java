package com.theark.p2pwriters.Model;

import java.io.Serializable;

public class Sale implements Serializable {

    public String amount;
    private String bookCategory;
    private String bookName;
    private String date;
    private String sendTo, documentId;

    // Required default constructor for Firestore
    public Sale() {
    }

    public Sale(String bookCategory, String bookName, String date, String sendTo, String amount,String documentId) {
        this.bookCategory = bookCategory;
        this.bookName = bookName;
        this.date = date;
        this.sendTo = sendTo;
        this.amount=amount;
        this.documentId=documentId;
    }
    // Constructors, getters, and setters

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBook() {
        return bookName;
    }

    public void setBook(String bookName) {
        this.bookName = bookName;
    }

    public String getDateSent() {
        return date;
    }

    public void setDateSent(String date) {
        this.date = date;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

