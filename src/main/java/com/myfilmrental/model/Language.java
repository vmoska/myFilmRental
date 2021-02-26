package com.myfilmrental.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="language")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    @Column(name="language_id",length=2)
    private String  languageId;

    @Column(name="language_name",unique=true, nullable=false,length=40)
    private String languageName;

}
