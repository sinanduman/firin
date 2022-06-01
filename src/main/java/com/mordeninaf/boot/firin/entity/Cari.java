package com.mordeninaf.boot.firin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CARI")
public class Cari {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String cariAd;
    private String cariTel;
    private String cariAdres;
    private String cariYetkili;
    private int cariAktif;

    public Cari(String cariAd, String cariYetkili, String cariTel, int cariAktif) {
        this.cariAd = cariAd;
        this.cariYetkili = cariYetkili;
        this.cariTel = cariTel;
        this.cariAktif = cariAktif;
    }
}