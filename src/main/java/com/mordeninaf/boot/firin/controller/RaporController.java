package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.*;
import com.mordeninaf.boot.firin.service.*;
import com.mordeninaf.boot.firin.util.DateUtils;
import com.mordeninaf.boot.firin.util.Parameters;
import com.mordeninaf.boot.firin.util.TextUtils;
import com.mordeninaf.boot.firin.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        String baslangicTarihi = LocalDate.now(ZoneId.systemDefault()).toString();
        String bitisTarihi = LocalDate.now(ZoneId.systemDefault()).plusDays(1).toString();

        Page<Tahsilat> tahsilatPagingList = null;
        List<Tahsilat> tahsilatList = new ArrayList<>();

        Page<Siparis> siparisPagingList = null;
        List<Siparis> siparisList = new ArrayList<>();

        int numberOfPages;

        if (tip.equals(Type.T.name())) {
            tahsilatPagingList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(0, baslangicTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            tahsilatList = new ArrayList<>(tahsilatPagingList.toList());
            numberOfPages = tahsilatPagingList.getTotalPages() > 0 ? tahsilatPagingList.getTotalPages() : 1;
        } else {
            siparisPagingList = siparisService.findAllByCariIdAndTarihBetween(0, baslangicTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            siparisList = new ArrayList<>(siparisPagingList.toList());
            numberOfPages = siparisPagingList.getTotalPages() > 0 ? siparisPagingList.getTotalPages() : 1;
        }

        List<Integer> totalPages = IntStream.rangeClosed(0, numberOfPages-1).boxed().collect(Collectors.toList());

        List<Cari> cariList = cariService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toMap(Cari::getId, Function.identity()));

        model.addAttribute("tahsilat", null);
        model.addAttribute("tahsilatObj", new Tahsilat());
        model.addAttribute("tahsilatList", tahsilatList);
        model.addAttribute("siparisList", siparisList);
        model.addAttribute("cariList", cariList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("tip", tip);
        model.addAttribute("cariId", 0);
        model.addAttribute("baslangicTarihi", baslangicTarihi);
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
                       @RequestParam(name = "baslangicTarihi", defaultValue = "1923-10-29") String baslangicTarihi,
                       @RequestParam(name = "bitisTarihi", defaultValue = "1923-10-29") String bitisTarihi,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {

        baslangicTarihi = baslangicTarihi.equals(Parameters.KURULUS_TARIHI)? LocalDate.now(ZoneId.systemDefault()).toString() : baslangicTarihi;
        bitisTarihi = bitisTarihi.equals(Parameters.KURULUS_TARIHI)? LocalDate.now(ZoneId.systemDefault()).toString() : bitisTarihi;

        Page<Tahsilat> tahsilatPagingList = null;
        List<Tahsilat> tahsilatList = new ArrayList<>();

        Page<Siparis> siparisPagingList = null;
        List<Siparis> siparisList = new ArrayList<>();

        int numberOfPages;

        if (tip.equals(Type.T.name())) {
            tahsilatPagingList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(cariId, baslangicTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            tahsilatList = new ArrayList<>(tahsilatPagingList.toList());
            numberOfPages = tahsilatPagingList.getTotalPages() > 0 ? tahsilatPagingList.getTotalPages() : 1;
        } else {
            siparisPagingList = siparisService.findAllByCariIdAndTarihBetween(cariId, baslangicTarihi, bitisTarihi, pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
            siparisList = new ArrayList<>(siparisPagingList.toList());
            numberOfPages = siparisPagingList.getTotalPages() > 0 ? siparisPagingList.getTotalPages() : 1;
        }

        List<Integer> totalPages = IntStream.rangeClosed(0, numberOfPages-1).boxed().collect(Collectors.toList());

        List<Cari> cariList = cariService.findAll();
        List<Urun> urunList = urunService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toMap(Cari::getId, Function.identity()));
        Map<Integer, Urun> urunMap = urunList.stream().collect(Collectors.toMap(Urun::getId, Function.identity()));

        model.addAttribute("tahsilatList", tahsilatList);
        model.addAttribute("siparisList", siparisList);
        model.addAttribute("cariList", cariList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("urunMap", urunMap);
        model.addAttribute("tip", tip);
        model.addAttribute("cariId", cariId);
        model.addAttribute("baslangicTarihi", baslangicTarihi);
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
                        @RequestParam(name = "tarih", defaultValue = "1923-10-29") String basTarihi,
                        @RequestParam(name = "bitis", defaultValue = "1923-10-29") String bitisTarihi,
                        @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        return sorgu(model, tip, cariId, basTarihi, bitisTarihi, pageNo);
    }

    @GetMapping("/rapor/excel")
    public void download(HttpServletResponse response,
                         @RequestParam(name = "tip", defaultValue = "T") String tip,
                         @RequestParam(name = "cariId", defaultValue = "0") Integer cariId,
                         @RequestParam(name = "tarih", defaultValue = "1923-10-29") String baslangicTarihi,
                         @RequestParam(name = "bitis", defaultValue = "1923-10-29") String bitisTarihi) throws IOException {

        List<Tahsilat> tahsilatList = new ArrayList<>();
        List<Siparis> siparisList = new ArrayList<>();

        if (tip.equals(Type.T.name())) {
            tahsilatList = tahsilatService.findAllByCariIdAndTarihOdemeBetween(cariId, baslangicTarihi, bitisTarihi);
        } else {
            siparisList = siparisService.findAllByCariIdAndTarihBetween(cariId, baslangicTarihi, bitisTarihi);
        }

        List<Cari> cariList = cariService.findAll();
        List<Urun> urunList = urunService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toMap(Cari::getId, Function.identity()));
        Map<Integer, Urun> urunMap = urunList.stream().collect(Collectors.toMap(Urun::getId, Function.identity()));

        List<Rapor> raporList = mergeTahsilatSiparisCari(tahsilatList, siparisList, cariMap, urunMap);

        String cariAd = (cariId == 0) ? "tum" : cariMap.get(cariId).getCariAd();
        ExcelGenerator generator = new ExcelGenerator(raporList, tip.equals(Type.T.name()) ? Type.T : Type.S, cariAd, baslangicTarihi, bitisTarihi);
        generator.generate(response);
    }

    private List<Rapor> mergeTahsilatCari(List<Tahsilat> tahsilatList, Map<Integer, Cari> cariMap) {
        List<Rapor> raporList = new ArrayList<>();
        for (Tahsilat tahsilat : tahsilatList) {
            Rapor rapor = new Rapor();
            rapor.setTutar(tahsilat.getTutar().doubleValue());
            rapor.setOdemeTarihi(tahsilat.getTarihOdeme());
            rapor.setKayitTarihi(DateUtils.toTurkishDateTime(tahsilat.getTarih()));
            rapor.setCariAd(cariMap.get(tahsilat.getCariId()).getCariAd());
            raporList.add(rapor);
        }
        return raporList;
    }

    private List<Rapor> mergeTahsilatSiparisCari(List<Tahsilat> tahsilatList, List<Siparis> siparisList, Map<Integer, Cari> cariMap, Map<Integer, Urun> urunMap) {
        List<Rapor> raporList = new ArrayList<>();
        for (Tahsilat tahsilat : tahsilatList) {
            Rapor rapor = new Rapor();
            rapor.setTutar(tahsilat.getTutar().doubleValue());
            rapor.setOdemeTarihi(tahsilat.getTarihOdeme());
            rapor.setKayitTarihi(DateUtils.toTurkishDateTime(tahsilat.getTarih()));
            rapor.setCariAd(cariMap.get(tahsilat.getCariId()).getCariAd());
            raporList.add(rapor);
        }
        for (Siparis siparis : siparisList) {
            Rapor rapor = new Rapor();
            rapor.setTutar(siparis.getTutar());
            rapor.setKayitTarihi(DateUtils.toTurkishDateTime(siparis.getTarih()));
            rapor.setCariAd(cariMap.get(siparis.getCariId()).getCariAd());
            rapor.setUrunAd(urunMap.get(siparis.getUrunId()).getUrunAd());
            rapor.setAdet(siparis.getAdet());
            rapor.setSatisIade(siparis.getSatisIade() == 1 ? "SATIŞ": "İADE");
            raporList.add(rapor);
        }
        return raporList;
    }

    private synchronized void mahsuplasSiparisTahsilat(Map<Integer, Double> cariOnayliSiparisMap, Map<Integer, Double> cariTahsilatMap) {
        for(Map.Entry<Integer, Double> entry : cariOnayliSiparisMap.entrySet()) {
            Double tahsilatTutar = cariTahsilatMap.get(entry.getKey());
            if (tahsilatTutar != null) {
                entry.setValue(entry.getValue() - tahsilatTutar);
            }
        }
        /* Tahsilatta var Siparis de yok ise
         * Tahsilat tutarinin Siparise Ekle (Negatif olarak) */
        for(Map.Entry<Integer, Double> entry : cariTahsilatMap.entrySet()) {
            cariOnayliSiparisMap.computeIfAbsent(entry.getKey(), k -> entry.getValue() * -1);
        }
    }

    private Double getSiparisTutar (Siparis siparis) {
        /* Satis is Arti Iade ise Eksi */
        if (siparis.getSatisIade() == 0)
            return -1 * siparis.getTutar();
        else
            return siparis.getTutar();
    }
}
