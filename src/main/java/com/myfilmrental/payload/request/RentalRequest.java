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
public class RentalRequest {

    @Size(max=10, min=1)
    @NotBlank
    @NotNull
    @NotEmpty
    private String filmId;

    @Size(max=128, min=1)
    @NotBlank
    @NotEmpty
    private String storeId;

    @NotBlank
    @NotNull
    @NotEmpty
    @Email
    private String email;

}

