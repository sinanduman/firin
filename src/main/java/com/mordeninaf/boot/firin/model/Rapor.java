package com.mordeninaf.boot.firin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

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