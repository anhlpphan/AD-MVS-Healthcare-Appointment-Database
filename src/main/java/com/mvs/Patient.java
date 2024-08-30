package com.mvs;

public class Patient {
  private int userId;
  private double height;
  private double weight;

  public Patient(int userId, double height, double weight) {
    this.userId = userId;
    this.height = height;
    this.weight = weight;
  }

  // Getters
  public int getUserId() {
    return userId;
  }

  public double getHeight() {
    return height;
  }

  public double getWeight() {
    return weight;
  }
}
