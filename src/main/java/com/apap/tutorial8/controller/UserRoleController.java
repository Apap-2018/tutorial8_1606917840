package com.apap.tutorial8.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.service.UserRoleService;

@Controller
@RequestMapping("/user")
public class UserRoleController {
	@Autowired
	private UserRoleService userService;
	
	@RequestMapping(value="/addUser", method=RequestMethod.POST)
	private String addUserSubmit(@ModelAttribute UserRoleModel user, Model model) {
		if (userService.validateRequirements(user.getPassword())) {
			userService.addUser(user);
			model.addAttribute("sukses", true);
		} else {
			model.addAttribute("notif", "Tolong password disesuaikan agar sesuai, yakni minimal 8 karakter dan ada alfabet dan nomor");
		}
		return "home";
	}
	
	@RequestMapping(value="/updatePassword/{username}", method=RequestMethod.POST)
	private String updateUserSubmit(@PathVariable("username") String username, String oldPassword, String newPassword, String newPasswordKonf, Model model) {
		UserRoleModel user = userService.getUserFromUsername(username);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		
		// pw lama harus sama
		if (passwordEncoder.matches(oldPassword, user.getPassword())) {
			// pw harus sesuai sm min 8 karakter, ada nomor dan karakter
			if (userService.validateRequirements(newPassword)) {
				// pw yg ditaro harus sama sama yg pw konfirmasi
				if (newPassword.equals(newPasswordKonf)) {
					userService.updateUser(newPassword, user);
					model.addAttribute("sukses", true);
				} else {
					model.addAttribute("notif", "Tolong password baru dan password lama disamakan");
				}
			} else {
				model.addAttribute("notif", "Tolong password baru disesuaikan agar sesuai, yakni minimal 8 karakter dan ada alfabet dan nomor");
				return "update-password";
			}
		} else {
			model.addAttribute("notif", "Password lama tidak benar, mohon masukkan kembali");
		}
		return "update-password";
		
	}
	
	
	@RequestMapping(value="/updatePassword/{username}", method=RequestMethod.GET)
	private String updateUser(@PathVariable("username") String username, Model model) {
		model.addAttribute("username", username);
		return "update-password";
	}
	
}


