package com.namnguyen.mvphotelbookingapi.services.impl;

import com.namnguyen.mvphotelbookingapi.models.dto.AddressDTO;
import com.namnguyen.mvphotelbookingapi.models.entity.AddressEntity;
import com.namnguyen.mvphotelbookingapi.repository.AddressRepository;
import com.namnguyen.mvphotelbookingapi.services.AddressService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public AddressEntity saveAddress(AddressDTO addressDTO) {
        log.info("Attempting to save a new address: {}", addressDTO.toString());
        AddressEntity address = mapAddressDtoToAddress(addressDTO);

        AddressEntity savedAddress = addressRepository.save(address);
        log.info("Successfully saved new address with ID: {}", address.getId());
        return savedAddress;
    }
//
//    @Override
//    public AddressDTO findAddressById(Long id) {
//        Address address = addressRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
//
//        return mapAddressToAddressDto(address);
//    }

    @Override
    public AddressEntity updateAddress(AddressDTO addressDTO) {
        log.info("Attempting to update address with ID: {}", addressDTO.getId());
        AddressEntity existingAddress = addressRepository.findById(addressDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));

        setFormattedDataToAddress(existingAddress, addressDTO);

        AddressEntity updatedAddress = addressRepository.save(existingAddress);
        log.info("Successfully updated address with ID: {}", existingAddress.getId());

        return updatedAddress;
    }
//
//    @Override
//    public void deleteAddress(Long id) {
//        log.info("Attempting to delete address with ID: {}", id);
//        if (!addressRepository.existsById(id)) {
//            log.error("Failed to delete address. Address with ID: {} not found", id);
//            throw new EntityNotFoundException("Address not found");
//        }
//        addressRepository.deleteById(id);
//        log.info("Successfully deleted address with ID: {}", id);
//    }
//
    @Override
    public AddressEntity mapAddressDtoToAddress(AddressDTO dto) {
        return AddressEntity.builder()
                .addressLine(formatText(dto.getAddressLine()))
                .city(formatText(dto.getCity()))
                .country(formatText(dto.getCountry()))
                .build();
    }

    @Override
    public AddressDTO mapAddressToAddressDto(AddressEntity address) {
        return AddressDTO.builder()
                .id(address.getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .country(address.getCountry())
                .build();
    }

    private String formatText(String text) {
        return StringUtils.capitalize(text.trim());
    }

    private void setFormattedDataToAddress(AddressEntity address, AddressDTO addressDTO) {
        address.setAddressLine(formatText(addressDTO.getAddressLine()));
        address.setCity(formatText(addressDTO.getCity()));
        address.setCountry(formatText(addressDTO.getCountry()));
    }
}
