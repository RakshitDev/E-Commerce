package com.ecommerce.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long addressId;

	@NotBlank
	@Size(min = 5, message = "Street name should include 5 character")
	private String street;

	@NotBlank
	@Size(min = 4, message = "Building name should include 4 character")
	private String buildingName;

	@NotBlank
	@Size(min = 4, message = "City name should include 4  character")
	private String city;

	@NotBlank
	@Size(min = 4, message = "State name should include 4 character")
	private String state;

	@NotBlank
	@Size(min = 4, message = "Country name should include 4 character")
	private String country;

	@NotBlank
	@Size(min = 6, message = "pincode  should include 6 character")
	private String pincode;
	
	@ToString.Exclude
	@ManyToOne()
	@JoinColumn(name="userId")
	private User user;

	public Address(@NotBlank @Size(min = 5, message = "Street name should include 5 character") String street,
			@NotBlank @Size(min = 4, message = "Building name should include 4 character") String buildingName,
			@NotBlank @Size(min = 4, message = "City name should include 4  character") String city,
			@NotBlank @Size(min = 4, message = "State name should include 4 character") String state,
			@NotBlank @Size(min = 4, message = "Country name should include 4 character") String country,
			@NotBlank @Size(min = 6, message = "pincode  should include 6 character") String pincode) {
		super();
		this.street = street;
		this.buildingName = buildingName;
		this.city = city;
		this.state = state;
		this.country = country;
		this.pincode = pincode;
	}

}
