package com.myfilmrental.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RentalId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name="rental_date", columnDefinition="Date")
    private Date rentalDate;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="customer_email", nullable = false)
    private Customer customer;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="film_id", nullable=false)
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable=false)
    private Store store;

}
