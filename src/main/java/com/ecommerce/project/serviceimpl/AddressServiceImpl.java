package com.ecommerce.project.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.project.exception.ApiException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Address;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDto;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	UserRepository userRepository;

	@Override
	public AddressDto createAddress(AddressDto addressDto, User user) {
		Address address = modelMapper.map(addressDto, Address.class);
		List<Address> addressesList = user.getAddresses();
		addressesList.add(address);
		user.setAddresses(addressesList);
		address.setUser(user);
		Address savedAddress = addressRepository.save(address);

		return modelMapper.map(savedAddress, AddressDto.class);
	}

	@Override
	public List<AddressDto> getAllAddress() {
		List<Address> addressList = addressRepository.findAll();
		System.out.println(addressList);
		if (addressList.size() == 0) {
			throw new ApiException("Address List is Empty");
		}
		List<AddressDto> addressDto = addressList.stream().map(address -> modelMapper.map(address, AddressDto.class))
				.toList();
		return addressDto;
	}

	@Override
	public AddressDto findByAddressId(Long addressId) {
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
		return modelMapper.map(address, AddressDto.class);

	}

	@Override
	public List<AddressDto> getUserAddress(User user) {
		List<Address> userAddress = user.getAddresses();
		List<AddressDto> addressDto = userAddress.stream().map(address -> modelMapper.map(address, AddressDto.class))
				.toList();

		return addressDto;
	}

	@Override
	public AddressDto updateAddress(Long addressId, AddressDto addressDto) {
		Address addressFromDatabase  = addressRepository.findById(addressId)
		.orElseThrow(()-> new ResourceNotFoundException("Address","addressID",addressId));
		
		addressFromDatabase.setCity(addressDto.getCity());
        addressFromDatabase.setPincode(addressDto.getPincode());
        addressFromDatabase.setState(addressDto.getState());
        addressFromDatabase.setCountry(addressDto.getCountry());
        addressFromDatabase.setStreet(addressDto.getStreet());
        addressFromDatabase.setBuildingName(addressDto.getBuildingName());

        Address updatedAddress = addressRepository.save(addressFromDatabase);

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddress);
        userRepository.save(user);
		
        return modelMapper.map(updatedAddress, AddressDto.class);
	}

	@Override
	public String deleteAddress(Long addressId) {
		Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = addressFromDatabase.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(addressFromDatabase);

        return "Address deleted successfully with addressId: " + addressId;
		
	}

}
