package com.ecommerce.project.service;

import java.util.List;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDto;

public interface AddressService {

	AddressDto createAddress(AddressDto addressDto, User user);

	List<AddressDto> getAllAddress();

	AddressDto findByAddressId(Long addressId);

	List<AddressDto> getUserAddress(User user);

	AddressDto updateAddress(Long addressId, AddressDto addressDto);

	String deleteAddress(Long addressId);

}
