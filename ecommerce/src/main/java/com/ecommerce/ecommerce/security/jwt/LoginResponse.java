package com.ecommerce.ecommerce.security.jwt;

import java.util.List;

public class LoginResponse {

    private Long id;
    private String jwtToken;
    private String userName;
    private List<String> roles;

    public String getJwtToken() {
        return jwtToken;
    }

    public LoginResponse(Long id,String jwtToken, String userName, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.userName = userName;
        this.roles = roles;
    }

    public void setId(){}
    public Long getId(){
        return id;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
