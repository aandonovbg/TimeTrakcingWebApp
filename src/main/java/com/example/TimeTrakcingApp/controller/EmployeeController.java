package com.example.TimeTrakcingApp.controller;

import com.example.TimeTrakcingApp.dto.StatisticsDto;
import com.example.TimeTrakcingApp.dto.UserRegisterDto;
import com.example.TimeTrakcingApp.entity.Employee;
import com.example.TimeTrakcingApp.enums.Role;
import com.example.TimeTrakcingApp.repository.DailyProtocolRepository;
import com.example.TimeTrakcingApp.repository.EmployeeRepository;
import com.example.TimeTrakcingApp.services.AuthenticationService;
import com.example.TimeTrakcingApp.services.Statistics;
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
@RequestMapping("/admin/employees")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DailyProtocolRepository dailyProtocolRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private Statistics statistics;

    private void getLoggedInInfo(Model model) {
        model.addAttribute("whoIsLoggedIn", authenticationService.whoIsLoggedIn());
        model.addAttribute("role", authenticationService.getLoggedInRole());
    }

    @GetMapping("/list")
    public String listAllEmployees(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("allEmployees", employeeRepository.findAll());
        return "admin/employees/listEmployees";
    }

    @GetMapping("/add")
    public String addEmployee(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("registerDto", new UserRegisterDto());
        return "admin/employees/addEmployee";
    }

    @PostMapping("/submit")
    public ModelAndView submitEmployee(
            @ModelAttribute("registerDto")
            @Valid UserRegisterDto registerDto,
            BindingResult bindingResult)
            throws MessagingException, IOException {
        if (userService.ifUserExists(registerDto.getUsername())) {
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/employees/addEmployee");
        } else {

            registerDto.setRole(Role.EMPLOYEE);
            userService.createUser(registerDto);
            return new ModelAndView("redirect:/admin/employees/list");
        }
    }

    @GetMapping("/edit/{employeeId}")
    public String editEmployee(@PathVariable(name = "employeeId") Long employeeId, Model model) {
        getLoggedInInfo(model);
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            model.addAttribute("employee", optionalEmployee.get());
        }

        return "admin/employees/editEmployee";
    }

    @PostMapping("/update")
    public ModelAndView updateEmployee(
            @RequestParam(value = "password") String password,
            @Valid Employee editedEmployee, BindingResult bindingResult) throws MessagingException, IOException {

        if (userService.updateIfUserExists(editedEmployee)) {
            bindingResult.rejectValue("username", "error.username", "Потребителското Име вече е заето. Моля изберете друго.");
            return new ModelAndView("admin/employees/editEmployee");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView("admin/employees/editEmployee/{employeeId}");
        }
        userService.updateUser(editedEmployee, password);
        return new ModelAndView("redirect:/admin/employees/list");
    }

    @GetMapping("delete/{userId}")
    public ModelAndView deleteEmployee(@PathVariable(name = "userId") Long userId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(userId);
        if (optionalEmployee.isPresent()) {
            employeeRepository.delete(optionalEmployee.get());
            return new ModelAndView("redirect:/admin/employees/list");
        } else {
            return new ModelAndView("redirect:/admin/employees/list");
        }
    }

    @GetMapping("/stats")
    public String showStatistics(Model model) {
        getLoggedInInfo(model);
        model.addAttribute("statisticsDto", new StatisticsDto());
        return "admin/employees/statistics";
    }


    @PostMapping("/statsResult")
    public ModelAndView submitStatistics(
            @ModelAttribute("statisticsDto")
            @Valid StatisticsDto statisticsDto,
            BindingResult bindingResult,
            Model model
    ) throws MessagingException, IOException {
        getLoggedInInfo(model);
        String username = null;
        if (bindingResult.hasErrors()) {
            return new ModelAndView("/admin/employees/statistics");
        } else if (statisticsDto.getEmployeeName() != null) { //username is used
            if (statistics.ifEmployeeExists(statisticsDto.getEmployeeName())) { // if user with that username exists
                model.addAttribute("status", username = "present"); //passing status to view
                model.addAttribute("allDailyProtocols", statistics.getEmployeeProtocols(statisticsDto.getEmployeeName()));
                model.addAttribute("fullName", statistics.getEmployeeFullName(statisticsDto.getEmployeeName()));
                model.addAttribute("sumTotalHoursWorked", statistics.sumTotalHoursEmployee(statisticsDto.getEmployeeName()));
                return new ModelAndView("admin/employees/listStatistics");
            } else {
                bindingResult.rejectValue("employeeName", "error.employeeName", "Потребителското Име не е намерено. Моля изберете друго.");
                return new ModelAndView("/admin/employees/statistics");
            }
        } else {
            if (statisticsDto.getWeekNumber() != null) {
                if ((statistics.validateWeekNumber(statisticsDto.getWeekNumber()))) {

                    int weekNumber =Integer.parseInt(statisticsDto.getWeekNumber());
                    model.addAttribute("DailyProtocolsByWeekNumber", statistics.collectProtocolsFromSpecificWeek(statistics.getMondayDate(weekNumber), statistics.getSundayDate(weekNumber)));
                    model.addAttribute("weekNumber",statisticsDto.getWeekNumber());
                    model.addAttribute("sumTotalHours", statistics.sumTotalHours(statistics.getMondayDate(weekNumber), statistics.getSundayDate(weekNumber)));
                }else {
                    bindingResult.rejectValue("weekNumber", "error.weekNumber", "Максеималният брой седмици е "+statistics.getWeekCount());
                    return new ModelAndView("/admin/employees/statistics");
                }
            }
        }
        return new ModelAndView("admin/employees/listStatistics");
    }
}
