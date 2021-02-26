package com.myfilmrental.controller;

import com.myfilmrental.model.Customer;
import com.myfilmrental.payload.request.CustomerRequest;
import com.myfilmrental.payload.response.ApiResponseCustom;
import com.myfilmrental.payload.response.CustomerResponse;
import com.myfilmrental.payload.response.ResponseEntityHandler;
import com.myfilmrental.repository.CustomerRepository;
import com.myfilmrental.repository.FilmRepository;
import com.myfilmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
public class CustomerController {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    FilmRepository filmRepository;

    @PostMapping("/add-update-customer")
    public ResponseEntity<ApiResponseCustom> addUpdateCustomer(
            @RequestBody @Valid CustomerRequest customerRequest,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<Customer> customer = customerRepository.findById(customerRequest.getEmail());

        if(customer.isPresent()) {
            customer.get().setFirstName(customerRequest.getFirstName());
            customer.get().setLastName(customerRequest.getLastName());

            Customer c = customerRepository.save(customer.get());
            response.setMsg("Customer updated with success!");
            response.setStatus(HttpStatus.OK);
            return response.getResponseEntity();

        }else {
            Customer customerNew = customerRepository.save(new Customer(
                    customerRequest.getEmail(),
                    customerRequest.getFirstName(),
                    customerRequest.getLastName())
            );
            response.setMsg("New customer added with success!");
            response.setStatus(HttpStatus.OK);
            return response.getResponseEntity();
        }


    }

    @GetMapping("/get-all-customers-by-store/{storeId}")
    public ResponseEntity<ApiResponseCustom> getAllCutomersByStore(
            @PathVariable @Size(min = 1,max = 10) @NotNull @NotEmpty String storeId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        boolean exist = storeRepository.existsById(storeId);

        if(!exist){
            response.setMsg("Store not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Set<CustomerResponse> cr = customerRepository.getCustomersByStore(storeId);

        if(cr.isEmpty()) {
            response.setMsg("No customers found in the store!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }
        else {
            response.setMsg(cr);
            response.setStatus(HttpStatus.OK);
            return response.getResponseEntity();
        }

    }

}
