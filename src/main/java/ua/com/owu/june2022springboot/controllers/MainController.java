package ua.com.owu.june2022springboot.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {


    private List<Customer> customers = new ArrayList<>();

    public MainController() {
        customers.add(new Customer(1, "name"));
        customers.add(new Customer(2, "mary"));
        customers.add(new Customer(3, "rory"));
    }

    @GetMapping("/customers")
//    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<Customer>> getCustomers() {
//        ResponseEntity<List<Customer>> responseEntity = new ResponseEntity<>(this.customers, HttpStatus.OK);
//        return responseEntity;
        return new ResponseEntity<>(this.customers, HttpStatus.OK);
    }


    @PostMapping("/customers")
    public ResponseEntity<List<Customer>> addCustomer(@RequestBody Customer customer) {
        System.out.println(customer);
        this.customers.add(customer);
        return new ResponseEntity<>(this.customers, HttpStatus.CREATED);
    }

    @GetMapping("/customers/{id}")
    private ResponseEntity<Customer> getCustomer(@PathVariable int id) {
        Customer customer = this.customers.get(id - 1);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<List<Customer>> deleteCustomer(@PathVariable int id) {
        this.customers.remove(id - 1);
        return new ResponseEntity<>(this.customers, HttpStatusCode.valueOf(200));
    }


    //todo put, patch

}
