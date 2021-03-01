# Flight-In
API that provides flights information between 2 destinations from the selected airlines.  

The API provides the average price of total flights and can be filtered by destination, dates and currency.

## Requirements
- Java11
- Maven
- Docker

## Setup
The [docker-compose](docker-compose.yaml) provides a mongodb & mongo-express container.
```bash
docker-compose up -d
mvn spring-boot:run
```
The server is running on local port 8084 and mongo-express on local port 8888.

## API 
The [swagger](swagger.yaml) file describes the API endpoints and operations.  
Also, a [postman collection](flightin.postman_collection.json) is provided with a sample request for each API endpoint.

### Sample Request/Response
The following snippet requests TAP and Ryanair flight information between Porto-Lisbon and Lisbon-Porto in EUR from 06/03/2021 to 07/03/2021.
```bash
curl --location --request GET 'http://localhost:8084/flights/avg?dest=LIS&dest=OPO&airline=FR&airline=TP&curr=EUR&dateFrom=06/03/2021&dateTo=07/03/2021' \
--header 'Accept: application/json'
{
    "LIS": {
        "currency": "EUR",
        "name": "Aeroporto de Lisboa",
        "from": "06/03/2021",
        "to": "07/03/2021",
        "price_average": 168.67,
        "bags_price": {
            "bag1_average": 80.00,
            "bag2_average": 267.00
        }
    },
    "OPO": {
        "currency": "EUR",
        "name": "Aeroporto Francisco Sá Carneiro",
        "from": "06/03/2021",
        "to": "07/03/2021",
        "price_average": 171.20,
        "bags_price": {
            "bag1_average": 71.73,
            "bag2_average": 191.67
        }
    }
}
```

## Search history
Received requests are saved in a local Database (mongo).  
The API provides 3 extra endpoints to find or delete the received requests.
```bash
GET /flights/history  
GET /flights/history/{id}
DELETE /flights/history/{id}
```

## Design
The implementation of this API follows the Hexagonal Architecture principles, which allows multiple adapters to interact with the database and external systems.  

- pt.flightin.flightsearch.**web** package represents the primary adapter 
- pt.flightin.flightsearch.**core** package represents the business logic
- pt.flightin.flightsearch.**skypicker** package represents the secondary adapter

## Stack
- Java11
- Springboot (web, mongodb)
- Feign
- Lombok/Mapstruct

