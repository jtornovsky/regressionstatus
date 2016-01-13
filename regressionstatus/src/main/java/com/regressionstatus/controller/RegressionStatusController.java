package com.regressionstatus.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.regressionstatus.data.RegressionStatusDataUpdater;


@Controller
@RequestMapping("/showCurrentStatus")
public class RegressionStatusController {

	private final static Log logger = LogFactory.getLog(RegressionStatusController.class);
	private final String statusPageTitle = "Status";
	private final String statusPageHeader = "Regression Status";
	
	@Value("${regression.current.status.webpage.auto.refresh.interval}")
	private String regressionCurrentStatusWebpageAutoRefreshInterval;
	
	@Autowired
	@Qualifier("regressionStatusDataUpdaterImpl")
	RegressionStatusDataUpdater regressionStatusUpdater;
	
	// http://localhost:8080/regressionstatus/showCurrentStatus/rstatus
	@RequestMapping(value = "/rstatus", method = RequestMethod.GET)
	public String index1(Model model) {
		model.addAttribute("regressionCurrentStatusWebpageAutoRefreshInterval", regressionCurrentStatusWebpageAutoRefreshInterval);
		model.addAttribute("statusPageTitle", statusPageTitle);
		model.addAttribute("statusPageHeader", statusPageHeader);
		regressionStatusUpdater.fetchStatusData();
		model.addAttribute("dataMap", regressionStatusUpdater.getOverallSetupsCurrentStatusMap());
		return "rstatuspage";	// jsp page to be returned and displayed
	}
}
