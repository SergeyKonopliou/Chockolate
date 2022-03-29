package com.chockolate.service;

import java.util.List;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Order;

public interface OrderService {

	public void addNewOrder(Order order) throws ServiceException;;
	
	public List<Order> loadAllOrder() throws ServiceException;;
	
	public void deleteOrder(Long id) throws ServiceException;;
}
