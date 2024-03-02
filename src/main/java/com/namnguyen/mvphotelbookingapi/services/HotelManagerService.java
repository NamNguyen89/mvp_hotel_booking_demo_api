package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.entity.HotelManagerEntity;
import com.namnguyen.mvphotelbookingapi.models.entity.UserEntity;

public interface HotelManagerService {

    HotelManagerEntity findByUser(UserEntity user);

}
