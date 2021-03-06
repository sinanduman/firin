package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.model.Siparis;
import com.mordeninaf.boot.firin.model.Tahsilat;
import com.mordeninaf.boot.firin.service.CariService;
import com.mordeninaf.boot.firin.service.SiparisService;
import com.mordeninaf.boot.firin.service.TahsilatService;
import com.mordeninaf.boot.firin.util.TextUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CariController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CariController.class);

    private final CariService cariService;
    private final SiparisService siparisService;
    private final TahsilatService tahsilatService;

    @GetMapping("/cari")
    public String main(Model model) {
        return show(model, 0);
    }

    @RequestMapping(value = "/cari/show", method = {RequestMethod.GET, RequestMethod.POST})
    public String show(Model model,
                       @RequestParam(name = "id", defaultValue = "0") Integer id) {
        Cari cari = (id == 0) ? null: cariService.findById(id);
        List<Cari> cariList = cariService.findAll();
        model.addAttribute("cari", cari);
        model.addAttribute("cariList", cariList);
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
                LOGGER.info("{} - CAR?? kay??t g??ncellendi... ESKI:{}, YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), cari, cariObj);
            } else {
                cariObj.setTarih(LocalDateTime.now(ZoneId.systemDefault()));
                cariService.save(cariObj);
                LOGGER.info("{} - CAR?? kay??t eklendi... YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), cariObj);
            }
        }
        if (cariObj != null) {
            redirAttrs.addFlashAttribute("success", "????lem Ba??ar??l??.");
            redirAttrs.addFlashAttribute("registered", cariObj.getId());
        } else {
            redirAttrs.addFlashAttribute("error", "????lem Ba??ar??s??z.");
        }
        return "redirect:/cari";
    }

    @PostMapping(value = "/cari/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirAttrs) {
        String result = "OK";
        if (!StringUtils.isNumeric(id)) {
            LOGGER.info("{} ID'li Cari kay??t bulunamad??.", id);
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
                    LOGGER.info("{} - CAR?? kay??t silindi... {}", LocalDateTime.now(ZoneId.systemDefault()), cari);
                } else {
                    result = "Sipari?? ve Tahsilat kayd?? bulundu??undan silinemedi!";
                }
            }
        }
        if (result.equalsIgnoreCase("OK")) {
            redirAttrs.addFlashAttribute("success", "????lem Ba??ar??l??.");
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
