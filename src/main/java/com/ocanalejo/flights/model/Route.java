package com.ocanalejo.flights.model;

import lombok.Getter;
import lombok.Setter;

public class Route {

  @Getter
  @Setter
  private String airportFrom;
  @Getter
  @Setter
  private String airportTo;
  @Getter
  @Setter
  private String connectingAirport;
  @Getter
  @Setter
  private boolean newRoute;
  @Getter
  @Setter
  private boolean seasonalRoute;
  @Getter
  @Setter
  private String group;
  
}
