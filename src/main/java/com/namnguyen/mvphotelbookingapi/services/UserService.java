package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.dto.ResetPasswordDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.UserDTO;
import com.namnguyen.mvphotelbookingapi.models.dto.UserRegistrationDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.UserEntity;

import java.util.List;

public interface UserService {

//    UserEntity saveUser(UserRegistrationDTO registrationDTO);

    // For registration
    UserEntity findUserByUsername(String username);

    UserDTO findUserDTOByUsername(String username);

//    UserDTO findUserById(Long id);
//
//    List<UserDTO> findAllUsers();
//
//    void updateUser(UserDTO userDTO);
//
//    void updateLoggedInUser(UserDTO userDTO);
//
//    void deleteUserById(Long id);
//
//    UserEntity resetPassword(ResetPasswordDTO resetPasswordDTO);

}
