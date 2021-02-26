package com.myfilmrental.repository;

import com.myfilmrental.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store,String> {

    @Query(value="SELECT s.storeName "
            + "FROM Store s "
            + "JOIN s.films sf "
            + "WHERE sf.filmId = :filmId ")
    Optional<String> getStoreNameByFilmId(@Param("filmId") String filmId);


    @Query(value="SELECT s "
            + "FROM Store s "
            + "LEFT JOIN FETCH s.films "
            + "WHERE s.storeId= :storeId ")
    Optional<Store> getStoreByStoreId(@Param("storeId") String storeId);

    boolean existsByStoreName(String storeName);
}
