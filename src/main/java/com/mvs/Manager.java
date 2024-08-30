package com.mvs;

import java.util.Date;

public class Manager {
  private int userId; // Unique identifier for the manager
  private Date startDate; // The start date of the manager's employment
  private Date endDate; // The end date of the manager's employment, if applicable
  private String officeNumber; // Office number of the manager
  private int departmentSupervisedId; // ID of the department supervised by the manager
  private double salary; // Salary of the manager

  /**
   * Constructor to initialize a Manager object with the specified details.
   * 
   * @param userId The unique identifier for the manager.
   * @param startDate The start date of the manager's employment.
   * @param endDate The end date of the manager's employment.
   * @param officeNumber The office number of the manager.
   * @param departmentSupervisedId The ID of the department supervised by the manager.
   * @param salary The salary of the manager.
   */
  public Manager(int userId, Date startDate, Date endDate, String officeNumber, int departmentSupervisedId, double salary) {
    this.userId = userId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.officeNumber = officeNumber;
    this.departmentSupervisedId = departmentSupervisedId;
    this.salary = salary;
  }

  // Getters and setters for accessing and modifying the private fields

  /**
   * Gets the user ID of the manager.
   * 
   * @return The user ID.
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Gets the start date of the manager's employment.
   * 
   * @return The start date.
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * Gets the end date of the manager's employment.
   * 
   * @return The end date.
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * Gets the office number of the manager.
   * 
   * @return The office number.
   */
  public String getOfficeNumber() {
    return officeNumber;
  }

  /**
   * Gets the ID of the department supervised by the manager.
   * 
   * @return The department ID.
   */
  public int getDepartmentSupervisedId() {
    return departmentSupervisedId;
  }

  /**
   * Gets the salary of the manager.
   * 
   * @return The salary.
   */
  public double getSalary() {
    return salary;
  }
}
