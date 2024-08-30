package com.mvs;

public class Allergy {
  private int allergyId;
  private int userId;
  private String description;

  public Allergy(int allergyId, int userId, String description) {
    this.allergyId = allergyId;
    this.userId = userId;
    this.description = description;
  }

  // Getters and Setters
  public int getAllergyId() {
    return allergyId;
  }

  public int getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }

  public void setAllergyId(int allergyId) {
    this.allergyId = allergyId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
