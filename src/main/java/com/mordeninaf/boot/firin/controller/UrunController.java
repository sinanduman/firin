package com.mordeninaf.boot.firin.controller;

import com.mordeninaf.boot.firin.entity.Urun;
import com.mordeninaf.boot.firin.service.UrunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UrunController {

    @Autowired
    private UrunService urunService;

    @GetMapping("/urun")
    public String main(Model model) {
        List<Urun> urunList = urunService.findAll();
        model.addAttribute("urun", null);
        model.addAttribute("urunObj", new Urun());
        model.addAttribute("urunlist", urunList);
        model.addAttribute("firstName", "Sinan");
        return "urun";
    }

    @GetMapping(value = "/urun/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Urun urun = urunService.findById(id);
        //: System.out.println(urun);
        List<Urun> urunList = urunService.findAll();
        model.addAttribute("urun", urun);
        model.addAttribute("urunlist", urunList);
        model.addAttribute("urunObj", new Urun());
        model.addAttribute("firstName", "Sinan");
        return "urun";
    }

    @PostMapping("/urun")
    public String save(Model model, RedirectAttributes redirAttrs, @ModelAttribute Urun urunObj) {
        if (urunObj != null && !urunObj.getUrunAd().trim().isEmpty()) {
            urunService.save(urunObj);
        }
        redirAttrs.addFlashAttribute("success", "Kayıt Başarılı.");
        return "redirect:/urun";
    }
}
