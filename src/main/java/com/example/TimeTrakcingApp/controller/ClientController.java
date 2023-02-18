package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.entity.Client;
import com.example.TimeTrakcingApp.entity.Employee;
import com.example.TimeTrakcingApp.enums.Role;
import com.example.TimeTrakcingApp.repository.ClientRepository;
import com.example.TimeTrakcingApp.repository.EmployeeRepository;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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


    private void getLoggedInInfo(Model model) {
        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/list")
    public String listAllEmployees(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allClients",clientRepository.findAll());
        return "admin/clients/listClients";
    }

    @GetMapping("/add")
    public String addResort(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("client",new Client());
        return "admin/clients/addClient";
    }
    @PostMapping("/submit")
    public ModelAndView submitResort(@Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/addClient");
        }else{

            clientRepository.save(client);
            return new ModelAndView("redirect:/admin/clients/list");
        }
    }
    @GetMapping("/edit/{clientId}")
    public String editUser(@PathVariable(name = "clientId") Long clientId, Model model) {
        getLoggedInInfo(model);
        Optional<Client> optionalClient = clientRepository.findById(clientId);

        if (optionalClient.isPresent()) {
            model.addAttribute("client",optionalClient.get());
        }
        else {
            model.addAttribute("employee","Error!");
            model.addAttribute("errorMessage","Employee with id " + clientId +" does not exist.");
        }
        return "admin/clients/editClient";
    }
    @PostMapping("/update")
    public ModelAndView updateResort(@Valid Client client,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/clients/editClient/{clientId}");
        }
        clientRepository.save(client);
        return new ModelAndView("redirect:/admin/clients/list");
    }
    @GetMapping("delete/{clientId}")
    public ModelAndView deleteResort(@PathVariable(name = "clientId") Long userId) {
        Optional<Client> optionalClient = clientRepository.findById(userId);
        if (optionalClient.isPresent()) {
            clientRepository.delete(optionalClient.get());
            return new ModelAndView("redirect:/admin/clients/list");
        }else {
            return new ModelAndView("redirect:/admin/clients/list");
        }
    }
}
