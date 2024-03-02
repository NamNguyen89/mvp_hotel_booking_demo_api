package com.namnguyen.mvphotelbookingapi.services;

import com.namnguyen.mvphotelbookingapi.models.dto.AddressDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.AddressEntity;

public interface AddressService {

    AddressEntity saveAddress(AddressDTO addressDTO);
//
//    AddressDTO findAddressById(Long id);

    AddressEntity updateAddress(AddressDTO addressDTO);

//    void deleteAddress(Long id);

    AddressEntity mapAddressDtoToAddress(AddressDTO dto);

    AddressDTO mapAddressToAddressDto(AddressEntity address);


}
