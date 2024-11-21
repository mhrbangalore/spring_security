package com.mohan.spring_security_learn.controller;

import com.mohan.spring_security_learn.dto.ReqRes;
import com.mohan.spring_security_learn.service.OurUsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController

public class UserManagementController {

    @Autowired
    private OurUsersManagementService ourUsersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes registrationRequest){
        return ResponseEntity.ok(ourUsersManagementService.register(registrationRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes loginRequest){
        return ResponseEntity.ok(ourUsersManagementService.login(loginRequest));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refresh(@RequestBody ReqRes refreshReq){
        return ResponseEntity.ok(ourUsersManagementService.refreshToken(refreshReq));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers(){
        return ResponseEntity.ok(ourUsersManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-users/{userId}")
    public ResponseEntity<ReqRes> getUsersById(@PathVariable("userId") Integer userId){
        return ResponseEntity.ok(ourUsersManagementService.getUsersById(userId));
    }

    @PutMapping("/admin/update-user/{userId}")
    public ResponseEntity<ReqRes> updateUser(@RequestBody ReqRes updateUser,
                                             @PathVariable("userId") Integer userId){
        return ResponseEntity.ok(ourUsersManagementService.updateUser(updateUser, userId));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = ourUsersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Integer userId){
        return ResponseEntity.ok(ourUsersManagementService.deleteUser(userId));
    }

    @GetMapping("/auth/health-check")
    public ResponseEntity<String> performHealthCheck(){
        return ResponseEntity.ok("Server is up and running...");
    }

}





































