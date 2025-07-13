package com.ecommerce.project.secrity.jwt.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.project.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailImpl implements UserDetails {
	private static final long serialVersionId=1l; 
	
	private Long id;
	private String userName;
	private String email;
	@JsonIgnore
	private String password;
	
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailImpl(Long id, String userName, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}
	
	public static UserDetailImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
		.map(role->new SimpleGrantedAuthority(role.getRoleName().name()))
		.collect(Collectors.toList());
		return new UserDetailImpl(user.getUserId(),
				user.getUserName(),
				user.getEmail(),
				user.getPassword(),
				authorities
				);
		
	}
	
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}
	
	@Override
	public boolean isAccountNonExpired() {
	    return true; // or custom logic
	}

	@Override
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	public boolean isEnabled() {
	    return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null ||getClass() != obj.getClass())
			return false;
		UserDetailImpl user = (UserDetailImpl) obj;
		return Objects.equals(id, user.id);
	}

	


	

	

}



	