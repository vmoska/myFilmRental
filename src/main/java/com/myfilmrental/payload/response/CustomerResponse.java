package com.myfilmrental.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {

    private String email;
    private String firstname;
    private String lastname;

}
