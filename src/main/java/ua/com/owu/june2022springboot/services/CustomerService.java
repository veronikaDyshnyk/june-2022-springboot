package ua.com.owu.june2022springboot.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.List;

@Service
@AllArgsConstructor

public class CustomerService {
    private CustomerDAO customerDAO;
    private MailService mailService;

    public void save(Customer customer) {
        customerDAO.save(customer);
        mailService.send(customer);
    }

    public ResponseEntity<List<Customer>> customerListByName(String name) {
        if (name != null && !name.isBlank()) {
            List<Customer> customerByName = customerDAO.findByName(name);
            return new ResponseEntity<>(customerByName, HttpStatusCode.valueOf(200));
        } else {
            throw new RuntimeException();
        }
    }


    public Customer getCustomerById(int id) {
        return customerDAO.findById(id).get();
    }

    public void updateCustomer(Customer customer) {
        customerDAO.save(customer);
    }


}
