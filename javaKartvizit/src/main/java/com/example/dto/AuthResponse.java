package com.example.dto;

public class AuthResponse {
    private String message;
    private boolean success;
    private String token;
    private UserInfo userInfo;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    public AuthResponse(String message, boolean success, String token, UserInfo userInfo) {
        this.message = message;
        this.success = success;
        this.token = token;
        this.userInfo = userInfo;
    }
    
    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public UserInfo getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }
    
    public static class UserInfo {
        private Integer id;
        private String username;
        private String email;
        
        public UserInfo() {}
        
        public UserInfo(Integer id, String username, String email) {
            this.id = id;
            this.username = username;
            this.email = email;
        }
        
        // Getters and Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}