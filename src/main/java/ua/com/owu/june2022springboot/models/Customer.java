package ua.com.owu.june2022springboot.models;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    @JsonView({Views.Admin.class, Views.Client.class})
    private String name;
    @JsonView({Views.Admin.class, Views.Client.class})
    private String surname;
    @JsonView({Views.Admin.class, Views.Client.class})
    private String email;
    @JsonView({Views.Admin.class, Views.Client.class})
    private boolean isActivated = false;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ActivationToken activationToken;

    public Customer(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
}
