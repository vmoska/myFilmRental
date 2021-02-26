package com.myfilmrental.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.myfilmrental.payload.response.FilmResponse;
import com.myfilmrental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FilmService {

    @Autowired
    FilmRepository filmRepository;

    public List<FilmResponse> findFilmsPagedTitleAsc(int pageNo, int pageSize) {

        Pageable paging = PageRequest.of(pageNo, pageSize);

        Page<FilmResponse> page = filmRepository.getAllFilmPagedByTitleAsc(paging);

        if (page.hasContent()) {
            return page.getContent();
        } else {
            return new ArrayList<FilmResponse>();

        }
    }

    public Date adjustDate(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        Date correctDate = Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
        return correctDate;
    }

}