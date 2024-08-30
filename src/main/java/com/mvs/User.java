package com.mvs;

import java.sql.Date;
import java.sql.Timestamp;

public class User {
  // Fields to store user information
  private int userId;
  private String username;
  private String password;
  private String firstName;
  private String middleName;
  private String lastName;
  private Date dateOfBirth;
  private String sex;
  private String phoneNo;
  private String street;
  private String city;
  private String state;
  private String zipCode;
  private Timestamp dateRegistered;
  private int userTypeID;
  private String email;
  private String ssn;

  // Constructor to initialize all fields
  public User(int userId, String username, String password, String firstName, String middleName, String lastName,
      Date dateOfBirth, String sex, String phoneNo, String street, String city, String state, String zipCode,
      Timestamp dateRegistered, int userTypeID, String email, String ssn) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
    this.sex = sex;
    this.phoneNo = phoneNo;
    this.street = street;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.dateRegistered = dateRegistered;
    this.userTypeID = userTypeID;
    this.email = email;
    this.ssn = ssn;
  }

  // Getter for userId
  public int getUserId() {
    return userId;
  }

  // Getter for username
  public String getUsername() {
    return username;
  }

  // Getter for password
  public String getPassword() {
    return password;
  }

  // Getter for firstName
  public String getFirstName() {
    return firstName;
  }

  // Getter for middleName
  public String getMiddleName() {
    return middleName;
  }

  // Getter for lastName
  public String getLastName() {
    return lastName;
  }

  // Getter for dateOfBirth
  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  // Getter for sex
  public String getSex() {
    return sex;
  }

  // Getter for phoneNo
  public String getPhoneNo() {
    return phoneNo;
  }

  // Getter for street
  public String getStreet() {
    return street;
  }

  // Getter for city
  public String getCity() {
    return city;
  }

  // Getter for state
  public String getState() {
    return state;
  }

  // Getter for zipCode
  public String getZipCode() {
    return zipCode;
  }

  // Getter for dateRegistered
  public Timestamp getDateRegistered() {
    return dateRegistered;
  }

  // Getter for userTypeID
  public int getUserTypeID() {
    return userTypeID;
  }

  // Getter for email
  public String getEmail() {
    return email;
  }

  // Getter for ssn
  public String getSSN() {
    return ssn;
  }
}
