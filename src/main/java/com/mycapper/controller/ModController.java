package com.mycapper.controller;

import com.mycapper.entity.ModData;
import com.mycapper.service.ModService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/mods")
public class ModController {
    @Autowired
    private ModService modService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("mods", modService.getAllMods());
        return "mods/list"; // 去掉开头的斜杠
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("mod", new ModData());
        return "mods/add";
    }

    @PostMapping("/add")
    public String add(@RequestParam("modName") String modName,
                      @RequestParam("downloadUrl") String downloadUrl) {
        ModData mod = new ModData();
        mod.setModName(modName);
        mod.setDownloadUrl(downloadUrl);
        mod.setCreatedAt(LocalDateTime.now());
        modService.addMod(mod);
        return "redirect:/mods";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        modService.deleteMod(id);
        return "redirect:/mods";
    }
}