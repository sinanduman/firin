package com.mordeninaf.boot.firin.repository;

import com.mordeninaf.boot.firin.entity.Cari;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CariRepository extends CrudRepository<Cari, Integer> {
    List<Cari> findAll();
}
