package com.gotham.hotel.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gotham.hotel.dto.LoginRequest;
import com.gotham.hotel.dto.Response;
import com.gotham.hotel.dto.UserDTO;
import com.gotham.hotel.entity.User;
import com.gotham.hotel.exception.OurException;
import com.gotham.hotel.repo.UserRepository;
import com.gotham.hotel.service.interfac.IUserService;
import com.gotham.hotel.utils.JWTUtils;
import com.gotham.hotel.utils.Utils;

import lombok.var;

@Service
public class UserService implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user) {

        Response response = new Response();
        
        try{
            if(user.getRole() == null || user.getRole().isBlank()){

                user.setRole("USER");
            }

            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail() + "Already Exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUser(userDTO);

        }
        catch(OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred during user registration" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        
        Response response = new Response();

        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User not found"));

            var token = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error occurred during user login" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
        
        Response response = new Response();

        try{

            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setUserList(userDTOList);
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting users" + e.getMessage());
        }

        return response;

    }

    @Override
    public Response getUserBookingHistory(String userId) {
        
        Response response = new Response();

        try{

           User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
           UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
           response.setStatusCode(200);
           response.setMessage("Successful");
           response.setUser(userDTO);

        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error user bookings" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        
        Response response = new Response();

        try{

           userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
           userRepository.deleteById(Long.valueOf(userId)); 
           response.setStatusCode(200);
           response.setMessage("Successful");

        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error deleting user" + e.getMessage());
        }

        return response;
        
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try{

           User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("User not found"));
           UserDTO userDTO = Utils.mapUserEntityToUserDTO(user); 
           response.setStatusCode(200);
           response.setMessage("Successful");
           response.setUser(userDTO);

        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user" + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        
        Response response = new Response();

        try{

           User user = userRepository.findByEmail(String.valueOf(email)).orElseThrow(() -> new OurException("User not found"));
           UserDTO userDTO = Utils.mapUserEntityToUserDTO(user); 
           response.setStatusCode(200);
           response.setMessage("Successful");
           response.setUser(userDTO);

        }
        catch(OurException e){
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch(Exception e){
            response.setStatusCode(500);
            response.setMessage("Error getting user info" + e.getMessage());
        }

        return response;
    }
    
}
