package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.model.Cari;
import com.mordeninaf.boot.firin.model.Siparis;
import com.mordeninaf.boot.firin.model.Urun;
import com.mordeninaf.boot.firin.service.CariService;
import com.mordeninaf.boot.firin.service.SiparisService;
import com.mordeninaf.boot.firin.service.UrunService;
import com.mordeninaf.boot.firin.util.DateUtils;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class UrunController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrunController.class);

    private final UrunService urunService;
    private final SiparisService siparisService;
    private final CariService cariService;

    @GetMapping("/urun")
    public String main(Model model) {
        return show(model, 0, 0);
    }

    @RequestMapping(value = "/urun/show", method = {RequestMethod.GET, RequestMethod.POST})
    public String show(Model model,
                       @RequestParam(name = "id") Integer id,
                       @RequestParam(name = "cariId") Integer cariId) {

        Urun urun = (id == 0) ? null : urunService.findById(id);
        List<Urun> urunList = urunService.findAll();
        Map<Integer, Cari> cariMap = cariService.findAll().stream().filter(c -> c.getAktif() == 1).collect(Collectors.toConcurrentMap(Cari::getId, Function.identity()));
        model.addAttribute("urun", urun);
        model.addAttribute("urunlist", urunList);
        model.addAttribute("cariId", cariId);
        model.addAttribute("cariMap", cariMap);
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
                LOGGER.info("{} - ??R??N kayd?? g??ncellendi... ESKI:{}, YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), urun, urunObj);
            } else {
                urunObj.setTarih(LocalDateTime.now(ZoneId.systemDefault()));
                urunService.save(urunObj);
                LOGGER.info("{} - ??R??N kayd?? eklendi... YENI: {}", LocalDateTime.now(ZoneId.systemDefault()), urunObj);
            }
        }
        if (urunObj != null) {
            redirAttrs.addFlashAttribute("success", "????lem Ba??ar??l??.");
            redirAttrs.addFlashAttribute("registered", urunObj.getId());
        } else {
            redirAttrs.addFlashAttribute("error", "????lem Ba??ar??s??z.");
        }
        return "redirect:/urun";
    }

    @PostMapping(value = "/urun/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirAttrs) {
        String result = "OK";
        if (!StringUtils.isNumeric(id)) {
            LOGGER.info("{} ID'li ??r??n kayd?? bulunamad??.", id);
            result = "NOK";
        } else {
            Urun urun = urunService.findById(Integer.parseInt(id));
            if (urun == null) {
                result = "NOK";
            } else {
                List<Siparis> urunSiparis = siparisService.findAllByUrunId(urun.getId());
                if (urunSiparis.isEmpty()) {
                    urunService.remove(urun);
                    LOGGER.info("{} - ??R??N kayd?? silindi... {}", LocalDateTime.now(ZoneId.systemDefault()), urun);
                } else {
                    result = "Sipari?? kayd?? bulundu??undan silinemedi!";
                }
            }
        }
        if (result.equalsIgnoreCase("OK")) {
            redirAttrs.addFlashAttribute("success", "????lem Ba??ar??l??.");
        } else {
            redirAttrs.addFlashAttribute("error", result);
        }
        return "redirect:/urun/";
    }

    @GetMapping("/urun/cancel")
    public String cancel(Model model, RedirectAttributes redirAttrs) {
        return "redirect:/urun";
    }
}
