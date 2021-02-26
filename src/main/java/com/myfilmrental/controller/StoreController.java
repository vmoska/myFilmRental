package com.myfilmrental.controller;

import com.myfilmrental.model.Film;
import com.myfilmrental.model.Store;
import com.myfilmrental.payload.request.StoreRequest;
import com.myfilmrental.payload.response.ApiResponseCustom;
import com.myfilmrental.payload.response.ResponseEntityHandler;
import com.myfilmrental.repository.FilmRepository;
import com.myfilmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

@RestController
@Validated
public class StoreController {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    FilmRepository filmRepository;

    @PostMapping("/add-update-store")
    public ResponseEntity<ApiResponseCustom> addUpdateStore(
            @RequestBody @Valid StoreRequest storeRequest,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<Store> store = storeRepository.findById(storeRequest.getStoreId());

        if (store.isPresent()) {

            boolean exist = storeRepository.existsByStoreName(storeRequest.getStoreName());

            if (exist) {
                response.setMsg("This store name already exist!");
                response.setStatus(HttpStatus.FORBIDDEN);
                return response.getResponseEntity();
            }

            response.setMsg("Store updated with success!");
            response.setStatus(HttpStatus.OK);
            store.get().setStoreName(storeRequest.getStoreName());
            storeRepository.save(store.get());
            return response.getResponseEntity();

        }
        boolean exist = storeRepository.existsByStoreName(storeRequest.getStoreName());

        if(exist) {
            response.setMsg("This store name already exist!");
            response.setStatus(HttpStatus.FORBIDDEN);
            return response.getResponseEntity();
        }

        Store s = new Store(storeRequest.getStoreId(), storeRequest.getStoreName());
        storeRepository.save(s);

        response.setMsg("New store added with success!");
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();
    }

    @PostMapping("/add-film-to-store/{storeId}/{filmId}")
    public ResponseEntity<ApiResponseCustom> addFilmToStore(
            @PathVariable @Size(max=10, min=1) @NotNull @NotEmpty String storeId,
            @PathVariable @Size(max=10, min=1) @NotNull @NotEmpty String filmId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<Store> store = storeRepository.getStoreByStoreId(storeId);

        if (store.isEmpty()) {
            response.setMsg("Store not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<Film> film = filmRepository.findById(filmId);

        if (film.isEmpty()) {
            response.setMsg("Film not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        if (store.get().getFilms().contains(film.get())) {
            response.setMsg("This film " + filmId + " already exists in this store: " + storeId);
            response.setStatus(HttpStatus.FORBIDDEN);
            return response.getResponseEntity();
        }

        store.get().getFilms().add(film.get());
        storeRepository.save(store.get());
        response.setMsg("This film " + filmId + " added to store: " + storeId);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();
    }

}

