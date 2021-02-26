package com.myfilmrental.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    @Size(max=50)
    @NotBlank @NotNull  @NotEmpty
    @Email
    private String  email;

    @Size(max=50)
    private String firstName;

    @Size(max=50)
    private String lastName;

}
