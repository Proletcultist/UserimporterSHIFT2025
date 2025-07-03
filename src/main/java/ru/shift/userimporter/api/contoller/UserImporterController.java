package ru.shift.userimporter.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class UserImporterController{
	
	@RequestMapping("/ping")
	public static String ping(){
		return "Pong";
	}

}
