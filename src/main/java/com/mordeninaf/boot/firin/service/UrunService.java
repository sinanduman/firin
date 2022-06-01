package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.entity.Urun;
import com.mordeninaf.boot.firin.repository.UrunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UrunService {

    @Autowired
    private UrunRepository urunRepository;

    public Urun findById(Integer id) {
        Optional<Urun> urun = urunRepository.findById(id);
        return urun.orElse(null);
    }

    public List<Urun> findAll() {
        return urunRepository.findAll();
    }

    public void save(Urun urun) {
        urunRepository.save(urun);
    }
}
