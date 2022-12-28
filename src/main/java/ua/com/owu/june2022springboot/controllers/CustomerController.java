package ua.com.owu.june2022springboot.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;
import ua.com.owu.june2022springboot.models.dto.CustomerDTO;
import ua.com.owu.june2022springboot.models.views.Views;
import ua.com.owu.june2022springboot.services.CustomerService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {

    private CustomerDAO customerDAO;

    private CustomerService customerService;


    //get all
    @GetMapping("")
    @JsonView({Views.Client.class})
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> allCustomers = customerDAO.findAll();
        return new ResponseEntity<>(allCustomers, HttpStatusCode.valueOf(200));
    }

    //save
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCustomer(@RequestBody Customer customer) {
//        customerDAO.save(customer);

        customerService.save(customer);
    }

    //get by id
    @GetMapping("/{id}")
    @JsonView(Views.Admin.class)
    public ResponseEntity<Customer> getOneCustomer(@PathVariable int id) {
        Customer customer = customerDAO.findById(id).get();
        return new ResponseEntity<>(customer, HttpStatusCode.valueOf(200));
    }

    //delete
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@PathVariable int id) {
        customerDAO.deleteById(id);
    }

    //update
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateCustomer(@PathVariable int id, @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerDAO.findById(id).get();
        customer.setName(customerDTO.getUsername());
        customerDAO.save(customer);
    }

    //get all by name
//    @GetMapping("/find/name/{name}")
//    public List<Customer> findAllByName(@PathVariable String name) {
//        return customerDAO.findByName(name);
//    }

    @GetMapping("find/name/{name}")
    public ResponseEntity<List<Customer>> getCustomersByName(@PathVariable String name) {
        return customerService.customerListByName(name);
    }


    //get all by name
    @GetMapping("/find/surname/{surname}")
    public List<Customer> findAllBySurname(@PathVariable String surname) {
        return customerDAO.findBySurname(surname);
    }


    @GetMapping("/activate/{token}")
    public void activateCustomer(@PathVariable String token) {
        Customer customer = customerService.byToken(token);
        customerService.activate(customer);

    }

















}
