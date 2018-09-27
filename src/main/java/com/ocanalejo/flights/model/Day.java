package com.ocanalejo.flights.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Day {
  
  @Getter
  @Setter
  private int day;
  @Getter
  @Setter
  private List<Flight> flights;

}
