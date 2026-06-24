package com.makersacademy.audiojakh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class HomeController {
	@RequestMapping(value = "/home")
	public RedirectView index() {
		return new RedirectView("/posts");
	}
}
