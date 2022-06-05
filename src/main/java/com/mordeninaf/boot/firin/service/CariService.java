package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.repository.CariRepository;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CariService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CariService.class);

    @Autowired
    private CariRepository cariRepository;

    public Cari findById(Integer id) {
        Optional<Cari> cari = cariRepository.findById(id);
        return cari.orElse(null);
    }

    public List<Cari> findAll() {
        return cariRepository.findAll();
    }

    public Cari save(Cari cari) {
        return cariRepository.save(cari);
    }

    public String remove(Cari cari) {
        try {
            cariRepository.delete(cari);
            return "OK";
        } catch (Exception e) {
            LOGGER.info(ExceptionUtils.getStackTrace(e));
            return "NOK";
        }
    }
}
