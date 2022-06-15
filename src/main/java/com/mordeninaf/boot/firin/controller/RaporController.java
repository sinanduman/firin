package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.*;
import com.mordeninaf.boot.firin.service.*;
import com.mordeninaf.boot.firin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class RaporController {
    @Autowired
    private TahsilatService tahsilatService;
    @Autowired
    private CariService cariService;
    @Autowired
    private SiparisService siparisService;
    @Autowired
    private UrunService urunService;

    @GetMapping("/rapor")
    public String main(Model model,
                       @RequestParam(name = "tip", defaultValue = "T") String tip,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {

        String basTarihi = LocalDate.now(ZoneId.systemDefault()).toString();
        String bitisTarihi = LocalDate.now(ZoneId.systemDefault()).plusDays(1).toString();

        Page<Tahsilat> tahsilatPagingList = null;
        List<Tahsilat> tahsilatList = new ArrayList<>();

        Page<Siparis> siparisPagingList = null;
        List<Siparis> siparisList = new ArrayList<>();

        List<Borc> borcList = new ArrayList<>();
        List<Cari> cariList = cariService.findAll();
        List<Urun> urunList = urunService.findAll();

        int numberOfPages = 1;

        if (tip.equals(Type.T.name())) {
            /* TAHSİLAT */
            tahsilatPagingList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(0, basTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            tahsilatList = new ArrayList<>(tahsilatPagingList.toList());
            numberOfPages = tahsilatPagingList.getTotalPages() > 0 ? tahsilatPagingList.getTotalPages() : 1;
        } else if (tip.equals(Type.S.name())) {
            /* SİPARİŞ */
            siparisPagingList = siparisService.findAllByCariIdAndUrunIdAndTarihBetween(0, 0, basTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            siparisList = new ArrayList<>(siparisPagingList.toList());
            numberOfPages = siparisPagingList.getTotalPages() > 0 ? siparisPagingList.getTotalPages() : 1;
        } else if (tip.equals(Type.B.name())){
            /* BORÇ */
            List<Siparis> borcSiparisList;
            List<Tahsilat> borcTahsilatList;
            /* cariId = 0*/
            borcSiparisList = siparisService.findAllByTarihBetween(basTarihi, bitisTarihi);
            borcTahsilatList = tahsilatService.findAllByTarihOdemeBetween(basTarihi, bitisTarihi);
            /* CariId -> Toplam */
            Map<Integer, Double> siparisMapByCariId = borcSiparisList.stream().collect(Collectors.groupingByConcurrent(Siparis::getCariId, Collectors.reducing(0d, Siparis::getTutar, Double::sum)));
            /* CariId -> Toplam */
            Map<Integer, Integer> tahsilatMapByCariId = borcTahsilatList.stream().collect(Collectors.groupingByConcurrent(Tahsilat::getCariId, Collectors.reducing(0, Tahsilat::getTutar, Integer::sum)));

            for (Map.Entry<Integer, Double> entry : siparisMapByCariId.entrySet()) {
                if (tahsilatMapByCariId.get(entry.getKey()) != null) {
                    siparisMapByCariId.put(entry.getKey(), entry.getValue() - tahsilatMapByCariId.get(entry.getKey()).doubleValue());
                }
            }

            numberOfPages = siparisMapByCariId.size() > 0 ? siparisMapByCariId.size() : 1;
        }

        List<Integer> totalPages = NumberUtils.getTotalPages(numberOfPages);

        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toConcurrentMap(Cari::getId, Function.identity()));

        model.addAttribute("tahsilat", null);
        model.addAttribute("tahsilatObj", new Tahsilat());
        model.addAttribute("tahsilatList", tahsilatList);
        model.addAttribute("siparisList", siparisList);
        model.addAttribute("cariList", cariList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("urunList", urunList);
        model.addAttribute("borcList", borcList);
        model.addAttribute("tip", tip);
        model.addAttribute("cariId", 0);
        model.addAttribute("urunId", 0);
        model.addAttribute("basTarihi", basTarihi);
        model.addAttribute("bitisTarihi", bitisTarihi);
        model.addAttribute("pageSize", Parameters.PAGE_SIZE);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "rapor";
    }

    @PostMapping(value = "/rapor")
    public String sorgu(Model model,
                       @RequestParam(name = "tip", defaultValue = "T") String tip,
                       @RequestParam(name = "cariId", defaultValue = "0") Integer cariId,
                       @RequestParam(name = "urunId", defaultValue = "0") Integer urunId,
                       @RequestParam(name = "basTarihi", defaultValue = "1923-10-29") String basTarihi,
                       @RequestParam(name = "bitisTarihi", defaultValue = "1923-10-29") String bitisTarihi,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {

        basTarihi = basTarihi.equals(Parameters.KURULUS_TARIHI)? LocalDate.now(ZoneId.systemDefault()).toString() : basTarihi;
        bitisTarihi = bitisTarihi.equals(Parameters.KURULUS_TARIHI)? LocalDate.now(ZoneId.systemDefault()).toString() : bitisTarihi;

        Page<Tahsilat> tahsilatPagingList = null;
        List<Tahsilat> tahsilatList = new ArrayList<>();

        Page<Siparis> siparisPagingList = null;
        List<Siparis> siparisList = new ArrayList<>();

        List<Borc> borcList = new ArrayList<>();
        List<Cari> cariList = cariService.findAll();
        List<Urun> urunList = urunService.findAll();

        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toConcurrentMap(Cari::getId, Function.identity()));
        Map<Integer, Urun> urunMap = urunList.stream().collect(Collectors.toConcurrentMap(Urun::getId, Function.identity()));

        int numberOfPages = 1;

        if (tip.equals(Type.T.name())) {
            /* TAHSİLAT */
            tahsilatPagingList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(cariId, basTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            tahsilatList = new ArrayList<>(tahsilatPagingList.toList());
            numberOfPages = tahsilatPagingList.getTotalPages() > 0 ? tahsilatPagingList.getTotalPages() : 1;
        } else if (tip.equals(Type.S.name())) {
            /* SİPARİŞ */
            siparisPagingList = siparisService.findAllByCariIdAndUrunIdAndTarihBetween(cariId, urunId, basTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            siparisList = new ArrayList<>(siparisPagingList.toList());
            numberOfPages = siparisPagingList.getTotalPages() > 0 ? siparisPagingList.getTotalPages() : 1;
        } else if (tip.equals(Type.B.name())){
            /* BORÇ */
            List<Siparis> borcSiparisList;
            List<Tahsilat> borcTahsilatList;
            if (cariId == 0) {
                borcSiparisList = siparisService.findAllByTarihBetween(basTarihi, bitisTarihi);
                borcTahsilatList = tahsilatService.findAllByTarihOdemeBetween(basTarihi, bitisTarihi);
            } else {
                borcSiparisList = siparisService.findAllByCariIdAndTarihBetween(cariId, basTarihi, bitisTarihi);
                borcTahsilatList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(cariId, basTarihi, bitisTarihi);
            }
            /* CariId -> Toplam */
            Map<Integer, Double> siparisMapByCariId = borcSiparisList.stream().collect(Collectors.groupingByConcurrent(Siparis::getCariId, Collectors.reducing(0d, Siparis::getTutar, Double::sum)));
            /* CariId -> Toplam */
            Map<Integer, Integer> tahsilatMapByCariId = borcTahsilatList.stream().collect(Collectors.groupingByConcurrent(Tahsilat::getCariId, Collectors.reducing(0, Tahsilat::getTutar, Integer::sum)));

            borcList = new ArrayList<>();
            for (Map.Entry<Integer, Double> entry : siparisMapByCariId.entrySet()) {
                if (tahsilatMapByCariId.get(entry.getKey()) != null) {
                    Integer value = tahsilatMapByCariId.get(entry.getKey());
                    siparisMapByCariId.put(entry.getKey(), entry.getValue() - value);
                }
                borcList.add(new Borc(entry.getKey(), cariMap.get(entry.getKey()).getCariAd(), siparisMapByCariId.get(entry.getKey()), LocalDate.parse(basTarihi, DateTimeFormatter.ISO_LOCAL_DATE)));
            }
            borcList.sort(Comparator.comparing(Borc::getCariAd));

            /* Tüm Tahsilat tek sayfada
             * numberOfPages = 1;
             * */
        }
        List<Integer> totalPages = NumberUtils.getTotalPages(numberOfPages);

        model.addAttribute("tahsilatList", tahsilatList);
        model.addAttribute("siparisList", siparisList);
        model.addAttribute("cariList", cariList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("urunMap", urunMap);
        model.addAttribute("urunList", urunList);
        model.addAttribute("borcList", borcList);
        model.addAttribute("tip", tip);
        model.addAttribute("cariId", cariId);
        model.addAttribute("urunId", urunId);
        model.addAttribute("basTarihi", basTarihi);
        model.addAttribute("bitisTarihi", bitisTarihi);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", Parameters.PAGE_SIZE);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "rapor";
    }

    @GetMapping(value = "/rapor/show")
    public String show(Model model,
                        @RequestParam(name = "tip", defaultValue = "T") String tip,
                        @RequestParam(name = "cariId", defaultValue = "0") Integer cariId,
                        @RequestParam(name = "urunId", defaultValue = "0") Integer urunId,
                        @RequestParam(name = "tarih", defaultValue = "1923-10-29") String basTarihi,
                        @RequestParam(name = "bitis", defaultValue = "1923-10-29") String bitisTarihi,
                        @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        return sorgu(model, tip, cariId, urunId, basTarihi, bitisTarihi, pageNo);
    }

    @GetMapping("/rapor/excel")
    public void download(HttpServletResponse response,
                         @RequestParam(name = "tip", defaultValue = "T") String tip,
                         @RequestParam(name = "cariId", defaultValue = "0") Integer cariId,
                         @RequestParam(name = "urunId", defaultValue = "0") Integer urunId,
                         @RequestParam(name = "tarih", defaultValue = "1923-10-29") String basTarihi,
                         @RequestParam(name = "bitis", defaultValue = "1923-10-29") String bitisTarihi) throws IOException {

        List<Tahsilat> tahsilatList = new ArrayList<>();
        List<Siparis> siparisList = new ArrayList<>();

        /* CariId -> Toplam */
        Map<Integer, Double> siparisMapByCariId = new ConcurrentHashMap<>();
        /* CariId -> Toplam */
        Map<Integer, Integer> tahsilatMapByCariId = new ConcurrentHashMap<>();


        if (tip.equals(Type.T.name())) {
            tahsilatList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(cariId, basTarihi, bitisTarihi);
        } else if (tip.equals(Type.S.name())) {
            siparisList = siparisService.findAllByCariIdAndTarihBetween(cariId, basTarihi, bitisTarihi);
        } else if (tip.equals(Type.B.name())) {
            List<Siparis> borcSiparisList;
            List<Tahsilat> borcTahsilatList;
            if (cariId == 0) {
                borcSiparisList = siparisService.findAllByTarihBetween(basTarihi, bitisTarihi);
                borcTahsilatList = tahsilatService.findAllByTarihOdemeBetween(basTarihi, bitisTarihi);
            } else {
                borcSiparisList = siparisService.findAllByCariIdAndTarihBetween(cariId, basTarihi, bitisTarihi);
                borcTahsilatList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(cariId, basTarihi, bitisTarihi);
            }
            siparisMapByCariId = borcSiparisList.stream().collect(Collectors.groupingByConcurrent(Siparis::getCariId, Collectors.reducing(0d, Siparis::getTutar, Double::sum)));
            tahsilatMapByCariId = borcTahsilatList.stream().collect(Collectors.groupingByConcurrent(Tahsilat::getCariId, Collectors.reducing(0, Tahsilat::getTutar, Integer::sum)));

            for (Map.Entry<Integer, Double> entry : siparisMapByCariId.entrySet()) {
                if (tahsilatMapByCariId.get(entry.getKey()) != null) {
                    siparisMapByCariId.put(entry.getKey(), entry.getValue() - tahsilatMapByCariId.get(entry.getKey()).doubleValue());
                }
            }
        }

        List<Cari> cariList = cariService.findAll();
        List<Urun> urunList = urunService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toConcurrentMap(Cari::getId, Function.identity()));
        Map<Integer, Urun> urunMap = urunList.stream().collect(Collectors.toConcurrentMap(Urun::getId, Function.identity()));

        List<Rapor> raporList = mergeTahsilatSiparisCariBorc(tahsilatList, siparisList, siparisMapByCariId, cariMap, urunMap, basTarihi, bitisTarihi);

        String cariAd = (cariId == 0) ? "tum" : cariMap.get(cariId).getCariAd();
        Type raporTipi = tip.equals("T") ? Type.T : tip.equals("S") ? Type.S : Type.B;
        ExcelGenerator generator = new ExcelGenerator(raporList, raporTipi, cariAd, basTarihi, bitisTarihi);
        generator.generate(response);
    }

    private List<Rapor> mergeTahsilatSiparisCariBorc(List<Tahsilat> tahsilatList, List<Siparis> siparisList, Map<Integer, Double> siparisMapByCariId, Map<Integer, Cari> cariMap, Map<Integer, Urun> urunMap, String basTarihi, String bitisTarihi) {
        List<Rapor> raporList = new ArrayList<>();
        for (Tahsilat tahsilat : tahsilatList) {
            Rapor rapor = new Rapor();
            rapor.setTutar(tahsilat.getTutar().doubleValue());
            rapor.setOdemeTarihi(DateUtils.toTurkishDateFromIso(tahsilat.getTarihOdeme()));
            rapor.setKayitTarihi(DateUtils.toTurkishDateTime(tahsilat.getTarih()));
            rapor.setCariAd(cariMap.get(tahsilat.getCariId()).getCariAd());
            raporList.add(rapor);
        }
        for (Siparis siparis : siparisList) {
            Rapor rapor = new Rapor();
            rapor.setTutar(siparis.getTutar());
            rapor.setKayitTarihi(DateUtils.toTurkishDateFromIso(siparis.getTarihSiparis()));
            rapor.setCariAd(cariMap.get(siparis.getCariId()).getCariAd());
            rapor.setUrunAd(urunMap.get(siparis.getUrunId()).getUrunAd());
            rapor.setAdet(siparis.getAdet());
            rapor.setSatisIade(siparis.getSatisIade() == 1 ? "SATIŞ": "İADE");
            raporList.add(rapor);
        }
        for (Map.Entry<Integer, Double> entry : siparisMapByCariId.entrySet()) {
            Rapor rapor = new Rapor();
            rapor.setCariId(entry.getKey());
            rapor.setCariAd(cariMap.get(entry.getKey()).getCariAd());
            rapor.setTutar(entry.getValue());
            rapor.setKayitTarihi(DateUtils.toTurkishDateFromIso(basTarihi));
            raporList.add(rapor);
        }
        return raporList;
    }
}
