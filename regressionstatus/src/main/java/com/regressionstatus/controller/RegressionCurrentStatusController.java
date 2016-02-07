package com.regressionstatus.controller;


//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.regressionstatus.data.current.CurrentRegressionStatusDataUpdater;
import com.regressionstatus.data.frontendparameters.current.UrlParametersHandler;

/**
 * Class fetches data from overall map with the current regression status
 * and passes it to rstatus.jsp page
 * link to use: 
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus
 * {@link}
 * link to use when providing parameters:
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45&192.168.20.107&192.168.20.245;usedefaultips=true
 * http://localhost:8080/regressionstatus/showCurrentStatus/rstatus/ip=192.168.30.45&192.168.20.107;usedefaultips=false 
 * {@link}  
 * @author jtornovsky
 *
 */
@Controller
@RequestMapping("/showCurrentStatus")
public class RegressionCurrentStatusController {

	// logger still not in use
//	private final static Log logger = LogFactory.getLog(RegressionCurrentStatusController.class);
	
	/**
	 * holds the title of a status page
	 */
	private final String statusPageTitle = "Status";
	
	/**
	 * holds the header of a overall status table
	 */
	private final String statusPageHeader = "Regression Status";
	
	/**
	 * holds the current status web page auto refresh interval 
	 */
	@Value("${regression.current.status.webpage.auto.refresh.interval}")
	private String regressionCurrentStatusWebpageAutoRefreshInterval;
	
	/**
	 * holds the instance of a an data updater to be used to calculate status report for all regression setups 
	 */
	@Autowired
	@Qualifier("currentRegressionStatusDataUpdaterSummaryHtmlReport")
	private CurrentRegressionStatusDataUpdater regressionStatusUpdater;

	@Autowired
	@Qualifier("urlParametersHandlerDao")
	private UrlParametersHandler urlParametersHandler;

	/**
	 * fetches the calculated reports of a current regression status as a map
	 * and passes that map to be shown in the predefined web page
	 * @param model - object to be filled with data to show in the web page
	 * @return - jsp page name to be returned and displayed
	 */
	@RequestMapping(value = "/rstatus", method = RequestMethod.GET)
	public String showCurrentRegressionStatus(String parameters, Model model) {
		model.addAttribute("regressionCurrentStatusWebpageAutoRefreshInterval", regressionCurrentStatusWebpageAutoRefreshInterval);
		model.addAttribute("statusPageTitle", statusPageTitle);
		model.addAttribute("statusPageHeader", statusPageHeader);
		regressionStatusUpdater.fetchStatusData();
		model.addAttribute("dataMap", regressionStatusUpdater.getOverallSetupsCurrentStatusMap());
		return "regCurrStatusPage";	// jsp page to be returned and displayed
	}
	
	/**
	 * Retrieves parameters given in URL 
	 * @param parameters - parameters in URL
	 * @param model - object to be filled with data to show in the web page
	 * @return - jsp page name to be returned and displayed
	 */
	@RequestMapping(value = "/rstatus/{parameters:.+}", method = RequestMethod.GET)
	public String showCurrentRegressionStatusWithParameters(@PathVariable("parameters") String parameters, Model model) {
		urlParametersHandler.fillUrlParametersMap(parameters);
		return showCurrentRegressionStatus(parameters, model);
	}
}
