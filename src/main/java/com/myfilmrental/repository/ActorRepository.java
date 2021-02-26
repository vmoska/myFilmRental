package com.myfilmrental.repository;

import java.util.List;
import java.util.Set;

import com.myfilmrental.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, String>{

    List<Actor> findAllByLastNameIn(Set<String> lastnames);

}