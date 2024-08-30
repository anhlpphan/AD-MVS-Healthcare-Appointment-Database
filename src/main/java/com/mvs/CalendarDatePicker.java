package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * CalendarDatePicker is a JDialog that displays a calendar for the user to pick a date.
 * The selected date can be retrieved using the getSelectedDate() method.
 */
public class CalendarDatePicker extends JDialog {
  private LocalDate selectedDate;
  private LocalDate currentDate;
  private JLabel monthLabel;
  private JPanel calendarPanel;

  /**
   * Constructs a CalendarDatePicker with the specified owner frame.
   * 
   * @param owner the owner frame
   */
  public CalendarDatePicker(Frame owner) {
    super(owner, "Select Date", true);
    setSize(400, 400);
    setLocationRelativeTo(owner);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    currentDate = LocalDate.now();
    initializeUI();
    updateCalendar();
  }

  /**
   * Initializes the user interface components.
   */
  private void initializeUI() {
    setLayout(new BorderLayout());

    // Month and navigation panel
    JPanel headerPanel = new JPanel(new BorderLayout());
    monthLabel = new JLabel("", JLabel.CENTER);
    JButton prevButton = new JButton("<");
    JButton nextButton = new JButton(">");

    // Action listener for previous month button
    prevButton.addActionListener(e -> {
      currentDate = currentDate.minusMonths(1);
      updateCalendar();
    });

    // Action listener for next month button
    nextButton.addActionListener(e -> {
      currentDate = currentDate.plusMonths(1);
      updateCalendar();
    });

    headerPanel.add(prevButton, BorderLayout.WEST);
    headerPanel.add(monthLabel, BorderLayout.CENTER);
    headerPanel.add(nextButton, BorderLayout.EAST);
    add(headerPanel, BorderLayout.NORTH);

    // Calendar grid panel
    calendarPanel = new JPanel(new GridLayout(0, 7)); // 7 for days of the week
    add(calendarPanel, BorderLayout.CENTER);
  }

  /**
   * Updates the calendar to display the current month.
   */
  private void updateCalendar() {
    calendarPanel.removeAll();

    LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
    int daysInMonth = firstOfMonth.lengthOfMonth();
    int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

    monthLabel.setText(firstOfMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

    // Adding day headers
    String[] dayNames = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
    for (String dayName : dayNames) {
      calendarPanel.add(new JLabel(dayName, JLabel.CENTER));
    }

    // Adjust dayOfWeek to suit the calendar starting with Sunday
    int shift = (dayOfWeek == 7) ? 0 : dayOfWeek;

    // Adding empty labels for empty days at the start
    for (int i = 0; i < shift; i++) {
      calendarPanel.add(new JLabel(""));
    }

    // Adding day buttons
    for (int i = 1; i <= daysInMonth; i++) {
      JButton dayButton = new JButton(String.valueOf(i));
      final int day = i;
      dayButton.addActionListener(e -> {
        selectedDate = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), day);
        setVisible(false);
      });
      calendarPanel.add(dayButton);
    }

    calendarPanel.revalidate();
    calendarPanel.repaint();
  }

  /**
   * Returns the selected date.
   * 
   * @return the selected date
   */
  public LocalDate getSelectedDate() {
    return selectedDate;
  }
}
