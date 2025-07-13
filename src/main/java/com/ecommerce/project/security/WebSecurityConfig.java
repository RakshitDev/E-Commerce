package com.ecommerce.project.security;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Roles;
import com.ecommerce.project.model.User;
import com.ecommerce.project.repositories.RolesRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.secrity.jwt.AuthEntryPoint;
import com.ecommerce.project.secrity.jwt.AuthTokenFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class WebSecurityConfig {
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	AuthEntryPoint unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationTokenFilter() {
		return new AuthTokenFilter();
	}
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider  authprovider=new DaoAuthenticationProvider();
		authprovider.setUserDetailsService(userDetailsService);
		authprovider.setPasswordEncoder(passwordEncoder());
		return authprovider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/signin").permitAll()
	            .requestMatchers("/api/auth/signup").permitAll()
	            .requestMatchers("/v3/api-docs/**").permitAll()
	            .requestMatchers("/swagger-ui/**").permitAll()
	            .requestMatchers("/swagger-ui.html").permitAll()
	            .requestMatchers("/api/public/**").permitAll()   // ✅ if needed
	            .requestMatchers("/api/admin/**").permitAll()
	            .requestMatchers("/api/test/**").permitAll()
	            .requestMatchers("/image/**").permitAll()
	            .anyRequest().authenticated()
	    );

	    http.sessionManagement(session ->
	        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    );

	    http.exceptionHandling(exception ->
	        exception.authenticationEntryPoint(unauthorizedHandler)
	    );

	    http.csrf(AbstractHttpConfigurer::disable);

	    http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return web -> web.ignoring().requestMatchers(
	        "/v2/api-docs",
	        "/configuration/ui",
	        "/swagger-resources/**",
	        "/swagger-ui.html",
	        "/webjars/**"
	    );
	}

	 @Bean
	    public CommandLineRunner initData(RolesRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        return args -> {
	            // Retrieve or create roles
	            Roles userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
	                    .orElseGet(() -> {
	                        Roles newUserRole = new Roles(AppRole.ROLE_USER);
	                        return roleRepository.save(newUserRole);
	                    });

	            Roles sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
	                    .orElseGet(() -> {
	                        Roles newSellerRole = new Roles(AppRole.ROLE_SELLER);
	                        return roleRepository.save(newSellerRole);
	                    });

	            Roles adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
	                    .orElseGet(() -> {
	                        Roles newAdminRole = new Roles(AppRole.ROLE_ADMIN);
	                        return roleRepository.save(newAdminRole);
	                    });

	            Set<Roles> userRoles = Set.of(userRole);
	            Set<Roles> sellerRoles = Set.of(sellerRole);
	            Set<Roles> adminRoles = Set.of(userRole, sellerRole, adminRole);


	            // Create users if not already present
	            if (!userRepository.existsByUserName("user1")) {
	                User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
	                userRepository.save(user1);
	            }

	            if (!userRepository.existsByUserName("seller1")) {
	                User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
	                userRepository.save(seller1);
	            }

	            if (!userRepository.existsByUserName("admin")) {
	                User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
	                userRepository.save(admin);
	            }

	            // Update roles for existing users
	            userRepository.findByUserName("user1").ifPresent(user -> {
	                user.setRoles(userRoles);
	                userRepository.save(user);
	            });

	            userRepository.findByUserName("seller1").ifPresent(seller -> {
	                seller.setRoles(sellerRoles);
	                userRepository.save(seller);
	            });

	            userRepository.findByUserName("admin").ifPresent(admin -> {
	                admin.setRoles(adminRoles);
	                userRepository.save(admin);
	            });
	        };
	    }

	

}
