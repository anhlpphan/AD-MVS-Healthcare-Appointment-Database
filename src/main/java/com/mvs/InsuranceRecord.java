package com.mvs;

import java.util.Date;

/**
 * InsuranceRecord represents an insurance record associated with a user.
 * It contains details such as the insurance card number, insurance company name, insurance company phone number,
 * primary doctor's user ID, and the expiration date of the insurance.
 */
public class InsuranceRecord {
  private String insuranceCardNo;
  private int userID;
  private String insuranceCompanyName;
  private String insuranceCompanyPhoneNo;
  private int primaryDoctorUserID;
  private Date expDate;

  /**
   * Constructs an InsuranceRecord with the specified details.
   * 
   * @param insuranceCardNo the insurance card number
   * @param userID the user ID associated with the insurance
   * @param insuranceCompanyName the name of the insurance company
   * @param insuranceCompanyPhoneNo the phone number of the insurance company
   * @param primaryDoctorUserID the user ID of the primary doctor
   * @param expDate the expiration date of the insurance
   */
  public InsuranceRecord(String insuranceCardNo, int userID, String insuranceCompanyName,
      String insuranceCompanyPhoneNo, int primaryDoctorUserID, Date expDate) {
    this.insuranceCardNo = insuranceCardNo;
    this.userID = userID;
    this.insuranceCompanyName = insuranceCompanyName;
    this.insuranceCompanyPhoneNo = insuranceCompanyPhoneNo;
    this.primaryDoctorUserID = primaryDoctorUserID;
    this.expDate = expDate;
  }

  // Getters and Setters

  /**
   * Gets the insurance card number.
   * 
   * @return the insurance card number
   */
  public String getInsuranceCardNo() {
    return insuranceCardNo;
  }

  /**
   * Sets the insurance card number.
   * 
   * @param insuranceCardNo the new insurance card number
   */
  public void setInsuranceCardNo(String insuranceCardNo) {
    this.insuranceCardNo = insuranceCardNo;
  }

  /**
   * Gets the user ID associated with the insurance.
   * 
   * @return the user ID
   */
  public int getUserID() {
    return userID;
  }

  /**
   * Sets the user ID associated with the insurance.
   * 
   * @param userID the new user ID
   */
  public void setUserID(int userID) {
    this.userID = userID;
  }

  /**
   * Gets the name of the insurance company.
   * 
   * @return the insurance company name
   */
  public String getInsuranceCompanyName() {
    return insuranceCompanyName;
  }

  /**
   * Sets the name of the insurance company.
   * 
   * @param insuranceCompanyName the new insurance company name
   */
  public void setInsuranceCompanyName(String insuranceCompanyName) {
    this.insuranceCompanyName = insuranceCompanyName;
  }

  /**
   * Gets the phone number of the insurance company.
   * 
   * @return the insurance company phone number
   */
  public String getInsuranceCompanyPhoneNo() {
    return insuranceCompanyPhoneNo;
  }

  /**
   * Sets the phone number of the insurance company.
   * 
   * @param insuranceCompanyPhoneNo the new insurance company phone number
   */
  public void setInsuranceCompanyPhoneNo(String insuranceCompanyPhoneNo) {
    this.insuranceCompanyPhoneNo = insuranceCompanyPhoneNo;
  }

  /**
   * Gets the user ID of the primary doctor.
   * 
   * @return the primary doctor user ID
   */
  public int getPrimaryDoctorUserID() {
    return primaryDoctorUserID;
  }

  /**
   * Sets the user ID of the primary doctor.
   * 
   * @param primaryDoctorUserID the new primary doctor user ID
   */
  public void setPrimaryDoctorUserID(int primaryDoctorUserID) {
    this.primaryDoctorUserID = primaryDoctorUserID;
  }

  /**
   * Gets the expiration date of the insurance.
   * 
   * @return the expiration date
   */
  public Date getExpDate() {
    return expDate;
  }

  /**
   * Sets the expiration date of the insurance.
   * 
   * @param expDate the new expiration date
   */
  public void setExpDate(Date expDate) {
    this.expDate = expDate;
  }
}
