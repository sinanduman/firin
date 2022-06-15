package com.mordeninaf.boot.firin.repository;

import com.mordeninaf.boot.firin.model.Tahsilat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TahsilatRepository extends PagingAndSortingRepository<Tahsilat, Integer> {
    List<Tahsilat> findAll();
    Page<Tahsilat> findAll(Pageable pageable);
    Page<Tahsilat> findAllByCariId(Integer cariId, Pageable pageable);
    Page<Tahsilat> findAllByCariIdAndTarihOdeme(Integer cariId, String basTarihi, Pageable pageable);
    Page<Tahsilat> findAllByTarihOdeme(String basTarihi, Pageable pageable);
    List<Tahsilat> findAllByTarihOdemeBetween(String basTarihi, String bitisTarihi);
    Page<Tahsilat> findAllByTarihOdemeBetween(String basTarihi, String bitisTarihi, Pageable pageable);
    List<Tahsilat> findAllByCariIdAndTarihOdemeBetween(Integer cariId, String basTarihi, String bitisTarihi);
    Page<Tahsilat> findAllByCariIdAndTarihOdemeBetween(Integer cariId, String basTarihi, String bitisTarihi, Pageable pageable);
    List<Tahsilat> findAllByCariId(Integer cariId);
    List<Tahsilat> findAllByTarihOdeme(String basTarihi);
}
