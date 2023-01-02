package ua.com.owu.june2022springboot.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ua.com.owu.june2022springboot.dao.CustomerDAO;
import ua.com.owu.june2022springboot.models.Customer;

@RestController
@AllArgsConstructor
public class MainController {

    private CustomerDAO customerDAO;

    private PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String open() {
        return "open";
    }


    @PostMapping("/save")
    public void save(@RequestBody Customer customer) {
        String password = customer.getPassword();
        String encode = passwordEncoder.encode(password);
        customer.setPassword(encode);

        customerDAO.save(customer);
    }

    @GetMapping("/secure")
    public String Secure() {
        return "secure";
    }
}
