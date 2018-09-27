package com.ocanalejo.flights.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocanalejo.flights.model.ResponseFlight;
import com.ocanalejo.flights.model.Route;
import com.ocanalejo.flights.model.Schedule;

/**
 * Main FlightService implemantation
 * Encapsulates all the application logic for flights retrieval
 * 
 * @author ocanalejo
 *
 */
@Service
public class FlightsServiceImpl implements FlightsService {
  
  @Autowired
  RetrieverService retrieverService;

  /* (non-Javadoc)
   * @see com.ocanalejo.flights.service.FlightsService#getFlights(java.lang.String, java.lang.String, java.time.LocalDateTime, java.time.LocalDateTime)
   */
  @Override
  public List<ResponseFlight> getFlights(String depAirport, String arrAirport, LocalDateTime depDateTime, LocalDateTime arrDateTime) {

    List<ResponseFlight> responseFlights = Collections.synchronizedList(new ArrayList<>());
    List<Route> routes = new ArrayList<>();
    int depMonth = depDateTime.getMonthValue();
    int depYear = depDateTime.getYear();
    int depDay = depDateTime.getDayOfMonth();
    int arrDay = arrDateTime.getDayOfMonth();
    /*
     * Get Routes
     * get direct route (if any) and all other routes with a valid interconnection
     */
    routes.addAll(retrieverService.getRoutesByDeparture(depAirport).stream()
        .filter(r -> r.getAirportTo().equalsIgnoreCase(arrAirport) ||
            !retrieverService.getDirectRoutes(r.getAirportTo(), arrAirport).isEmpty()).collect(Collectors.toList()));
    /*
     * Get Flights
     * we will use a multithreading approach to enhance the task performance
     */
    ExecutorService threadPool = Executors.newCachedThreadPool();
    for(final Route route : routes) {
      Runnable fligthsByRouteRunnable = () -> {
        if(route.getAirportTo().equalsIgnoreCase(arrAirport)) {
          responseFlights.addAll(getDirectFlights(route.getAirportFrom(), route.getAirportTo(), depMonth, depYear, depDay, arrDay));
        } else {
          responseFlights.addAll(getFlightsWithConnection(route.getAirportFrom(), route.getAirportTo(), arrAirport, depMonth, depYear, depDay, arrDay, depDateTime, arrDateTime));
        }
      };
      threadPool.execute(fligthsByRouteRunnable);
    }
    awaitTerminationAfterShutdown(threadPool);
    
    return responseFlights.stream().filter(f -> f.getLegs().get(0).getDepartureLocalDateTime().isAfter(depDateTime.minusMinutes(1)) &&
        f.getLegs().get(0).getArrivalLocalDateTime().isBefore(arrDateTime.plusMinutes(1))).collect(Collectors.toList());
  }

  private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
    threadPool.shutdown();
    try {
        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
            threadPool.shutdownNow();
        }
    } catch (InterruptedException ex) {
        threadPool.shutdownNow();
        Thread.currentThread().interrupt();
    }
}
  /**
   * Return a list of direct flights found
   */
  private List<ResponseFlight> getDirectFlights(String airportFrom, String airportTo, int depMonth, int depYear, int depDay, int arrDay) {
    
    List<ResponseFlight> responseFlights = new ArrayList<>();
    Schedule schedule = retrieverService.getSchedule(airportFrom, airportTo, depYear, depMonth);
    
    if (schedule != null) {
      schedule.filterDays(depDay, arrDay)
        .forEach(d -> d.getFlights()
            .forEach(f -> responseFlights.add(new ResponseFlight(d, f.getDepartureTime(), f.getArrivalTime(), airportFrom, airportTo, depYear, depMonth))));
    }
    return responseFlights;
  }
  
  /**
   * Return a list of interconnected flights found
   */
  private List<ResponseFlight> getFlightsWithConnection(String airportFrom, String airportTo, String arrAirport, int depMonth, int depYear, int depDay, 
      int arrDay, LocalDateTime depDateTime, LocalDateTime arrDateTime) {
    
    List<ResponseFlight> responseFlights = new ArrayList<>();
    List<ResponseFlight> departureFlights = getDirectFlights(airportFrom, airportTo, depMonth, depYear, depDay, arrDay);
    List<ResponseFlight> connectionFlights   = getDirectFlights(airportTo, arrAirport, depMonth, depYear, depDay, arrDay);

    // before analyze it, we can get a more lightweight departureFlights list 
    // by removing every flight that departures before the given departure datetime
    departureFlights.removeIf(df -> df.getLegs().get(0).getDepartureLocalDateTime().isBefore(depDateTime));
    departureFlights.forEach(df -> responseFlights.addAll(df.getValidConnectionFlights(connectionFlights, arrDateTime)));
    
    return responseFlights;
  }
 
}
