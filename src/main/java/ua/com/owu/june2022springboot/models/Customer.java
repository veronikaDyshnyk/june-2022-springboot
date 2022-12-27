package ua.com.owu.june2022springboot.models;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.com.owu.june2022springboot.models.views.Views;

import javax.swing.text.View;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Admin.class)
    private int id;
    @JsonView({Views.Admin.class,Views.Client.class})
    private String name;
    @JsonView({Views.Admin.class,Views.Client.class})
    private  String surname;
    @JsonView({Views.Admin.class,Views.Client.class})
    private String email;
    @JsonView({Views.Admin.class,Views.Client.class})
    private boolean isActivated = false;
    private String avatar; //path to img

    public Customer(String name) {
        this.name = name;
    }

    public Customer(String name, String surname, String email, boolean isActivated, String avatar) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isActivated = isActivated;
        this.avatar = avatar;
    }

    public Customer(String name, String email, String avatar) {
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }
}
