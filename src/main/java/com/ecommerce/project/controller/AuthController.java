package com.ecommerce.project.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RolesRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.secrity.jwt.JwtUtils;
import com.ecommerce.project.secrity.jwt.service.UserDetailImpl;
import com.ecommerce.project.security.request.LoginRequest;
import com.ecommerce.project.security.request.SingnUpRequest;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.security.response.UserInfoResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RolesRepository roleRepository;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException exception) {
			Map<String, Object> map = new HashMap<>();
			map.put("message", "Bad Credentials");
			map.put("status", false);
			return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
		List<String> role = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), role,jwtCookie.toString());
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(userInfoResponse);
	}

	@PostMapping(value = "/signup", produces = "application/json")
	public ResponseEntity<?> saveUser(@RequestBody SingnUpRequest request) {
		System.out.println("🔴 Inside signup controller");
		if (userRepository.existsByUserName(request.getUserName())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error:username already taken!!"));
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error:email already taken!!"));
		}
		User user = new User(request.getUserName(), request.getEmail(), encoder.encode(request.getPassword()));
		Set<String> strRole = request.getRoles();
		Set<Roles> roles = new HashSet<>();
		if (strRole == null) {
			Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRole.forEach(role -> {
				switch (role) {
				case "admin":
					Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "seller":
					Roles modRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});

		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User Registered successfully"));
	}

	@GetMapping("/username")
	public String currentUserName(Authentication authentication) {
		if (authentication != null) {
			return authentication.getName();
		} else
			return "";

	}

	@GetMapping("/user")
	public ResponseEntity<?> getUserDetails(Authentication authentication) {
		UserDetailImpl userDetails = (UserDetailImpl) authentication.getPrincipal();

		List<String> role = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), role);
		return ResponseEntity.ok().body(userInfoResponse);
	}

	@PostMapping("/signout")
	public ResponseEntity<?> signoutUser() {
		ResponseCookie cookie = jwtUtils.getCleanCookie();
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(new MessageResponse("You have been loggedOut!"));
	}

}
