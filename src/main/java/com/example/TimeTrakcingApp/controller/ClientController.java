package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.entity.Client;
import com.example.TimeTrakcingApp.repository.ClientRepository;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import com.example.TimeTrakcingApp.services.DateConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DateConversionService dateConversionService;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/list")
    public String listAllClients(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allClients", clientRepository.findAll());
        return "admin/clients/listClients";
    }

    @GetMapping("/add")
    public String addClient(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("client", new Client());
        return "admin/clients/addClient";
    }

    @PostMapping("/submit")
    public ModelAndView submitClient(@RequestParam("expirationDate") String expirationDate, @Valid Client client, BindingResult bindingResult) {
        System.out.println(dateConversionService.getExpirationDateFormatted(expirationDate));
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/addClient");
        } else {
            client.setExpirationDate(dateConversionService.getExpirationDateFormatted(expirationDate));
            clientRepository.save(client);
            return new ModelAndView("redirect:/admin/clients/list");
        }
    }

    @GetMapping("/edit/{clientId}")
    public String editClient(@PathVariable(name = "clientId") Long clientId, Model model) {
        getLoggedInInfo(model);
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        if (optionalClient.isPresent()) {
            model.addAttribute("client", optionalClient.get());
        } else {
            model.addAttribute("employee", "Error!");
            model.addAttribute("errorMessage", "Employee with id " + clientId + " does not exist.");
        }
        return "admin/clients/editClient";
    }

    @PostMapping("/update")
    public ModelAndView updateClient(@Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/editClient/{clientId}");
        }
        clientRepository.save(client);
        return new ModelAndView("redirect:/admin/clients/list");
    }

    @GetMapping("delete/{clientId}")
    public ModelAndView deleteClient(@PathVariable(name = "clientId") Long clientId) {
        Optional<Client> optionalClient = clientRepository.findById(clientId);
        if (optionalClient.isPresent()) {
            clientRepository.delete(optionalClient.get());
        }
        return new ModelAndView("redirect:/admin/clients/list");
    }
}
