package com.ocanalejo.flights.service;

import java.util.List;

import com.ocanalejo.flights.model.Route;
import com.ocanalejo.flights.model.Schedule;

public interface RetrieverService {
  
  /**
   * Will get a list of available routes, based on a departure and an arrival airports codes
   * (only routes with empty (null) 'connectingAirport' are used) 
   * @param departure the code of the departure airport
   * @param arrival the code of the arrival airport
   * @return a list of available routes for the given airport's codes. 
   * The list will be empty in case no routes retrieved 
   */
  List<Route> getDirectRoutes(String departure, String arrival);
  
  /**
   * Return a list of routes, filtered by <b>departure</b> airport's code
   * @param departure departure the code of the departure airport
   * @return a list of available routes for the given airport's code. 
   * The list will be empty in case no routes retrieved
   */
  List<Route> getRoutesByDeparture(String departure);

  /**
   * Return a list of routes, filtered by <b>arrival</b> airport's code
   * @param departure departure the code of the departure airport
   * @return a list of available routes for the given airport's code. 
   * The list will be empty in case no routes retrieved
   */
  List<Route> getRoutesByArrival(String arrival);

  /**
   * Return a list of available flights, based on a departure and an arrival airports codes,
   * a year and a month. Retrieved flights are grouped by day  
   * @param departure departure the code of the departure airport
   * @param arrival the code of the arrival airport
   * @param depYear the departure's year
   * @param depMonth the departure's month
   * @return a list of available flights. The list will be empty in case no flights retrieved
   */
  Schedule getSchedule(String departure, String arrival, int depYear, int depMonth);

}
