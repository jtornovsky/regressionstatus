package com.regressionstatus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.regressionstatus.data.RegressionStatusDataUpdater;


@Controller
@RequestMapping("/showCurrentStatus")
public class RegressionStatusController {
	
	@Autowired
	@Qualifier("regressionStatusDataUpdaterImpl")
	RegressionStatusDataUpdater regressionStatusUpdater;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model) {
		// working url: http://localhost:8080/regressionstatus/hello/index		
		model.addAttribute("message", "mvc white page1");
		return "hello";	// jsp page to be returned and displayed
	}
	
	private final String statusPageTitle = "Status";
	private final String statusPageHeader = "Regression Status";
	
	@RequestMapping(value = "/rstatus", method = RequestMethod.GET)
	public String index1(Model model) {
		// working url: http://localhost:8080/regressionstatus/hello/index1
		model.addAttribute("statusPageTitle", statusPageTitle);
		model.addAttribute("statusPageHeader", statusPageHeader);
		regressionStatusUpdater.fetchStatusData();
//		regressionStatusUpdater.fillStatusData();
		model.addAttribute("dataMap", regressionStatusUpdater.getOverallSetupsCurrentStatusMap());
		return "rstatuspage";	// jsp page to be returned and displayed
	}
}
