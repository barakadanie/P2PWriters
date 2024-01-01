package com.theark.p2pwriters.Model;
// Imports and package declaration here

public class Category {

    private String categoryName;

    public Category() {
        // Required empty public constructor for Firestore
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {
        return categoryName != null ? categoryName : "";
    }
}

