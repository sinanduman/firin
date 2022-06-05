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
public class Siparis {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer urunId;
    private Integer cariId;
    private Double tutar;
    private int adet = 1;
    private int onay = 0;
    private int aktif = 1;
    private int satisIade;
    private LocalDateTime tarih;

    public Siparis(Integer urunId, Integer cariId, Integer adet, LocalDateTime tarih) {
        this.urunId = urunId;
        this.cariId = cariId;
        this.adet = adet;
        this.tarih = tarih;
    }
}