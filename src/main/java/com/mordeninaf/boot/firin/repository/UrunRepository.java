package com.mordeninaf.boot.firin.repository;

import com.mordeninaf.boot.firin.entity.Urun;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrunRepository extends CrudRepository<Urun, Integer> {
    List<Urun> findAll();
}
