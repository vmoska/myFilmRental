package com.myfilmrental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="customer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @Column(name="email",length=50)
    private String email;

    @Column(name="first_name",length=45)
    private String firstName;

    @Column(name="last_name",length=45)
    private String lastName;

}
