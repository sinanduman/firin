package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.model.Siparis;
import com.mordeninaf.boot.firin.model.Tahsilat;
import com.mordeninaf.boot.firin.repository.SiparisRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SiparisService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SiparisService.class);

    @Autowired
    private SiparisRepository siparisRepository;

    public Siparis findById(Integer id) {
        Optional<Siparis> siparis = siparisRepository.findById(id);
        return siparis.orElse(null);
    }

    public List<Siparis> findAll() {
        return siparisRepository.findAll();
    }

    public Page<Siparis> findAll(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        return siparisRepository.findAll(paging);
    }

    public Page<Siparis> findAllByCariIdAndTarihBetween(Integer cariId, String basTarihi, String bitisTarihi, int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        if (cariId == 0) {
            return siparisRepository.findAllByTarihBetween(LocalDate.parse(basTarihi).atStartOfDay(), LocalDate.parse(bitisTarihi).atStartOfDay(), paging);
        } else {
            return siparisRepository.findAllByCariIdAndTarihBetween(cariId, LocalDate.parse(basTarihi).atStartOfDay() , LocalDate.parse(bitisTarihi).atStartOfDay(), paging);
        }
    }

    public List<Siparis> findAllByCariIdAndTarihBetween(Integer cariId, String basTarihi, String bitisTarihi) {
        if (cariId == 0) {
            return siparisRepository.findAllByTarihBetween(LocalDate.parse(basTarihi).atStartOfDay(), LocalDate.parse(bitisTarihi).atStartOfDay());
        } else {
            return siparisRepository.findAllByCariIdAndTarihBetween(cariId, LocalDate.parse(basTarihi).atStartOfDay() , LocalDate.parse(bitisTarihi).atStartOfDay());
        }
    }

    public List<Siparis> findAllByCariId(Integer cariId) {
        return siparisRepository.findAllByCariId(cariId);
    }

    public List<Siparis> findAllByUrunId(Integer urunId) {
        return siparisRepository.findAllByUrunId(urunId);
    }

    public Siparis save(Siparis siparis) {
        return siparisRepository.save(siparis);
    }

    public String remove(Siparis siparis) {
        try {
            siparisRepository.delete(siparis);
            return "OK";
        } catch (Exception e) {
            LOGGER.info(ExceptionUtils.getStackTrace(e));
            return "NOK";
        }
    }

    public List<Siparis> findAllByOnayIs(int onayValue) {
        return siparisRepository.findAllByOnayIs(onayValue);
    }
}
