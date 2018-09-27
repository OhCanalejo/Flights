package com.ocanalejo.flights.service;

import java.time.LocalDateTime;
import java.util.List;

import com.ocanalejo.flights.model.ResponseFlight;

public interface FlightsService {

  /**
   * Returns a list of availabe flights for the given departure and arrival airports and datetimes
   * Will return only flights departing from the departure airport <b>not eariler than the departure datetime</b>, 
   * and arriving to the arrival airport <b>not later than the arrival datetime</b>.
   * The returned list will contain:
   * - all available direct flights (example: DUB-WRO)
   * - all interconnected flights, with a maximum of one stop (example: DUB-STN-WRO)
   * 
   * @param departure the departure airport's code
   * @param arrival the arrival airport's code
   * @param departureDateTime the departure date and time, in ISO format
   * @param arrivalDateTime the arrival date and time, in ISO format
   * @return a list of available flights, or an empty list in case no one has been found
   */
  List<ResponseFlight> getFlights(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime);

}
