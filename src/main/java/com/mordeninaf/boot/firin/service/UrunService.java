package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.model.Urun;
import com.mordeninaf.boot.firin.repository.UrunRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UrunService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrunService.class);

    private final UrunRepository urunRepository;

    public Urun findById(Integer id) {
        Optional<Urun> urun = urunRepository.findById(id);
        return urun.orElse(null);
    }

    public List<Urun> findAll() {
        List<Urun> urunList = urunRepository.findAll();
        urunList.sort(Comparator.comparing(Urun::getUrunAd));
        return urunList;
    }

    @Transactional
    public Urun save(Urun urun) {
        return urunRepository.save(urun);
    }

    @Transactional
    public String remove(Urun urun) {
        try {
            urunRepository.delete(urun);
            return "OK";
        } catch (Exception e) {
            LOGGER.info(ExceptionUtils.getStackTrace(e));
            return "NOK";
        }
    }
}
