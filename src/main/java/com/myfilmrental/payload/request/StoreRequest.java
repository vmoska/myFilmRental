package com.myfilmrental.payload.request;

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
public class StoreRequest {

    @Size(max=10, min=1)
    @NotBlank
    @NotNull
    @NotEmpty
    private String storeId;

    @Size(max=50, min=1)
    @NotBlank
    @NotEmpty
    private String storeName;

}

