package com.gotham.hotel.service.interfac;

import com.gotham.hotel.dto.LoginRequest;
import com.gotham.hotel.dto.Response;
import com.gotham.hotel.entity.User;

public interface IUserService {
    
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);

}
