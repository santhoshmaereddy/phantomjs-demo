package com.test.automation.phantomjsdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.automation.phantomjsdemo.utils.CommonUtils;



@RestController
@RequestMapping(value = DefaultController.PATH)
public class DefaultController {
	
	public static final String PATH = "/api/v1/jobs";
	
	private CommonUtils commonUtils;

	@CrossOrigin
	@GetMapping(value = "/execute")
	ResponseEntity<String> executeTestAutomation(@RequestParam(value = "browser") String browser, @RequestParam("url") String url) {
		
		commonUtils = new CommonUtils();
		String executionStatus = commonUtils.executeTestAutomation(browser, url);
		
		return new ResponseEntity<>(executionStatus, HttpStatus.OK);
	}
	
	

}
