package com.myfilmrental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="actor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Actor {

    @Id
    @Column(name="actor_id", length=10)
    private String actorId;

    @Column(name="first_name", nullable=false, length=45)
    private String firstName;

    @Column(name="last_name", nullable=false, length=45)
    private String lastName;

    @ManyToOne
    @JoinColumn(name="country_id", nullable=false)
    private Country country;

    @Override
    public String toString() {
        return "Actor [firstName=" + firstName + ", lastName=" + lastName + "]";
    }

}
