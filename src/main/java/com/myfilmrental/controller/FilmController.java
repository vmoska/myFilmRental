package com.myfilmrental.controller;

import com.myfilmrental.model.Actor;
import com.myfilmrental.model.Country;
import com.myfilmrental.model.Film;
import com.myfilmrental.model.Language;
import com.myfilmrental.payload.request.FilmRequest;
import com.myfilmrental.payload.response.*;
import com.myfilmrental.repository.*;
import com.myfilmrental.service.FilmService;
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
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Validated
public class FilmController {

    @Autowired
    FilmRepository filmRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    ActorRepository actorRepository;
    @Autowired
    FilmService filmService;

    @PostMapping("/add-update-film")
    public ResponseEntity<ApiResponseCustom> addUpdateFilm(
            @RequestBody @Valid FilmRequest filmRequest,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<Language> language = languageRepository.findById(filmRequest.getLanguage());
        if (language.isEmpty()) {
            response.setMsg("Language not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<Country> country = countryRepository.findById(filmRequest.getCountry());
        if (country.isEmpty()) {
            response.setMsg("Country not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<Film> film = filmRepository.findById(filmRequest.getFilmId());

        if (film.isPresent()) {
            film.get().setTitle(filmRequest.getTitle());
            film.get().setDescription(filmRequest.getDescription());
            film.get().setReleaseYear(filmRequest.getReleaseYear());
            film.get().setLanguage(language.get());
            film.get().setCountry(country.get());

            Film f = filmRepository.save(film.get());
            response.setMsg("Film updated with success!");
            response.setStatus(HttpStatus.OK);
            return response.getResponseEntity();
        }
        Film filmNew = filmRepository.save(new Film(filmRequest.getFilmId(),filmRequest.getTitle(),
                filmRequest.getDescription(),filmRequest.getReleaseYear(),language.get(),country.get()));

        response.setMsg("New film added with success!");
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();
    }


    @GetMapping("/get-film/{filmId}")
    public ResponseEntity<ApiResponseCustom> getFilmById(
            @PathVariable @Size(max=10, min=1) @NotNull String filmId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<FilmResponse> film = filmRepository.getfilmById(filmId);

        if (film.isEmpty()) {
            response.setMsg("Film not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }
        response.setMsg(film.get());
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/get-films-paged-by-title-asc")
    public ResponseEntity<ApiResponseCustom> getFilmPagedByTitleAsc(
            @RequestParam(defaultValue="0") int pagNo,
            @RequestParam(defaultValue="2") int pagSize,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        List<FilmResponse> films = filmService.findFilmsPagedTitleAsc(pagNo, pagSize);

        if (films.isEmpty()) {
            response.setMsg("Films not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        long totPost = filmRepository.count();

        FilmResponsePaged frp = new FilmResponsePaged(
                pagNo,
                films,
                ((totPost / pagSize) + (totPost % pagSize > 0 ? 1 : 0))
        );

        response.setMsg(frp);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }


    @GetMapping("/find-film-in-store/{filmId}")
    public ResponseEntity<ApiResponseCustom> findFilmInStore(
            @PathVariable @Size(max=10, min=1) @NotNull @NotEmpty String filmId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        Optional<FilmResponse> film = filmRepository.getfilmById(filmId);

        if (film.isEmpty()) {
            response.setMsg("Film not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        Optional<String> filmInStore = storeRepository.getStoreNameByFilmId(filmId);

        if (filmInStore.isEmpty()) {
            response.setMsg("Film not present in any store!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        response.setMsg("The film with ID " + filmId + " is in store " + filmInStore.get());
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();


    }


    @GetMapping("/find-films-by-actors")
    public ResponseEntity<ApiResponseCustom> findFilmsByActors(
            @RequestParam Set<String> lastnames,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        List<Actor> actorsListLastname = actorRepository.findAllByLastNameIn(lastnames);

        if(actorsListLastname.isEmpty()) {
            response.setMsg("No actors found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        List<FilmResponse> filmsByActors = filmRepository.getAllFilmByActors(actorsListLastname, Long.valueOf(actorsListLastname.size()));

        List<String> actorLists = actorsListLastname.stream()
                .map(Actor::toString)
                .collect(Collectors.toList());

        if (filmsByActors.isEmpty()) {
            response.setMsg("These actors " +actorLists+ " are not featured in the same film!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        FilmsActorResponse far = new FilmsActorResponse(actorLists,filmsByActors);
        response.setMsg(far);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/find-films-by-country/{countryId}")
    public ResponseEntity<ApiResponseCustom> findFilmByCountry(
            @PathVariable @Size(max=2, min=1) @NotNull @NotEmpty String countryId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        boolean exist = countryRepository.existsById(countryId);

        if (!exist) {
            response.setMsg("Country not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        List<FilmResponse> filmCountry = filmRepository.getFilmsByCountryId(countryId);

        if (filmCountry.isEmpty()) {
            response.setMsg("Film not product in this country : " + countryId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        response.setMsg(filmCountry);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

    @GetMapping("/find-films-by-language/{languageId}")
    public ResponseEntity<ApiResponseCustom> findFilmByLanguage(
            @PathVariable @Size(max=2, min=1) @NotNull @NotEmpty String languageId,
            HttpServletRequest request) {

        ResponseEntityHandler response = new ResponseEntityHandler(request);

        boolean exist = languageRepository.existsById(languageId);

        if (!exist) {
            response.setMsg("Language not found!");
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        List<FilmResponse> filmByLanguage = filmRepository.getFilmsBylanguageId(languageId);

        if (filmByLanguage.isEmpty()) {
            response.setMsg("Film not found for this language : " + languageId);
            response.setStatus(HttpStatus.NOT_FOUND);
            return response.getResponseEntity();
        }

        response.setMsg(filmByLanguage);
        response.setStatus(HttpStatus.OK);
        return response.getResponseEntity();

    }

}
