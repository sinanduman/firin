package com.mordeninaf.boot.firin;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.model.Urun;
import com.mordeninaf.boot.firin.repository.CariRepository;
import com.mordeninaf.boot.firin.repository.UrunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class FirinApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(FirinApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FirinApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadUrunData(UrunRepository urunRepository) {
        return args -> {
            List<Urun> urunList = urunRepository.findAll();
            if (urunList.isEmpty()) {
                urunRepository.save(new Urun("KÜÇÜK TAVA"));
                urunRepository.save(new Urun("BÜYÜK TAVA"));
                urunRepository.save(new Urun("TRABZON"));
                urunRepository.save(new Urun("BALİNA"));
                urunRepository.save(new Urun("KARA BUĞDAY"));
                urunRepository.save(new Urun("MISIR"));
                urunRepository.save(new Urun("TAHİNLİ"));
                urunRepository.save(new Urun("POĞAÇA"));
                urunRepository.save(new Urun("__DEVIR__"));

                LOGGER.info("{} Ürün kaydı eklendi.", urunRepository.findAll().size());

            }
        };
    }

    @Bean
    public CommandLineRunner loadCariData(CariRepository cariRepository) {
        return args -> {
            List<Cari> cariList = cariRepository.findAll();
            if (cariList.isEmpty()) {
                cariRepository.save(new Cari("ADİL", "", ""));
                cariRepository.save(new Cari("ASLANOĞLU", "", ""));
                cariRepository.save(new Cari("AYDIN", "", ""));
                cariRepository.save(new Cari("BAHAR", "", ""));
                cariRepository.save(new Cari("BAŞTÜRK", "", ""));
                cariRepository.save(new Cari("BEREKET", "", ""));
                cariRepository.save(new Cari("BESLEN", "", ""));
                cariRepository.save(new Cari("BEYDAĞI", "", ""));
                cariRepository.save(new Cari("BİZİM", "", ""));
                cariRepository.save(new Cari("DOĞAL TÜKKAN", "", ""));
                cariRepository.save(new Cari("DOĞUŞ GIDA", "", ""));
                cariRepository.save(new Cari("HANBEY", "", ""));
                cariRepository.save(new Cari("HAS PEYNİR", "", ""));
                cariRepository.save(new Cari("K. KAHV.", "", ""));
                cariRepository.save(new Cari("KAHVALTI D", "", ""));
                cariRepository.save(new Cari("KÖYÜM", "", ""));
                cariRepository.save(new Cari("LİMJORA", "", ""));
                cariRepository.save(new Cari("MARAŞ", "", ""));
                cariRepository.save(new Cari("NEFİS", "", ""));
                cariRepository.save(new Cari("NUR", "", ""));
                cariRepository.save(new Cari("OF PEYNİR", "", ""));
                cariRepository.save(new Cari("OF SÜT 1", "", ""));
                cariRepository.save(new Cari("OF SÜT 2", "", ""));
                cariRepository.save(new Cari("PEYNİRCİM", "", ""));
                cariRepository.save(new Cari("SAĞLAM", "", ""));
                cariRepository.save(new Cari("SOMUNCU 1", "", ""));
                cariRepository.save(new Cari("SOMUNCU 2", "", ""));
                cariRepository.save(new Cari("TATLAR", "", ""));
                cariRepository.save(new Cari("TUNA", "", ""));
                cariRepository.save(new Cari("ÜNLÜ", "", ""));

                LOGGER.info("{} Cari kaydı eklendi.", cariRepository.findAll().size());

            }
        };
    }
}
