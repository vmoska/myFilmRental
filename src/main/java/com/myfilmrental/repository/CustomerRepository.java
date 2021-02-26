package com.myfilmrental.repository;

import com.myfilmrental.model.Customer;
import com.myfilmrental.payload.response.CustomerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,String> {

    @Query(value="SELECT DISTINCT new com.myfilmrental.payload.response.CustomerResponse("
            + "c.email, "
            + "c.firstName, "
            + "c.lastName "
            + ") "
            + "FROM Customer c "
            + "JOIN Rental r on r.rentalId.store.storeId=:storeId "
            + "AND r.rentalId.customer=c ")
    Set<CustomerResponse> getCustomersByStore(@Param("storeId") String storeId);

    @Query(value="SELECT DISTINCT new com.myfilmrental.payload.response.CustomerResponse("
            + "c.email, "
            + "c.firstName, "
            + "c.lastName "
            + ") "
            + "FROM Customer c "
            + "JOIN Rental r on r.rentalId.customer=c "
            + "JOIN Film f on r.rentalId.film=f  "
            + "AND f.language.languageId=:languageId ")
    Set<CustomerResponse> getCustomersByRentFilmByLanguage(@Param("languageId") String languageId);

}
