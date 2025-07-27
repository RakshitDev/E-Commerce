package com.ecommerce.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.AddressDto;
import com.ecommerce.project.service.AddressService;
import com.ecommerce.project.util.AuthUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AddressController {

	@Autowired
	AuthUtil authUtil;

	@Autowired
	AddressService addressService;

	@PostMapping("/addresses")
	public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
		User user = authUtil.loggedInUser();
		AddressDto address = addressService.createAddress(addressDto, user);
		return new ResponseEntity<AddressDto>(address, HttpStatus.CREATED);

	}

	@GetMapping("/addresses")
	public ResponseEntity<List<AddressDto>> getAllAddress() {
		List<AddressDto> addressDtos = addressService.getAllAddress();
		return new ResponseEntity<>(addressDtos, HttpStatus.OK);
	}

	@GetMapping("/addresses/{addressId}")
	public ResponseEntity<AddressDto> getAddressbyId(@PathVariable Long addressId) {
		AddressDto addressDto = addressService.findByAddressId(addressId);
		return new ResponseEntity<AddressDto>(addressDto, HttpStatus.OK);
	}

	@GetMapping("/users/addresses")
	public ResponseEntity<List<AddressDto>> getUserAddress() {
		User user = authUtil.loggedInUser();
		List<AddressDto> addressDto = addressService.getUserAddress(user);
		return new ResponseEntity<>(addressDto, HttpStatus.OK);
	}

	@PutMapping("/addresses/{addressId}")
	public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId, @RequestBody AddressDto addressDto) {
		AddressDto updatedDto = addressService.updateAddress(addressId, addressDto);
		return new ResponseEntity<>(updatedDto, HttpStatus.OK);
	}
	@DeleteMapping("/addresses/{addressId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) {
		String message = addressService.deleteAddress(addressId);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
}
