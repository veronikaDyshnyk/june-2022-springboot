package ua.com.owu.june2022springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.com.owu.june2022springboot.models.Customer;

import java.util.List;

public interface CustomerDAO extends JpaRepository<Customer, Integer> {

    @Query("select c from Customer c where c.name=:name")
    List<Customer> findByName(String name);

//    List<Customer> findByName(String name);

    List<Customer> findBySurname(String surname);
}
