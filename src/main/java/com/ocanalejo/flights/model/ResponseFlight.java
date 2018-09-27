package com.ocanalejo.flights.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * This class acts as DTO, encapsulating final data that will be returned to our API consumers
 * 
 * @author ocanalejo
 *
 */
public class ResponseFlight {

  @Getter
  private final int stops;
  @Getter
  private final List<Leg> legs = new ArrayList<>();

  /*
   * Constructor for direct flight
   */
  public ResponseFlight(Day day, LocalTime depTime, LocalTime arrTime, String airportFrom, String airportTo, int depYear, int depMonth) {
    stops = 0;
    getLegs().add(new Leg(day.getDay(), depTime.getHour(), depTime.getMinute(), arrTime.getHour(), 
        arrTime.getMinute(), airportFrom, airportTo, depYear, depMonth));
  }

  /*
   * Constructor for flight with an interconnection
   */
  public ResponseFlight(Leg depLeg, Leg connLeg) {
    stops = 1;
    getLegs().add(depLeg);
    getLegs().add(connLeg);
  }


  /**
   * Legs represent each flight's round, with its departure and arrival info
   */
  public class Leg {

    @Getter
    private final String departureAirport;
    @Getter
    private final String arrivalAirport;
    private final LocalDateTime departureDateTime;
    private final LocalDateTime arrivalDateTime;

    public Leg(int day, int depHour, int depMinute, int arrHour, int arrMinute, String airportFrom, String airportTo, int depYear, int depMonth) {
      departureAirport = airportFrom;
      arrivalAirport = airportTo;
      departureDateTime = LocalDateTime.of(depYear, depMonth, day, depHour, depMinute);
      arrivalDateTime = LocalDateTime.of(depYear, depMonth, day, arrHour, arrMinute);
    }

    public String getDepartureDateTime() {
      return departureDateTime.toString();
    }

    public String getArrivalDateTime() {
      return arrivalDateTime.toString();
    }

    @JsonIgnore
    public LocalDateTime getDepartureLocalDateTime() {
      return departureDateTime;
    }

    @JsonIgnore
    public LocalDateTime getArrivalLocalDateTime() {
      return arrivalDateTime;
    }

    @JsonIgnore
    public LocalTime getDepartureLocalTime() {
      return LocalTime.of(departureDateTime.getHour(), departureDateTime.getMinute());
    }

    @JsonIgnore
    public LocalTime getArrivalLocalTime() {
      return LocalTime.of(arrivalDateTime.getHour(), arrivalDateTime.getMinute());
    }
  }

  /**
   * Returns a list of suitable interconnecting flights for a given initial flight, 
   * based on the final arrival date-time.
   * Any valid interconnecting flight must have a departure date-time at least 2 hours later over the initial's flight arrival date-time,
   * and its arrival must occur before the final arrival date-time
   * 
   * @param connectionFlights a list of possible interconnecting flights
   * @param arrDateTime the final date-time at which the trip should arrives 
   * @return a list of suitable interconnecting flights found, or an empty list in case no one is valid
   */
  public List<ResponseFlight> getValidConnectionFlights(List<ResponseFlight> connectionFlights, LocalDateTime arrDateTime) {
      List<ResponseFlight> responseFlights = new ArrayList<>();
      Leg depLeg = getLegs().get(0);
      for (ResponseFlight connFlight : connectionFlights) {
        Leg connLeg = connFlight.getLegs().get(0);
        if(connLeg.getDepartureLocalDateTime().isAfter(depLeg.getArrivalLocalDateTime().plusHours(2)) &&
            connLeg.getArrivalLocalDateTime().isBefore(arrDateTime.plusMinutes(1))) {
          responseFlights.add(new ResponseFlight(depLeg, connLeg));
        }
    }
    return responseFlights;
  }

}
