# Shortest Path Delivery Optimizer
The Shortest Path Delivery Optimizer is a Spring Boot application that calculates the optimal delivery route for a given set of driver, restaurant, and customer locations, along with preparation times at each restaurant. The application uses Dijkstra's algorithm to find the shortest path and minimize the total delivery time.
## Features
* Calculates the optimal delivery route based on driver, restaurant, and customer locations
* Considers preparation times at each restaurant
* Uses Dijkstra's algorithm for finding the shortest path
* Handles edge cases, corner cases, and null cases
* Follows separation of concerns principles
* Extensible and modular design
* Testable code with unit tests
* Proper exception handling
* Minimizes potential bugs through validation and error handling
## API Endpoint
## Request
* Method: POST
* URL: /api/delivery
* Content-Type: application/json
### Request Body
```json
{
  "startLocation": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "consumer1": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "consumer2": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "restaurant1": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "restaurant2": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "preparationTime1": 0.3,
  "preparationTime2": 0.3
}
```
## Response
```json
{
  "shortestPath": [
    {
      "latitude": 1.0,
      "longitude": 1.0
    },
    {
      "latitude": 1.0,
      "longitude": 1.0
    },
    {
      "latitude": 1.0,
      "longitude": 1.0
    },
    {
      "latitude": 1.0,
      "longitude": 1.0
    },
    {
      "latitude": 1.0,
      "longitude": 1.0
    }
  ],
  "totalTime": 0.6
}
```


## Testing with cURL
You can test the API endpoint using cURL with the following command:
```bash
curl -X POST "http://localhost:8080/api/delivery" -H "Content-Type: application/json" -d '{
  "startLocation": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "consumer1": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "consumer2": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "restaurant1": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "restaurant2": {
    "latitude": 1.0,
    "longitude": 1.0
  },
  "preparationTime1": 0.3,
  "preparationTime2": 0.3
}'
```


Make sure to replace `http://localhost:8080` with the actual URL and port where your application is running.
## Code Structure
The code follows a modular and extensible structure, with clear separation of concerns:
* **DeliveryController:** Handles the API endpoint and maps exceptions to appropriate HTTP status codes.
* **DeliveryService:** Defines the interface for calculating the shortest path.
* **DeliveryServiceImpl:** Implements the shortest path calculation logic using Dijkstra's algorithm.
* **DistanceCalculator:** Defines the interface for calculating distances between locations.
* **HaversineDistanceCalculator:** Implements the Haversine formula for calculating distances between geographical coordinates.
* **DeliveryRequest:** Represents the input request with driver, restaurant, and customer locations, along with preparation times.
* **DeliveryResponse:** Represents the output response with the shortest path and total delivery time.
* **Location:** Represents a geographical location with latitude and longitude.
## Exception Handling
The code includes proper exception handling:
* **IllegalArgumentException** is thrown for invalid input scenarios, such as null values or the start location being the same as consumer or restaurant locations.
* Exceptions are caught and mapped to appropriate HTTP status codes in the **DeliveryController**.
* Null checks are performed using **Objects.requireNonNull()** to prevent null pointer exceptions.
## Testability
The code is designed to be testable:
* The DeliveryService and DistanceCalculator interfaces allow for easy mocking and unit testing.
* The DeliveryServiceImpl class can be unit tested by mocking the DistanceCalculator dependency.
* The DeliveryController can be tested using integration tests or by mocking the DeliveryService dependency.
## Extensibility and Modularity
The code is extensible and modular:
* New distance calculation algorithms can be added by implementing the DistanceCalculator interface.
* Additional functionality can be added to the DeliveryService without modifying the existing code.
* The DeliveryRequest and DeliveryResponse classes can be extended to include additional properties if needed.
## Validation and Error Handling
The code includes validation and error handling to minimize potential bugs:
* The validateRequest method in DeliveryServiceImpl checks for null values and invalid input scenarios.
* The @NotNull annotation is used in the DeliveryRequest class to validate input fields.
* Exceptions are caught and handled appropriately in the DeliveryController and DeliveryServiceImpl classes.
## Build and Run
To build and run the application, follow these steps:
* Clone the repository.
* Make sure you have Java 11 or higher installed.
* Build the project using Gradle: ./gradlew build
* Run the application: ./gradlew bootRun
* The application will be accessible at http://localhost:8080.
## Conclusion
The Shortest Path Delivery Optimizer provides a reliable and efficient solution for calculating the optimal delivery route based on driver, restaurant, and customer locations, while considering preparation times at each restaurant. The code is designed to be functionally correct, handle edge cases and null values, follow separation of concerns principles, and be extensible, modular, and testable. Proper exception handling and validation minimize potential bugs, ensuring a robust and reliable application.
