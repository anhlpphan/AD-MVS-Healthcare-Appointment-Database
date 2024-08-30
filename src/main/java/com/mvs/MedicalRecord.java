package com.mvs;

import java.util.Date;

// MedicalRecord.java
public class MedicalRecord {
  // Fields representing the medical record data
  private int recordID;  // Unique identifier for the medical record
  private int userID;    // ID of the user/patient the record belongs to
  private String description;  // Description of the medical record
  private Date date;  // Date when the medical record was created or updated

  // Default constructor
  public MedicalRecord() {
  }

  // Parameterized constructor to initialize all fields
  public MedicalRecord(int recordID, int userID, String description, Date date) {
    this.recordID = recordID;
    this.userID = userID;
    this.description = description;
    this.date = date;
  }

  // Getter method for recordID
  public int getRecordID() {
    return recordID;
  }

  // Setter method for recordID
  public void setRecordID(int recordID) {
    this.recordID = recordID;
  }

  // Getter method for userID
  public int getUserID() {
    return userID;
  }

  // Setter method for userID
  public void setUserID(int userID) {
    this.userID = userID;
  }

  // Getter method for description
  public String getDescription() {
    return description;
  }

  // Setter method for description
  public void setDescription(String description) {
    this.description = description;
  }

  // Getter method for date
  public Date getDate() {
    return date;
  }

  // Setter method for date
  public void setDate(Date date) {
    this.date = date;
  }
}
