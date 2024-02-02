package com.luv2code.springboot.cruddemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	
	EmployeeController(EmployeeService theemployeeService){
		employeeService=theemployeeService;
	}
	
	@GetMapping("/list")
	public String listOfEmployee(Model model) {
		
		return findPaginated(1, model);
		
	}
	
	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model model) {
		//create model attribute to bind form data 
		Employee theemployee= new Employee();
		
		model.addAttribute("employee", theemployee);
		
		return "employee/employee-form";
	}
	
	@PostMapping("/save")
	public String formSave(@ModelAttribute("employee") Employee theEmployee) {
		//save this in db
		employeeService.save(theEmployee);
		
		//redirect
		return "redirect:/employee/list";
	}
	@GetMapping("/showFormForUpdate")
	public String DoUpdate(@RequestParam("employeeId") int theId, Model model) {
		
		// get employee from calling service class and so on.
		Employee theEmployee=employeeService.findById(theId);
		
		//set employee to model to prepolulate the form
		model.addAttribute("employee", theEmployee);
		
		// send that over
		
		return "employee/employee-form";
	}
	
	@GetMapping("/showFormForDelete")
	public String DoDelete(@RequestParam("employeeId") int theId) {
		
		employeeService.deleteById(theId);
		
		return "redirect:/employee/list";
	}
	
	@GetMapping("page/{pageNo}")
	public String findPaginated(@PathVariable(value="pageNo") int pageNo,Model model) {
		int pageSize=5;
		
		Page<Employee> page=employeeService.findPaginatedPage(pageNo, pageSize);
		
		List<Employee> listEmployees=page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listEmployees", listEmployees);
		
		return "employee/list";
		
	}
	
}
