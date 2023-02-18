package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.entity.Employee;
import com.example.TimeTrakcingApp.enums.Role;
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
@RequestMapping("/admin/employees")
public class AdminController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AuthenticationService authenticationService;


    private void getLoggedInInfo(Model model) {
        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/list")
    public String listAllEmployees(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allEmployees",employeeRepository.findAll());
        return "admin/employees/listEmployees";
    }

    @GetMapping("/add")
    public String addResort(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("employee",new Employee());
        model.addAttribute("roleTypes", Role.values());
        return "admin/employees/addEmployee";
    }
    @PostMapping("/submit")
    public ModelAndView submitResort(@Valid Employee employee, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/employees/addEmployee");
        }else{
            employee.setRole(Role.EMPLOYEE);
            employeeRepository.save(employee);
            return new ModelAndView("redirect:/admin/employees/list");
        }
    }
    @GetMapping("/edit/{employeeId}")
    public String editUser(@PathVariable(name = "employeeId") Long employeeId, Model model) {
        getLoggedInInfo(model);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        model.addAttribute("roleTypes", Role.values());
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee",optionalEmployee.get());
        }
        else {
            model.addAttribute("employee","Error!");
            model.addAttribute("errorMessage","Employee with id " + employeeId +" does not exist.");
        }
        return "admin/employees/editEmployee";
    }
    @PostMapping("/update")
    public ModelAndView updateResort(@Valid Employee employee,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/employees/editEmployee/{employeeId}");
        }
        employeeRepository.save(employee);
        return new ModelAndView("redirect:/admin/employees/list");
    }
    @GetMapping("delete/{userId}")
    public ModelAndView deleteResort(@PathVariable(name = "userId") Long userId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(userId);
        if (optionalEmployee.isPresent()) {
            employeeRepository.delete(optionalEmployee.get());
            return new ModelAndView("redirect:/admin/employees/list");
        }else {
            return new ModelAndView("redirect:/admin/employees/list");
        }
    }
}
