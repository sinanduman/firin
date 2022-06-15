package com.mordeninaf.boot.firin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Urun {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String urunAd;
    private double urunFiyat = 1;
    private int cariId = 0;
    private int aktif = 1;
    private LocalDateTime tarih;

    public Urun(String urunAd) {
        this.urunAd = urunAd;
    }

    public Urun(String urunAd, LocalDateTime tarih) {
        this.urunAd = urunAd;
        this.tarih = tarih;
    }
}