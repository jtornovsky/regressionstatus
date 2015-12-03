package com.regressionstatus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/showCurrentStatus")
public class RegressionStatusController {

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) {
		// working url: http://localhost:8080/regressionstatus/hello/index		
		model.addAttribute("message", "mvc white page1");
		return "hello";	// jsp page to be returned and displayed
	}
	
	private final String statusPageTitle = "Status";
	private final String statusPageHeader = "Regression Status";
	
	@RequestMapping(value = "/index1", method = RequestMethod.GET)
	public String index1(Model model) {
		// working url: http://localhost:8080/regressionstatus/hello/index1 
		model.addAttribute("statusPageTitle", statusPageTitle);
		model.addAttribute("statusPageHeader", statusPageHeader);
		return "hello1";	// jsp page to be returned and displayed
	}
}
