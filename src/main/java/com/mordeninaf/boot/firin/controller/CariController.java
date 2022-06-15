package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.model.Siparis;
import com.mordeninaf.boot.firin.model.Tahsilat;
import com.mordeninaf.boot.firin.service.CariService;
import com.mordeninaf.boot.firin.service.SiparisService;
import com.mordeninaf.boot.firin.service.TahsilatService;
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
public class CariController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CariController.class);

    @Autowired
    private CariService cariService;

    @Autowired
    private SiparisService siparisService;

    @Autowired
    private TahsilatService tahsilatService;

    @GetMapping("/cari")
    public String main(Model model) {
        List<Cari> cariList = cariService.findAll();
        model.addAttribute("cari", null);
        model.addAttribute("cariObj", new Cari());
        model.addAttribute("carilist", cariList);
        model.addAttribute("textUtils", TextUtils.class);
        return "cari";
    }

    @RequestMapping(value = "/cari/show", method = {RequestMethod.GET, RequestMethod.POST})
    public String show(Model model,
                       @RequestParam(name = "id") Integer id) {
        Cari cari = cariService.findById(id);
        List<Cari> cariList = cariService.findAll();
        model.addAttribute("cari", cari);
        model.addAttribute("carilist", cariList);
        model.addAttribute("cariObj", new Cari());
        model.addAttribute("textUtils", TextUtils.class);
        return "cari";
    }

    @PostMapping("/cari")
    public String save(Model model, RedirectAttributes redirAttrs, @ModelAttribute Cari cariObj) {
        if (cariObj != null && !cariObj.getCariAd().trim().isEmpty()) {
            if (cariObj.getId() != null) {
                Cari cari = cariService.findById(cariObj.getId());
                cariObj.setTarih(cari.getTarih() != null ? cari.getTarih() : LocalDateTime.now(ZoneId.systemDefault()));

                cariService.save(cariObj);
                LOGGER.info("{} - CARİ kayıt güncellendi... ESKI:{}, YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), cari, cariObj);
            } else {
                cariObj.setTarih(LocalDateTime.now(ZoneId.systemDefault()));
                cariService.save(cariObj);
                LOGGER.info("{} - CARİ kayıt eklendi... YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), cariObj);
            }
        }
        if (cariObj != null) {
            redirAttrs.addFlashAttribute("success", "İşlem Başarılı.");
            redirAttrs.addFlashAttribute("registered", cariObj.getId());
        } else {
            redirAttrs.addFlashAttribute("error", "İşlem Başarısız.");
        }
        return "redirect:/cari";
    }

    @PostMapping(value = "/cari/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirAttrs) {
        String result = "OK";
        if (!StringUtils.isNumeric(id)) {
            LOGGER.info("{} ID'li Cari kayıt bulunamadı.", id);
            result = "NOK";
        } else {
            Cari cari = cariService.findById(Integer.parseInt(id));
            if (cari == null) {
                result = "NOK";
            } else {
                List<Siparis> cariSiparis = siparisService.findAllByCariId(cari.getId());
                List<Tahsilat> tahsilatSiparis = tahsilatService.findAllByCariId(cari.getId());
                if (cariSiparis.isEmpty() && tahsilatSiparis.isEmpty()) {
                    cariService.remove(cari);
                    LOGGER.info("{} - CARİ kayıt silindi... {}", LocalDateTime.now(ZoneId.systemDefault()), cari);
                } else {
                    result = "Sipariş ve Tahsilat kaydı bulunduğundan silinemedi!";
                }
            }
        }
        if (result.equalsIgnoreCase("OK")) {
            redirAttrs.addFlashAttribute("success", "İşlem Başarılı.");
        } else {
            redirAttrs.addFlashAttribute("error", result);
        }
        return "redirect:/cari/";
    }

    @GetMapping("/cari/cancel")
    public String cancel(Model model, RedirectAttributes redirAttrs, @ModelAttribute Cari cariObj) {
        return "redirect:/cari";
    }
}
