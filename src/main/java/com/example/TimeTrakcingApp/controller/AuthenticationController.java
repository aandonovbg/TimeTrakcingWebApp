package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.entity.DailyProtocol;
import com.example.TimeTrakcingApp.repository.ClientRepository;
import com.example.TimeTrakcingApp.repository.DailyProtocolRepository;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import com.example.TimeTrakcingApp.services.DateConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private DateConversionService dateConversionService;
    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/logout")
    public String logoutPage() {

        return "redirect:/";
    }

    @GetMapping("")
    public String home(Model model) {

        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
        if (authenticationService.getLoggedInRole().equals("EMPLOYEE")) {
            model.addAttribute("allClients", clientRepository.findAll());
            model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
            model.addAttribute("allDailyProtocols", dailyProtocolRepository.getProtocolByProtocolDate(dateConversionService.getCurrentDateFormatted()));

            return "/protocol/list";
        }
        return "/home";
    }


    @PostMapping("/authenticate")
    public String authenticate(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest request
    ) {
        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
            Authentication result = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(result);
            return "redirect:/home";
        } catch (AuthenticationException e) {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
}
