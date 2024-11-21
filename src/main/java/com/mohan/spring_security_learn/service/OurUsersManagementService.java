package com.mohan.spring_security_learn.service;

import com.mohan.spring_security_learn.dto.ReqRes;
import com.mohan.spring_security_learn.entity.OurUsers;
import com.mohan.spring_security_learn.repository.OurUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OurUsersManagementService {

    @Autowired
    private OurUsersRepo ourUsersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes register(ReqRes registrationRequest){
        ReqRes response = new ReqRes();
        try {
            OurUsers ourUser = new OurUsers();
            ourUser.setName(registrationRequest.getName());
            ourUser.setCity(registrationRequest.getCity());
            ourUser.setEmail(registrationRequest.getEmail());
            ourUser.setRole(registrationRequest.getRole());
            ourUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            OurUsers savedUser = ourUsersRepo.save(ourUser);

            if(savedUser.getId() > 0){
                response.setOurUsers(savedUser);
                response.setStatusCode(200);
                response.setMessage("User is registered successfully, User ID:  " + savedUser.getId());
            }

        } catch (Exception e){
            response.setMessage("Error encountered while registering the user: " + e.getMessage());
            response.setStatusCode(500);
        }

        return response;
    }

    public ReqRes login(ReqRes loginRequest){
        ReqRes response = new ReqRes();

        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
            OurUsers ourUsers = ourUsersRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
            String jwt = jwtUtils.generateToken(ourUsers);
            String refreshJwt = jwtUtils.generateRefreshToken(new HashMap<>(), ourUsers);
            response.setOurUsers(ourUsers);
            response.setToken(jwt);
            response.setRefreshToken(refreshJwt);
            response.setExpirationTime("24hrs");
            response.setStatusCode(200);
            response.setMessage("Login successful");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Login failed.., " + e.getMessage());
        }

        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest){
        ReqRes response = new ReqRes();
        try{
            String jwt = refreshTokenRequest.getToken();
            String ourEmail = jwtUtils.extractUsername(jwt);
            OurUsers ourUsers = ourUsersRepo.findByEmail(ourEmail).orElseThrow();
            if(jwtUtils.isTokenValid(jwt,ourUsers)){
                String newJwt = jwtUtils.generateToken(ourUsers);
                response.setOurUsers(ourUsers);
                response.setToken(newJwt);
                response.setRefreshToken(jwt);
                response.setExpirationTime("24hrs");

            }
            response.setStatusCode(200);

        } catch (Exception e) {
            response.setMessage("Failure observed with refresh token request: " + e.getMessage());
            response.setStatusCode(500);
        }
        return response;
    }

    public ReqRes getAllUsers(){
        ReqRes response = new ReqRes();
        try{
            List<OurUsers> ourUsersListResult = ourUsersRepo.findAll();
            if(!ourUsersListResult.isEmpty()){
                response.setOurUsersList(ourUsersListResult);
                response.setStatusCode(200);
                response.setMessage("Fetch all users is successful...");
            } else {
                response.setStatusCode(404);
                response.setMessage("There is no users in the database, yet...");
            }
        } catch (Exception e) {
            response.setMessage("Error encountered while getting all users: " + e.getMessage());
            response.setStatusCode(500);
        }
        return response;
    }

    public ReqRes getUsersById(Integer userId){
        ReqRes response = new ReqRes();
        try{
            Optional<OurUsers> ourUser = ourUsersRepo.findById(userId);
            if(ourUser.isPresent()){
                response.setOurUsers(ourUser.get());
                response.setStatusCode(200);
                response.setMessage("User with " + userId + " is fetched successfully...");
            } else{
                response.setStatusCode(404);
                response.setMessage("There is no user with user Id: " + userId);
            }

        } catch (Exception e) {
            response.setMessage("Error encountered while fetching the user id: " + userId +
                    ". Error: " + e.getMessage());
            response.setStatusCode(500);
        }
        return response;
    }

    public ReqRes deleteUser(Integer userId){
        ReqRes response = new ReqRes();
        try{
            Optional<OurUsers> ourUser = ourUsersRepo.findById(userId);
            if(ourUser.isPresent()){
                ourUsersRepo.deleteById(userId);
                response.setStatusCode(200);
                response.setMessage("User " + userId + " is deleted successfully...");
            }else{
                response.setStatusCode(404);
                response.setMessage("There is no user with user Id: " + userId);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error encountered while deleting the user: " + e.getMessage());
        }
        return response;
    }

    public ReqRes updateUser(ReqRes updateUserRequest, Integer userId){
        ReqRes response = new ReqRes();
        try{
            Optional<OurUsers> ourUser = ourUsersRepo.findById(userId);
            if(ourUser.isPresent()){
                OurUsers existingUser = ourUser.get();
                existingUser.setName(updateUserRequest.getName());
                existingUser.setCity(updateUserRequest.getCity());
                existingUser.setRole(updateUserRequest.getRole());
                existingUser.setEmail(updateUserRequest.getEmail());
                if(updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()){
                    existingUser.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
                }
                OurUsers updatedUser = ourUsersRepo.save(existingUser);
                response.setOurUsers(updatedUser);
                response.setStatusCode(200);
                response.setMessage("User " + updatedUser.getEmail() + " is updated successfully...");
            }else{
                response.setStatusCode(404);
                response.setMessage("There is no user with user: " + updateUserRequest.getEmail());
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error encountered while updating the user " + e.getMessage());
        }
        return response;
    }

    public ReqRes getMyInfo(String email){
        ReqRes response = new ReqRes();
        try{
            Optional<OurUsers> ourUser = ourUsersRepo.findByEmail(email);
            if(ourUser.isPresent()){
                response.setOurUsers(ourUser.get());
                response.setStatusCode(200);
                response.setMessage("User with " + ourUser.get().getEmail() + " is fetched successfully...");
            } else{
                response.setStatusCode(404);
                response.setMessage("There is no user with user email: " + ourUser.get().getEmail());
            }

        }  catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error encountered with getMyInfo: " + e.getMessage());
        }
        return response;
    }

}









































