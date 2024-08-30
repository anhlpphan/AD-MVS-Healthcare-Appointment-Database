package com.mvs;

public class Medication {
  private int medicationId;
  private int userId;
  private String description;

  public Medication(int medicationId, int userId, String description) {
    this.medicationId = medicationId;
    this.userId = userId;
    this.description = description;
  }

  // Getters
  public int getMedicationId() {
    return medicationId;
  }

  public int getUserId() {
    return userId;
  }

  public String getDescription() {
    return description;
  }

  // Setters
  public void setMedicationId(int medicationId) {
    this.medicationId = medicationId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
