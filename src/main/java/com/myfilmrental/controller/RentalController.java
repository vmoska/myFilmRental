package com.myfilmrental.controller;

import com.myfilmrental.model.*;
import com.myfilmrental.payload.request.RentalRequest;
import com.myfilmrental.payload.response.*;
import com.myfilmrental.repository.*;
import com.myfilmrental.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
public class RentalController {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    FilmRepository filmRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    FilmService filmService;

    @PostMapping("/add-update-rental")
    public ResponseEntity<ApiResponseCustom> addUpdateRental(
            @RequestBody @Valid RentalRequest rentalRequest,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<Customer> customer = customerRepository.findById(rentalRequest.getEmail());

        if (customer.isEmpty()) {
            response.setMsg("Customer not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<Store> store = storeRepository.getStoreByStoreId(rentalRequest.getStoreId());

        if (store.isEmpty()) {
            response.setMsg("Store not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<Film> film = filmRepository.findById(rentalRequest.getFilmId());

        if (film.isEmpty()) {
            response.setMsg("Film not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        if (!store.get().getFilms().contains(film.get())) {
            response.setMsg("Film not found in the store!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<Rental> rental = rentalRepository.getRentalFilmUpdate(store.get(),film.get(),customer.get());

        if (rental.isPresent()) {
            rental.get().setRentalReturn(new Date());
            rentalRepository.save(rental.get());

            response.setMsg("Rental return updated with success!");
            response.setStatus(HttpStatus.OK);
            return response.getResponseEntity();

        }

        RentalId rentalId=new RentalId(new Date(),customer.get(),new Film(rentalRequest.getFilmId()),store.get());

        rentalRepository.save(new Rental(rentalId,filmService.adjustDate(new Date(253402214400000L))));

        response.setMsg("New rental added with success!");
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/get-rentals-number-in-date-range")
    public ResponseEntity<ApiResponseCustom> getRentalsNumberInDateRange(
            @RequestParam @Valid @NotNull @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate startDate,
            @RequestParam @Valid @NotNull @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate endDate,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        if (endDate.isBefore(startDate)) {
            response.setMsg("Not valid start date & end date!");
            response.setStatus(HttpStatus.FORBIDDEN);
            return response.getResponseEntity();
        }

        int count = rentalRepository.countRentalInDateRange(startDate, endDate);

        response.setMsg("Number of rental is " + count);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/find-all-films-rent-by-one-customer/{customerId}")
    public ResponseEntity<ApiResponseCustom> findFilmsRentByOneCustomer(
            @PathVariable @Email @NotNull @NotEmpty String customerId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        boolean exist = customerRepository.existsById(customerId);

        if (!exist) {
            response.setMsg("Customer not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Set<FilmRentResponse> frboc = rentalRepository.getRentFilmsByCustomerId(customerId);

        if (frboc.isEmpty()) {
            response.setMsg("No rent present for this customer " + customerId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        response.setMsg(frboc);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/find-film-with-max-number-of-rent")
    public ResponseEntity<ApiResponseCustom> findFilmsWithMaxRent(HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        List<FilmRentResponseNr> frr = rentalRepository.getFilmWithMaxRent();

        if (frr.isEmpty()) {
            response.setMsg("No rent present!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        FilmRentResponseNr filmWithMaxRent = frr.get(0);

        response.setMsg(filmWithMaxRent);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/get-customers-who-rent-films-by-language-film/{languageId}")
    public ResponseEntity<ApiResponseCustom> getCustomersRentFilmByLanguage(
            @PathVariable @Size(max=2, min=1) @NotNull @NotEmpty String languageId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        boolean exist = languageRepository.existsById(languageId);

        if (!exist) {
            response.setMsg("Language not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Set<CustomerResponse> cr = customerRepository.getCustomersByRentFilmByLanguage(languageId);

        if (cr.isEmpty()) {
            response.setMsg("No rent present for this language " + languageId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        response.setMsg(cr);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

}

