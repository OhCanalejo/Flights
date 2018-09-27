"Flights: A RESTful API for searching available flights" 

Developed on Java 8 and Spring 4, this PoC RESTful API provides a JSON list of available flights for given departure/arrival airports and date/times.
It's based on flights from Ryanair airlines, consuming its public microservices.

Deploy it on your favourite Servlet container and call it by using a URI like this:

https://<host>/<context>/interconnections?departure={departure-airport}&arrival={arrival-airport}&departureDateTime={departure-datetime}&arrivalDateTime={arrival-datetime}

example:
http://localhost:8080/interconnectingflights/interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-10-04T07%3A00&arrivalDateTime=2018-10-06T01%3A00

The Javadoc explains details, business rules, etc.
