package com.myfilmrental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="rental")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    @EmbeddedId
    private RentalId rentalId;

    @Column(name="rental_return", nullable=false, columnDefinition="Date")
    private Date rentalReturn;

    public Rental(RentalId rentalId) {
        this.rentalId = rentalId;
    }

}
