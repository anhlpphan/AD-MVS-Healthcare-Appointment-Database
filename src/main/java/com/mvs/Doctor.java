package com.mvs;

/**
 * Doctor is a class representing a doctor in the medical system.
 * It contains information about the doctor's user ID, license number,
 * specialization, years of experience, salary, and department number.
 */
public class Doctor {
  private int userId;
  private String licenseNo;
  private String specialization;
  private int yearsOfExperience;
  private double salary;
  private int departmentNo;  // New field for department number

  /**
   * Constructs a Doctor with the specified details.
   * 
   * @param userId the user ID of the doctor
   * @param licenseNo the license number of the doctor
   * @param specialization the specialization of the doctor
   * @param yearsOfExperience the years of experience of the doctor
   * @param salary the salary of the doctor
   * @param departmentNo the department number the doctor belongs to
   */
  public Doctor(int userId, String licenseNo, String specialization, int yearsOfExperience, double salary, int departmentNo) {
    this.userId = userId;
    this.licenseNo = licenseNo;
    this.specialization = specialization;
    this.yearsOfExperience = yearsOfExperience;
    this.salary = salary;
    this.departmentNo = departmentNo;  // Initialize the new field
  }

  // Getters
  /**
   * Returns the user ID of the doctor.
   * 
   * @return the user ID of the doctor
   */
  public int getUserId() {
    return userId;
  }

  /**
   * Returns the license number of the doctor.
   * 
   * @return the license number of the doctor
   */
  public String getLicenseNo() {
    return licenseNo;
  }

  /**
   * Returns the specialization of the doctor.
   * 
   * @return the specialization of the doctor
   */
  public String getSpecialization() {
    return specialization;
  }

  /**
   * Returns the years of experience of the doctor.
   * 
   * @return the years of experience of the doctor
   */
  public int getYearsOfExperience() {
    return yearsOfExperience;
  }

  /**
   * Returns the salary of the doctor.
   * 
   * @return the salary of the doctor
   */
  public double getSalary() {
    return salary;
  }

  /**
   * Returns the department number the doctor belongs to.
   * 
   * @return the department number the doctor belongs to
   */
  public int getDepartmentNo() {
    return departmentNo;
  }

  /**
   * Returns a string representation of the doctor, including all details.
   * 
   * @return a string representation of the doctor
   */
  @Override
  public String toString() {
    return "Doctor ID: " + userId +
        "\nLicense No: " + licenseNo +
        "\nSpecialization: " + specialization +
        "\nYears of Experience: " + yearsOfExperience +
        "\nSalary: $" + String.format("%.2f", salary) +
        "\nDepartment No: " + departmentNo;  // Include department number in toString output
  }
}
