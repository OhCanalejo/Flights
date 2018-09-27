package com.ocanalejo.flights.model;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

public class Schedule {

  @Getter
  @Setter
  private int month;
  @Getter
  @Setter
  private List<Day> days;

  public List<Day> filterDays(int dayFrom, int dayTo) {
    return getDays().stream().filter(d -> d.getDay() >= dayFrom && d.getDay() <= dayTo).collect(Collectors.toList());
  }
}
