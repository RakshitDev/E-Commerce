package com.ecommerce.project.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.UserRepository;

@Component
public class AuthUtil {
	@Autowired
	UserRepository userRepository;
	
	public String loggedInEmail() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(authentication.getName())
		.orElseThrow(()-> new UsernameNotFoundException("user not found!"));
		return user.getEmail();
	}
	
	public User loggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(authentication.getName())
		.orElseThrow(()-> new UsernameNotFoundException("user not found!"));
		return user;
		
	}
	public Long loggedInUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(authentication.getName())
		.orElseThrow(()-> new UsernameNotFoundException("user not found!"));
		return user.getUserId();
		
	}

}
