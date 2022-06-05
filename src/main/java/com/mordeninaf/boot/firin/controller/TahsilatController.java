package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.model.Siparis;
import com.mordeninaf.boot.firin.model.Tahsilat;
import com.mordeninaf.boot.firin.service.CariService;
import com.mordeninaf.boot.firin.service.SiparisService;
import com.mordeninaf.boot.firin.service.TahsilatService;
import com.mordeninaf.boot.firin.util.DateUtils;
import com.mordeninaf.boot.firin.util.Parameters;
import com.mordeninaf.boot.firin.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TahsilatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TahsilatController.class);
    @Autowired
    private TahsilatService tahsilatService;
    @Autowired
    private CariService cariService;
    @Autowired
    private SiparisService siparisService;

    @GetMapping("/tahsilat")
    public String main(Model model,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {

        Page<Tahsilat> tahsilatPagingList = tahsilatService.findAll(pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
        List<Tahsilat> tahsilatList = new ArrayList<>(tahsilatPagingList.toList());
        List<Tahsilat> tahsilatListAll = tahsilatService.findAll();
        int numberOfPages = tahsilatPagingList.getTotalPages() > 0 ? tahsilatPagingList.getTotalPages() : 1;

        List<Integer> totalPages = IntStream.rangeClosed(0, numberOfPages-1).boxed().collect(Collectors.toList());

        List<Cari> cariList = cariService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toMap(Cari::getId, Function.identity()));

        List<Siparis> onayliSiparisList = siparisService.findAllByOnayIs(1);
        Map<Integer, Double> cariOnayliSiparisMap =  onayliSiparisList.stream().collect(Collectors.groupingBy(Siparis::getCariId, Collectors.summingDouble(this::getSiparisTutar)));
        Map<Integer, Double> cariTahsilatMap = tahsilatListAll.stream().collect(Collectors.groupingBy(Tahsilat::getCariId, Collectors.summingDouble(Tahsilat::getTutar)));

        mahsuplasSiparisTahsilat(cariOnayliSiparisMap, cariTahsilatMap);

        model.addAttribute("tahsilat", null);
        model.addAttribute("tahsilatObj", new Tahsilat());
        model.addAttribute("tahsilatList", tahsilatList);
        model.addAttribute("cariOnayliSiparisMap", cariOnayliSiparisMap);
        model.addAttribute("cariList", cariList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", Parameters.PAGE_SIZE);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "tahsilat";
    }

    @PostMapping(value = "/tahsilat/show")
    public String show(Model model,
                       @RequestParam(name = "id") Integer id,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        Tahsilat tahsilat = tahsilatService.findById(id);

        Page<Tahsilat> tahsilatPagingList = tahsilatService.findAll(pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
        List<Tahsilat> tahsilatList = new ArrayList<>(tahsilatPagingList.toList());
        List<Tahsilat> tahsilatListAll = tahsilatService.findAll();
        int numberOfPages = tahsilatPagingList.getTotalPages() > 0 ? tahsilatPagingList.getTotalPages() : 1;

        List<Integer> totalPages = IntStream.rangeClosed(0, numberOfPages-1).boxed().collect(Collectors.toList());

        List<Cari> cariList = cariService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toMap(Cari::getId, Function.identity()));

        List<Siparis> onayliSiparisList = siparisService.findAllByOnayIs(1);
        Map<Integer, Double> cariOnayliSiparisMap =  onayliSiparisList.stream().collect(Collectors.groupingBy(Siparis::getCariId, Collectors.summingDouble(this::getSiparisTutar)));
        Map<Integer, Double> cariTahsilatMap = tahsilatListAll.stream().collect(Collectors.groupingBy(Tahsilat::getCariId, Collectors.summingDouble(Tahsilat::getTutar)));

        mahsuplasSiparisTahsilat(cariOnayliSiparisMap, cariTahsilatMap);

        model.addAttribute("tahsilat", tahsilat);
        model.addAttribute("tahsilatObj", new Tahsilat());
        model.addAttribute("tahsilatList", tahsilatList);
        model.addAttribute("cariOnayliSiparisMap", cariOnayliSiparisMap);
        model.addAttribute("cariList", cariList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", Parameters.PAGE_SIZE);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "tahsilat";
    }

    @PostMapping("/tahsilat")
    public String save(Model model, RedirectAttributes redirAttrs,
                       @ModelAttribute Tahsilat tahsilatObj,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        if (tahsilatObj != null && !(tahsilatObj.getCariId() == null)) {
            if (tahsilatObj.getId() != null) {
                Tahsilat tahsilat = tahsilatService.findById(tahsilatObj.getId());
                tahsilatObj.setTarih(tahsilat.getTarih() != null ? tahsilat.getTarih() : LocalDateTime.now(ZoneId.systemDefault()));

                tahsilatService.save(tahsilatObj);
                LOGGER.info("{} - TAHSİLAT kaydı güncellendi... ESKI:{}, YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), tahsilat, tahsilatObj);
            } else {
                tahsilatObj.setTarih(LocalDateTime.now(ZoneId.systemDefault()));
                tahsilatService.save(tahsilatObj);
                LOGGER.info("{} - TAHSİLAT kaydı eklendi... YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), tahsilatObj);
            }
        }
        if (tahsilatObj != null) {
            redirAttrs.addFlashAttribute("success", "İşlem Başarılı.");
            redirAttrs.addFlashAttribute("registered", tahsilatObj.getId());
        } else {
            redirAttrs.addFlashAttribute("error", "İşlem Başarısız.");
        }
        return "redirect:/tahsilat/?page=" + pageNo;
    }

    @GetMapping("/tahsilat/cancel")
    public String cancel(Model model, RedirectAttributes redirAttrs, @ModelAttribute Tahsilat tahsilatObj) {
        return "redirect:/tahsilat";
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
