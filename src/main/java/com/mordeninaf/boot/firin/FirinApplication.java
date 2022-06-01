package com.mordeninaf.boot.firin;

import com.mordeninaf.boot.firin.entity.Cari;
import com.mordeninaf.boot.firin.entity.Urun;
import com.mordeninaf.boot.firin.repository.CariRepository;
import com.mordeninaf.boot.firin.repository.UrunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class FirinApplication {

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(FirinApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FirinApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadUrunData(UrunRepository urunRepository) {
        return (args) -> {
            List<Urun> urunList = urunRepository.findAll();
            if (urunList.isEmpty()) {
                urunRepository.save(new Urun("KÜÇÜK TAVA", 1));
                urunRepository.save(new Urun("BÜYÜK TAVA", 1));
                urunRepository.save(new Urun("TRABZON", 1));
                urunRepository.save(new Urun("BALİNA", 1));
                urunRepository.save(new Urun("KARA BUĞDAY", 1));
                urunRepository.save(new Urun("MISIR", 1));
                urunRepository.save(new Urun("TAHİNLİ", 1));
                urunRepository.save(new Urun("POĞAÇA", 1));

                LOGGER.info("{} Ürün kaydı eklendi.", urunRepository.findAll().size());

            }
        };
    }

    @Bean
    public CommandLineRunner loadCariData(CariRepository cariRepository) {
        return (args) -> {
            List<Cari> cariList = cariRepository.findAll();
            if (cariList.isEmpty()) {
                cariRepository.save(new Cari("ADİL", "", "", 1));
                cariRepository.save(new Cari("ASLANOĞLU", "", "", 1));
                cariRepository.save(new Cari("AYDIN", "", "", 1));
                cariRepository.save(new Cari("BAHAR", "", "", 1));
                cariRepository.save(new Cari("BAŞTÜRK", "", "", 1));
                cariRepository.save(new Cari("BEREKET", "", "", 1));
                cariRepository.save(new Cari("BESLEN", "", "", 1));
                cariRepository.save(new Cari("BEYDAĞI", "", "", 1));
                cariRepository.save(new Cari("BİZİM", "", "", 1));
                cariRepository.save(new Cari("DOĞAL TÜKKAN", "", "", 1));
                cariRepository.save(new Cari("DOĞUŞ GIDA", "", "", 1));
                cariRepository.save(new Cari("HANBEY", "", "", 1));
                cariRepository.save(new Cari("HAS PEYNİR", "", "", 1));
                cariRepository.save(new Cari("K. KAHV.", "", "", 1));
                cariRepository.save(new Cari("KAHVALTI D", "", "", 1));
                cariRepository.save(new Cari("KÖYÜM", "", "", 1));
                cariRepository.save(new Cari("LİMJORA", "", "", 1));
                cariRepository.save(new Cari("MARAŞ", "", "", 1));
                cariRepository.save(new Cari("NEFİS", "", "", 1));
                cariRepository.save(new Cari("NUR", "", "", 1));
                cariRepository.save(new Cari("OF PEYNİR", "", "", 1));
                cariRepository.save(new Cari("OF SÜT 1", "", "", 1));
                cariRepository.save(new Cari("OF SÜT 2", "", "", 1));
                cariRepository.save(new Cari("PEYNİRCİM", "", "", 1));
                cariRepository.save(new Cari("SAĞLAM", "", "", 1));
                cariRepository.save(new Cari("SOMUNCU 1", "", "", 1));
                cariRepository.save(new Cari("SOMUNCU 2", "", "", 1));
                cariRepository.save(new Cari("TATLAR", "", "", 1));
                cariRepository.save(new Cari("TUNA", "", "", 1));
                cariRepository.save(new Cari("ÜNLÜ", "", "", 1));

                LOGGER.info("{} Cari kaydı eklendi.", cariRepository.findAll().size());

            }
        };
    }
}
