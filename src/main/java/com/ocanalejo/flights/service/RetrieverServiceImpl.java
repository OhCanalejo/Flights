package com.ocanalejo.flights.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ocanalejo.flights.model.Route;
import com.ocanalejo.flights.model.Schedule;

/**
 * This service is our consumer of the external service APIs 
 * Retrieves info about routes and schedules
 * 
 * @author ocanalejo
 *
 */
@Service
public class RetrieverServiceImpl implements RetrieverService {

  @Value("${endpoint.routes}")
  private String routesEndpoint;
  @Value("${endpoint.schedules}")
  private String schedulesEndpoint;
  
  private RestTemplate restTemplate;
  private List<Route> routes = new ArrayList<>();

  /* (non-Javadoc)
   * @see com.ocanalejo.flights.service.RetrieverService#getDirectRoutes(java.lang.String, java.lang.String)
   */
  @Override
  public List<Route> getDirectRoutes(String departure, String arrival) {
    return getRoutes().stream().filter(r -> r.getAirportFrom().equalsIgnoreCase(departure) 
            && r.getAirportTo().equalsIgnoreCase(arrival)).collect(Collectors.toList());
  }

  /* (non-Javadoc)
   * @see com.ocanalejo.flights.service.RetrieverService#getRoutesByDeparture(java.lang.String)
   */
  @Override
  public List<Route> getRoutesByDeparture(String departure) {
    return getRoutes().stream().filter(r -> r.getAirportFrom().equalsIgnoreCase(departure)).collect(Collectors.toList());
  }

  /* (non-Javadoc)
   * @see com.ocanalejo.flights.service.RetrieverService#getRoutesByArrival(java.lang.String)
   */
  @Override
  public List<Route> getRoutesByArrival(String arrival) {
    return getRoutes().stream().filter(r -> r.getAirportTo().equalsIgnoreCase(arrival)).collect(Collectors.toList());
  }

  /* (non-Javadoc)
   * @see com.ocanalejo.flights.service.RetrieverService#getSchedule(java.lang.String, java.lang.String, int, int)
   */
  @Override
  public Schedule getSchedule(String departure, String arrival, int depYear, int depMonth) {
    String schedEnpoint = schedulesEndpoint + "/" + departure + "/" + arrival + "/years/" + depYear + "/months/" + depMonth;
    try {
      return getRestTemplate().getForObject(schedEnpoint, Schedule.class);
    } catch (HttpClientErrorException ex) {
      return null;
    }
  }

  /**
   * Retrieves all available routes and caches them (assuming routes don't change, at least not in this scenario)
   */
  private List<Route> getRoutes() {
    if (routes.isEmpty()) {
      routes.addAll(Arrays.stream(getRestTemplate().getForObject(routesEndpoint, Route[].class))
          .filter(r -> r.getConnectingAirport() == null).collect(Collectors.toList()));
    }
    return routes;
  }
  
  /**
   * Sets Up the RestTemplate for external APIs calls
   */
  private RestTemplate getRestTemplate() {
    if (restTemplate == null) {
      restTemplate = new RestTemplate();
      MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
      mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON_UTF8));
      restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
    }
    return restTemplate;
  }

}
