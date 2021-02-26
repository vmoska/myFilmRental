package com.myfilmrental.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="store")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store {

    @Id
    @Column(name="store_id", length=10)
    private String storeId;

    @Column(name="store_name", unique=true, length=50)
    private String storeName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="inventory", joinColumns = { @JoinColumn(name="store_id") },
            inverseJoinColumns = {@JoinColumn(name="film_id") })
    private Set<Film> films;

    public Store(String storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

}
