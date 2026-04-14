package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.MasterSetting;
import com.example.demo.service.MasterSettingService;

@Controller
public class MasterController {

    private final MasterSettingService masterSettingService;

    public MasterController(MasterSettingService masterSettingService) {
        this.masterSettingService = masterSettingService;
    }

    @GetMapping("/master")
    public String show(Model model) {
        model.addAttribute("master", masterSettingService.getOrCreateSetting());
        return "master";
    }

    @PostMapping("/master/update")
    public String update(@ModelAttribute MasterSetting master) {
        masterSettingService.updateMasterText(master.getMasterText());
        return "redirect:/master?saved=1";
    }
}
