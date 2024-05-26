import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CarRentalController {

    @Autowired
    private CarRentalSystem rentalSystem;

    @GetMapping("/availableCars")
    public List<Car> getAvailableCars() {
        return ((Collection<Car>) rentalSystem.getCars()).stream()
            .filter(Car::isAvailable)
            .collect(Collectors.toList());
    }

    @PostMapping("/rent")
    public Response rentCar(@RequestBody RentRequest request) {
        Customer customer = new Customer("CUS" + (rentalSystem.getCustomers().size() + 1), request.getCustomerName());
        rentalSystem.addCustomer(customer);

        Car selectedCar = rentalSystem.getCars().stream()
            .filter(car -> car.getCarId().equals(request.getCarId()) && car.isAvailable())
            .findFirst().orElse(null);

        if (selectedCar != null) {
            rentalSystem.rentCar(selectedCar, customer, request.getRentalDays());
            double totalPrice = selectedCar.calculatePrice(request.getRentalDays());
            return new Response("Car rented successfully. Total Price: $" + totalPrice);
        } else {
            return new Response("Invalid car selection or car not available for rent.");
        }
    }

    @PostMapping("/return")
    public Response returnCar(@RequestBody ReturnRequest request) {
        Car carToReturn = rentalSystem.getCars().stream()
            .filter(car -> car.getCarId().equals(request.getCarId()) && !car.isAvailable())
            .findFirst().orElse(null);

        if (carToReturn != null) {
            rentalSystem.returnCar(carToReturn);
            return new Response("Car returned successfully.");
        } else {
            return new Response("Invalid car ID or car is not rented.");
        }
    }

    static class RentRequest {
        private String customerName;
        private String carId;
        private int rentalDays;

        // getters and setters
    }

    static class ReturnRequest {
        private String carId;

        // getters and setters
    }

    static class Response {
        private String message;

        public Response(String message) {
            this.message = message;
        }

        // getters and setters
    }
}
