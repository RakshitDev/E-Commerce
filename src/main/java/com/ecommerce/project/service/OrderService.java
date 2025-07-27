package com.ecommerce.project.service;

import com.ecommerce.project.payload.OrderDto;

public interface OrderService {

	OrderDto placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId,
			String pgStatus, String pgResponseMessage);


}
