package com.ecommerce.project.security.request;

import java.util.Set;

import com.ecommerce.project.model.Roles;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SingnUpRequest {
	
	@NotBlank(message = "name cannot be empty")
	@Size(min = 4,max = 20,message = "Name Must include minimum four characters")
	private String userName;
	@NotBlank(message = "Email cannot be blank")
	@Email
	private String email;
	@NotBlank(message = "Password cannot be empty")
	@Size(min =4,max = 50)
	private String password;
	
	private Set<String> roles;

}
