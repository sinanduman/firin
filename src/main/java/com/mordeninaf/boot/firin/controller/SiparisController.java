package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.model.Siparis;
import com.mordeninaf.boot.firin.model.Urun;
import com.mordeninaf.boot.firin.service.CariService;
import com.mordeninaf.boot.firin.service.SiparisService;
import com.mordeninaf.boot.firin.service.UrunService;
import com.mordeninaf.boot.firin.util.DateUtils;
import com.mordeninaf.boot.firin.util.Parameters;
import com.mordeninaf.boot.firin.util.TextUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RequiredArgsConstructor
@Controller
public class SiparisController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiparisController.class);
    private final SiparisService siparisService;
    private final CariService cariService;
    private final UrunService urunService;

    @GetMapping("/siparis")
    public String main(Model model,
                       @RequestParam(name = "id", defaultValue = "0") Integer id,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        return show(model, id, pageNo);
    }

    @PostMapping(value = "/siparis/show")
    public String show(Model model,
                       @RequestParam(name = "id", defaultValue = "0") Integer id,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        Siparis siparis = (id == 0) ? null : siparisService.findById(id);

        Page<Siparis> siparisPagingList = siparisService.findAll(pageNo, Parameters.PAGE_SIZE, Parameters.SIPARIS_SORT_BY);
        List<Siparis> siparisList = new ArrayList<>(siparisPagingList.toList());
        int numberOfPages = siparisPagingList.getTotalPages() > 0 ? siparisPagingList.getTotalPages() : 1;

        List<Integer> totalPages = IntStream.rangeClosed(0, numberOfPages-1)
                .boxed()
                .collect(Collectors.toList());

        List<Cari> cariList = cariService.findAll();
        List<Urun> urunList = urunService.findAll();
        Map<Integer, Cari> cariMap = cariList.stream().collect(Collectors.toConcurrentMap(Cari::getId, Function.identity()));
        Map<Integer, Urun> urunMap = urunList.stream().collect(Collectors.toConcurrentMap(Urun::getId, Function.identity()));
        model.addAttribute("siparis", siparis);
        model.addAttribute("siparisObj", new Siparis());
        model.addAttribute("siparisList", siparisList);
        model.addAttribute("cariList", cariList);
        model.addAttribute("urunList", urunList);
        model.addAttribute("cariMap", cariMap);
        model.addAttribute("urunMap", urunMap);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", Parameters.PAGE_SIZE);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "siparis";
    }

    @PostMapping("/siparis")
    public String save(Model model, RedirectAttributes redirAttrs,
                       @ModelAttribute Siparis siparisObj,
                       @RequestParam(name = "page", defaultValue = "0") Integer pageNo) {
        if (siparisObj != null && !(siparisObj.getCariId() == null || siparisObj.getUrunId() == null)) {
            if (siparisObj.getId() != null) {
                Siparis siparis = siparisService.findById(siparisObj.getId());
                siparisObj.setTarih(siparis.getTarih() != null ? siparis.getTarih() : LocalDateTime.now(ZoneId.systemDefault()));

                siparisService.save(siparisObj);
                LOGGER.info("{} - SİPARİŞ kaydı güncellendi... ESKI:{}, YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), siparis, siparisObj);
            } else {
                siparisObj.setTarih(LocalDateTime.now(ZoneId.systemDefault()));
                siparisService.save(siparisObj);
                LOGGER.info("{} - SİPARİŞ kaydı eklendi... YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), siparisObj);
            }
        }
        if (siparisObj != null) {
            redirAttrs.addFlashAttribute("success", "İşlem Başarılı.");
            redirAttrs.addFlashAttribute("registered", siparisObj.getId());
        } else {
            redirAttrs.addFlashAttribute("error", "İşlem Başarısız.");
        }
        return "redirect:/siparis/?page=" + pageNo;
    }

    @PostMapping(value = "/siparis/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirAttrs) {
        String result = "OK";
        if (!StringUtils.isNumeric(id)) {
            LOGGER.info("{} ID'li Sipariş kaydı bulunamadı.", id);
            result = "NOK";
        } else {
            Siparis siparis = siparisService.findById(Integer.parseInt(id));
            if (siparis == null) {
                result = "NOK";
            } else {
                LOGGER.info("{} - SİPARİŞ kaydı silindi... {}", LocalDateTime.now(ZoneId.systemDefault()), siparis);
                siparisService.remove(siparis);
            }
        }
        if (result.equalsIgnoreCase("OK")) {
            redirAttrs.addFlashAttribute("success", "İşlem Başarılı.");
        } else {
            redirAttrs.addFlashAttribute("error", "İşlem Başarısız.");
        }
        return "redirect:/siparis/";
    }

    @GetMapping("/siparis/cancel")
    public String cancel(Model model, RedirectAttributes redirAttrs, @ModelAttribute Siparis siparisObj) {
        return "redirect:/siparis";
    }
}
