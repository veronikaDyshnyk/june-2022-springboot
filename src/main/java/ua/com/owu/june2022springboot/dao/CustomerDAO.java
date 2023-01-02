package ua.com.owu.june2022springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.com.owu.june2022springboot.models.Customer;

public interface CustomerDAO extends JpaRepository<Customer, Integer> {


    Customer findByLogin(String login);
}
