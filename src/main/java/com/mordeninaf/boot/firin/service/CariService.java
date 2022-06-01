package com.mordeninaf.boot.firin.service;

import com.mordeninaf.boot.firin.entity.Cari;
import com.mordeninaf.boot.firin.repository.CariRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CariService {

    @Autowired
    private CariRepository cariRepository;

    public Cari findById(Integer id) {
        Optional<Cari> cari = cariRepository.findById(id);
        return cari.orElse(null);
    }

    public List<Cari> findAll() {
        return cariRepository.findAll();
    }

    public void save(Cari cari) {
        cariRepository.save(cari);
    }
}
