package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.repository.CariRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CariService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CariService.class);

    private final CariRepository cariRepository;

    public Cari findById(Integer id) {
        Optional<Cari> cari = cariRepository.findById(id);
        return cari.orElse(null);
    }

    public List<Cari> findAll() {
        return cariRepository.findAll();
    }

    @Transactional
    public Cari save(Cari cari) {
        return cariRepository.save(cari);
    }

    @Transactional
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
