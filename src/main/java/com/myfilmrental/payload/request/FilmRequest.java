package com.myfilmrental.payload.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
public class FilmRequest {

    @Size(max=10,min=1)
    @NotBlank @NotNull  @NotEmpty
    private String  filmId;

    @Size(max=128,min=1)
    @NotBlank @NotEmpty
    private String title;

    @NotBlank
    @NotNull @NotEmpty
    private String description;

    @NotNull
    @Min(value=1900)
    @Max(value=2021)
    private int releaseYear;

    @NotNull @NotEmpty
    @Size(max=2,min=1)
    private String language;

    @NotNull @NotEmpty
    @Size(max=2,min=1)
    private String country;

}

