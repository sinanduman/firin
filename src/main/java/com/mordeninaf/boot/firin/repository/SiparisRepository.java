package com.mordeninaf.boot.firin.repository;

import com.mordeninaf.boot.firin.model.Siparis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SiparisRepository extends PagingAndSortingRepository<Siparis, Integer> {
    List<Siparis> findAll();
    List<Siparis> findAllByCariId(Integer cariId);
    List<Siparis> findAllByUrunId(Integer urunId);
    List<Siparis> findAllByOnayIs(int onayValue);
    List<Siparis> findAllByTarihBetween(LocalDateTime basTarihi, LocalDateTime bitisTarihi);
    List<Siparis> findAllByCariIdAndTarihBetween(Integer cariId, LocalDateTime basTarihi, LocalDateTime bitisTarihi);
    List<Siparis> findAllByUrunIdAndTarihBetween(Integer urunId, LocalDateTime basTarihi, LocalDateTime bitisTarihi);
    List<Siparis> findAllByCariIdAndUrunIdAndTarihBetween(Integer cariId, Integer urunId, LocalDateTime basTarihi, LocalDateTime bitisTarihi);
    Page<Siparis> findAll(Pageable pageable);
    Page<Siparis> findAllByTarihBetween(LocalDateTime basTarihi, LocalDateTime bitisTarihi, Pageable paging);
    Page<Siparis> findAllByCariIdAndTarihBetween(Integer cariId, LocalDateTime basTarihi, LocalDateTime bitisTarihi, Pageable paging);
    Page<Siparis> findAllByUrunIdAndTarihBetween(Integer urunId, LocalDateTime basTarihi, LocalDateTime bitisTarihi, Pageable paging);
    Page<Siparis> findAllByCariIdAndUrunIdAndTarihBetween(Integer cariId, Integer urunId, LocalDateTime basTarihi, LocalDateTime bitisTarihi, Pageable paging);
}
