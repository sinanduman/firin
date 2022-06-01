package com.mordeninaf.boot.firin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Component

@Entity(name = "URUN")
public class Urun {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String urunAd;
    private int urunAktif;

    public Urun(String urunAd, int urunAktif) {
        this.urunAd = urunAd;
        this.urunAktif = urunAktif;
    }
}