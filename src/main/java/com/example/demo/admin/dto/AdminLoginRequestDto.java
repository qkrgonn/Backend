package com.example.demo.admin.dto;

public class AdminLoginRequestDto {
    private Long adminId;
    private String password;

    public AdminLoginRequestDto() {}
    
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
