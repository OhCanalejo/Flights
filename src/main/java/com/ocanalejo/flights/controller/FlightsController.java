package com.ocanalejo.flights.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocanalejo.flights.model.ResponseFlight;
import com.ocanalejo.flights.service.FlightsService;


/**
 * RESTful Controller, ready to attend client calls for getting flights
 * @author ocanalejo
 *
 */
@RestController
@RequestMapping("/interconnections")
public class FlightsController {

  @Autowired
  FlightsService flightsService;

  /**
   * Returns a list of availabe flights for the given departure and arrival airports and datetimes
   * @param departure the departure airport's code
   * @param arrival the arrival airport's code
   * @param departureDateTime the departure date and time, in ISO format
   * @param arrivalDateTime the arrival date and time, in ISO format
   * @return a ResponseEntity containing a list of available flights, or an empty one in case nothing was found.
   */
  @GetMapping
  public ResponseEntity<List<ResponseFlight>> getFlights(@RequestParam("departure") String departure, @RequestParam("arrival") String arrival,
      @RequestParam("departureDateTime") String departureDateTime, @RequestParam("arrivalDateTime") String arrivalDateTime) {
    
    LocalDateTime depDateTime = LocalDateTime.parse(departureDateTime);
    LocalDateTime arrDateTime = LocalDateTime.parse(arrivalDateTime);
    List<ResponseFlight> flightsObtained = flightsService.getFlights(departure, arrival, depDateTime, arrDateTime);
    
    return new ResponseEntity<>(flightsObtained, HttpStatus.OK);
  }
  

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception ex) {
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
