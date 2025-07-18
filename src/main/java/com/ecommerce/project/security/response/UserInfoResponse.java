package com.ecommerce.project.security.response;

import java.util.List;

import org.springframework.http.ResponseCookie;

public class UserInfoResponse {
	private Long id;
    private String token;

    private String username;
    private List<String> roles;

    public UserInfoResponse(Long id,String token, List<String> roles, String username) {
        this.id=id;
    	this.token = token;
        this.username = username;
        this.roles = roles;
    }

   

	public UserInfoResponse(Long id, String username, List<String> role) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.username = username;
        this.roles = role;
	}

	public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
