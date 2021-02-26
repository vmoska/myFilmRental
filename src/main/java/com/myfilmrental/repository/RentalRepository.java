package com.myfilmrental.repository;

import com.myfilmrental.model.*;
import com.myfilmrental.payload.response.FilmRentResponse;
import com.myfilmrental.payload.response.FilmRentResponseNr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RentalRepository extends JpaRepository<Rental, RentalId> {

    @Query(value="SELECT COUNT(*) "
            + "FROM rental "
            + "where rental_date "
            + "between :startDate and :endDate ",nativeQuery = true)
    int countRentalInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value="SELECT DISTINCT new com.myfilmrental.payload.response.FilmRentResponse("
            + "f.filmId, "
            + "f.title "
            + ") "
            + "FROM Rental r "
            + "JOIN Film f on f=r.rentalId.film "
            + "WHERE r.rentalId.customer.email = :email ")
    Set<FilmRentResponse> getRentFilmsByCustomerId(@Param("email") String email);

    @Query(value="SELECT new com.myfilmrental.payload.response.FilmRentResponseNr("
            + "f.filmId, "
            + "f.title, "
            + "COUNT(r.rentalId.film) as counter ) "
            + "FROM Rental r "
            + "JOIN Film f on f=r.rentalId.film "
            + "GROUP BY r.rentalId.film "
            + "ORDER BY counter DESC")
    List<FilmRentResponseNr> getFilmWithMaxRent();



    @Query(value="SELECT r from Rental r "
            + "WHERE r.rentalId.store=:store "
            + "AND r.rentalId.film=:film "
            + "AND r.rentalId.customer=:customer "
            + "AND r.rentalReturn >= CURRENT_DATE "
    )
    Optional<Rental> getRentalFilmUpdate(@Param("store") Store store, @Param("film") Film film, @Param("customer") Customer customer);



}

