package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.entity.DailyProtocol;
import com.example.TimeTrakcingApp.repository.ClientRepository;
import com.example.TimeTrakcingApp.repository.DailyProtocolRepository;
import com.example.TimeTrakcingApp.repository.EmployeeRepository;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import com.example.TimeTrakcingApp.services.DateConversionService;
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
@RequestMapping("/protocol")
public class ProtocolController {
    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DateConversionService dateConversionService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ClientRepository clientRepository;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/list")
    public String listDailyProtocol(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
        model.addAttribute("allDailyProtocols", dailyProtocolRepository.getProtocolByProtocolDate(dateConversionService.getCurrentDateFormatted()));
        return "protocol/list";
    }

    @GetMapping("/add")
    public String addProtocol(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allClients", clientRepository.findAll());
        model.addAttribute("dateNow", dateConversionService.getCurrentDateFormatted());
        model.addAttribute("dailyProtocol", new DailyProtocol());
        return "/protocol/add";
    }

    @PostMapping("/submit")
    public ModelAndView submitProtocol(@Valid DailyProtocol dailyProtocol, BindingResult bindingResult,Model model) {
        model.addAttribute("allClients", clientRepository.findAll());
        System.out.println(dateConversionService.getCurrentDateFormatted());
        if (bindingResult.hasErrors()) {
            return new ModelAndView("protocol/add");
        } else {
            dailyProtocol.setProtocolDate(dateConversionService.getCurrentDateFormatted());

            dailyProtocol.setEmployee(employeeRepository.getEmployeeByUsername(authenticationService.whoIsLoggedIn()));
            dailyProtocolRepository.save(dailyProtocol);

            return new ModelAndView("redirect:/protocol/list");
        }
    }

    @GetMapping("/edit/{dailyProtocolId}")
    public String editClient(@PathVariable(name = "dailyProtocolId") Long dailyProtocolId, Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allClients", clientRepository.findAll());
        Optional<DailyProtocol> optionalDailyProtocol = dailyProtocolRepository.findById(dailyProtocolId);

        if (optionalDailyProtocol.isPresent()) {
            model.addAttribute("dailyProtocol", optionalDailyProtocol.get());
        } else {
            model.addAttribute("dailyProtocol", "Error!");
            model.addAttribute("errorMessage", "Protocol with id " + dailyProtocolId + " does not exist.");
        }
        return "protocol/edit";
    }

    @PostMapping("/update")
    public ModelAndView updateClient(@Valid DailyProtocol dailyProtocol, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("protocol/edit/{protocolId}");
        }
        dailyProtocolRepository.save(dailyProtocol);
        return new ModelAndView("redirect:/protocol/list");
    }

    @GetMapping("delete/{dailyProtocolId}")
    public ModelAndView deleteClient(@PathVariable(name = "dailyProtocolId") Long dailyProtocolId) {
        Optional<DailyProtocol> optionalDailyProtocol = dailyProtocolRepository.findById(dailyProtocolId);
        if (optionalDailyProtocol.isPresent()) {
            dailyProtocolRepository.delete(optionalDailyProtocol.get());
        }
        return new ModelAndView("redirect:/protocol/list");
    }
}
