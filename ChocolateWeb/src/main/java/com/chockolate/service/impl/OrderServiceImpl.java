package com.chockolate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chockolate.exception.ServiceException;
import com.chockolate.model.Order;
import com.chockolate.repository.OrderRepository;
import com.chockolate.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;

	@Override
	public void addNewOrder(Order order) throws ServiceException {
		try {
			orderRepository.save(order);
		} catch (Exception e) {
			throw new ServiceException("Problems with add new order to DB in service method " + e.getMessage(),e);
		}
	}

	@Override
	public List<Order> loadAllOrder() throws ServiceException {
		List<Order> orders = new ArrayList<>();
		try {
			orders = orderRepository.findAll();
		} catch (Exception e) {
			throw new ServiceException("Problems with load all orders from DB in service method " + e.getMessage(),e);
		}
		return orders;
	}

	@Override
	public void deleteOrder(Long id) throws ServiceException {
		try {
			orderRepository.deleteById(id);
		} catch (Exception e) {
			throw new ServiceException("Problems with delete order from DB in service method " + e.getMessage(),e);
		}
	}

}
