package com.ocanalejo.flights.model;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

public class Flight {

  @Getter
  @Setter
  private String number;
  @Setter
  private String departureTime;
  @Setter
  private String arrivalTime;

  public LocalTime getDepartureTime() {
    return LocalTime.parse(departureTime);
  }

  public LocalTime getArrivalTime() {
    return LocalTime.parse(arrivalTime);
  }
}
