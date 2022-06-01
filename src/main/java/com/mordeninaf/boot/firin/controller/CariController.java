package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.entity.Cari;
import com.mordeninaf.boot.firin.service.CariService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CariController {

    @Autowired
    private CariService cariService;

    @GetMapping("/cari")
    public String main(Model model) {
        List<Cari> cariList = cariService.findAll();
        model.addAttribute("cari", null);
        model.addAttribute("cariObj", new Cari());
        model.addAttribute("carilist", cariList);
        return "cari";
    }

    @GetMapping(value = "/cari/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Cari cari = cariService.findById(id);
        //: System.out.println(cari);
        List<Cari> cariList = cariService.findAll();
        model.addAttribute("cari", cari);
        model.addAttribute("carilist", cariList);
        model.addAttribute("cariObj", new Cari());
        return "cari";
    }

    @PostMapping("/cari")
    public String save(Model model, RedirectAttributes redirAttrs, @ModelAttribute Cari cariObj) {
        if (cariObj != null && !cariObj.getCariAd().trim().isEmpty()) {
            cariService.save(cariObj);
        }
        redirAttrs.addFlashAttribute("success", "Kayıt Başarılı.");
        return "redirect:/cari";
    }

    @PostMapping("/cari/onay")
    public String cariOnay(@RequestBody Object object, Model model, RedirectAttributes redirAttrs) {
        System.out.println("Object: " + object);
        /*
        if (cariObj != null && !cariObj.getCariAd().trim().isEmpty()) {
            cariService.save(cariObj);
        }
        redirAttrs.addFlashAttribute("success", "Kayıt Başarılı.");
        */
        return "redirect:/cari";
    }

    @GetMapping("/cari/cancel")
    public String cancel(Model model, RedirectAttributes redirAttrs, @ModelAttribute Cari cariObj) {
        return "redirect:/cari";
    }
}
