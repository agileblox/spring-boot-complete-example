package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SecurityController {
		
	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user, Model model) {
		if (user != null) {
			return "redirect:/titulos";
		}
		
		return "Login";
	}
	
	
	
	

}