package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.dto.UserRegisterDto;
import com.example.TimeTrakcingApp.entity.Admin;
import com.example.TimeTrakcingApp.enums.Role;
import com.example.TimeTrakcingApp.repository.AdminRepository;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import com.example.TimeTrakcingApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("admin/admins")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/list")
    public String listAllAdmin(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allAdmins", adminRepository.findAll());
        return "admin/admins/list";
    }

    @GetMapping("/add")
    public String addAdmin(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("registerDto", new UserRegisterDto());
        model.addAttribute("roleTypes", Role.values());
        return "admin/admins/add";
    }

    @PostMapping("/submit")
    public ModelAndView submitAdmin(
            @ModelAttribute("registerDto")
            @Valid UserRegisterDto registerDto,
            BindingResult bindingResult)
            throws MessagingException, IOException
    {
        if(userService.ifUserExists(registerDto.getUsername())){
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/admins/add");
        } else {
            registerDto.setRole(Role.ADMIN);
            userService.createUser(registerDto);
            return new ModelAndView("redirect:/admin/admins/list");
        }
    }

    @GetMapping("/edit/{adminId}")
    public String editAdmin(@PathVariable(name = "adminId") Long adminId, Model model) {
        getLoggedInInfo(model);
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        model.addAttribute("roleTypes", Role.values());
        if (optionalAdmin.isPresent()) {
            model.addAttribute("admin", optionalAdmin.get());
        } else {
            model.addAttribute("employee", "Error!");
            model.addAttribute("errorMessage", "Employee with id " + adminId + " does not exist.");
        }
        return "admin/admins/edit";
    }

    @PostMapping("/update")
    public ModelAndView updateAdmin(@Valid Admin admin, BindingResult bindingResult) {
        if(userService.ifUserExists(admin.getUsername())){
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/admins/edit/{adminId}");
        }
        adminRepository.save(admin);
        return new ModelAndView("redirect:/admin/admins/list");
    }

    @GetMapping("delete/{adminId}")
    public ModelAndView deleteAdmin(@PathVariable(name = "adminId") Long adminId) {
        Optional<Admin> optionalAdmin = adminRepository.findById(adminId);
        if (optionalAdmin.isPresent()) {
            adminRepository.delete(optionalAdmin.get());
            return new ModelAndView("redirect:/admin/admins/list");
        } else {
            return new ModelAndView("redirect:/admin/admins/list");
        }
    }
}
