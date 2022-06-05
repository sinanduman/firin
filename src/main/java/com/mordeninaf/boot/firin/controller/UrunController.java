package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.Urun;
import com.mordeninaf.boot.firin.service.UrunService;
import com.mordeninaf.boot.firin.util.DateUtils;
import com.mordeninaf.boot.firin.util.TextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
public class UrunController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrunController.class);

    @Autowired
    private UrunService urunService;

    @GetMapping("/urun")
    public String main(Model model) {
        List<Urun> urunList = urunService.findAll();
        model.addAttribute("urun", null);
        model.addAttribute("urunObj", new Urun());
        model.addAttribute("urunlist", urunList);
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "urun";
    }

    @PostMapping(value = "/urun/show")
    public String show(Model model, @RequestParam(name = "id") Integer id) {
        Urun urun = urunService.findById(id);
        List<Urun> urunList = urunService.findAll();
        model.addAttribute("urun", urun);
        model.addAttribute("urunlist", urunList);
        model.addAttribute("urunObj", new Urun());
        model.addAttribute("dateUtils", DateUtils.class);
        model.addAttribute("textUtils", TextUtils.class);
        return "urun";
    }

    @PostMapping("/urun")
    public String save(Model model, RedirectAttributes redirAttrs, @ModelAttribute Urun urunObj) {
        if (urunObj != null && !urunObj.getUrunAd().trim().isEmpty()) {
            if (urunObj.getId() != null) {
                Urun urun = urunService.findById(urunObj.getId());
                urunObj.setTarih(urun.getTarih() != null ? urun.getTarih() : LocalDateTime.now(ZoneId.systemDefault()));

                urunService.save(urunObj);
                LOGGER.info("{} - ÜRÜN kaydı güncellendi... ESKI:{}, YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), urun, urunObj);
            } else {
                urunObj.setTarih(LocalDateTime.now(ZoneId.systemDefault()));
                urunService.save(urunObj);
                LOGGER.info("{} - ÜRÜN kaydı eklendi... YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), urunObj);
            }
        }
        redirAttrs.addFlashAttribute("success", "Kayıt Başarılı.");
        return "redirect:/urun";
    }

    @PostMapping(value = "/urun/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirAttrs) {
        String result = "OK";
        if (!StringUtils.isNumeric(id)) {
            LOGGER.info("{} ID'li Ürün kaydı bulunamadı.", id);
            result = "NOK";
        } else {
            Urun urun = urunService.findById(Integer.parseInt(id));
            if (urun == null) {
                result = "NOK";
            } else {
                LOGGER.info("{} - ÜRÜN kaydı silindi... {}", LocalDateTime.now(ZoneId.systemDefault()), urun);
                urunService.remove(urun);
            }
        }
        if (result.equalsIgnoreCase("OK")) {
            redirAttrs.addFlashAttribute("success", "İşlem Başarılı.");
        } else {
            redirAttrs.addFlashAttribute("error", "İşlem Başarısız.");
        }
        return "redirect:/urun/";
    }

    @GetMapping("/urun/cancel")
    public String cancel(Model model, RedirectAttributes redirAttrs) {
        return "redirect:/urun";
    }
}
