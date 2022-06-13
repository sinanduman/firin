package com.mordeninaf.boot.firin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Rapor {
    @Id
    private Integer id;
    private String cariAd;
    private String urunAd;
    private Integer adet;
    private Double tutar;
    private String satisIade;
    private String odemeTarihi;
    private String kayitTarihi;
}