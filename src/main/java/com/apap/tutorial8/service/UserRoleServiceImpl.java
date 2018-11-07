package com.apap.tutorial8.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apap.tutorial8.model.UserRoleModel;
import com.apap.tutorial8.repository.UserRoleDb;

@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	@Autowired
	private UserRoleDb userDb;
	
	@Override
	public UserRoleModel addUser(UserRoleModel user) {
		String pass = encrypt(user.getPassword());
		user.setPassword(pass);
		return userDb.save(user);
	}

	@Override
	public String encrypt(String password) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		return hashedPassword;
	}

	@Override
	public void updateUser(String newPassword, UserRoleModel user) {
		String newEncryptedPassword = encrypt(newPassword);
		user.setPassword(newEncryptedPassword);
		userDb.save(user);
	}

	@Override
	public UserRoleModel getUserFromUsername(String username) {
		return userDb.findByUsername(username);
	}

	@Override
	public boolean validateRequirements(String password) {
		char[] pass = password.toCharArray();
		int alfabet = 0;
		int digit = 0;
		for (char character : pass) {
			if (Character.isLetter(character))
				alfabet++;
			else if (Character.isDigit(character))
				digit++;;
		}
		return (((alfabet + digit) >= 8) && (alfabet > 0) && (digit > 0)) ? true : false;
	}
	
}
