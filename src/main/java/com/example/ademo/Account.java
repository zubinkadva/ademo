package com.example.ademo;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Account() {
    }

    private String email;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String password;

}
