package com.ecommerce.project.payload;

import java.util.ArrayList;
import java.util.List;

import com.ecommerce.project.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
	private Long addressId;
	private String street;
	private String buildingName;
	private String city;
	private String state;
	private String country;
	private String pincode;



	public AddressDto(String street, String buildingName, String city, String state, String country, String pincode) {
		super();
		this.street = street;
		this.buildingName = buildingName;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pincode = pincode;
	}

}
