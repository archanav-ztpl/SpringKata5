package com.masai.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.masai.service.interfaces.CustomerService;
import com.masai.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.LoginException;
import com.masai.exception.OrderException;
import com.masai.dto.CartDTO;
import com.masai.model.CartItem;
import com.masai.model.Customer;
import com.masai.model.Order;
import com.masai.dto.OrderDTO;
import com.masai.model.OrderStatusValues;
import com.masai.model.ProductStatus;
import com.masai.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CartServiceImpl cartService;
	
	
	@Override
	public Order saveOrder(OrderDTO orderDTO,String token) throws LoginException, OrderException {
		
		Order newOrder= new Order();
		
		Customer loggedInCustomer= customerService.getLoggedInCustomerDetails(token);
		
		if(loggedInCustomer != null) {
			//Customer loggedInCustomer= cs.getLoggedInCustomerDetails(token);
			newOrder.setCustomer(loggedInCustomer);
			String usersCardNumber= loggedInCustomer.getCreditCard().getCardNumber();
			String userGivenCardNumber= orderDTO.getCardNumber().getCardNumber();
			List<CartItem> productsInCart= loggedInCustomer.getCustomerCart().getCartItems();
			List<CartItem> productsInOrder = new ArrayList<>(productsInCart);
			
			newOrder.setOrdercartItems(productsInOrder);
			newOrder.setTotal(loggedInCustomer.getCustomerCart().getCartTotal());
			
			
			if(productsInCart.size()!=0) {
				if((usersCardNumber.equals(userGivenCardNumber)) 
						&& (orderDTO.getCardNumber().getCardValidity().equals(loggedInCustomer.getCreditCard().getCardValidity())
							&& (orderDTO.getCardNumber().getCardCVV().equals(loggedInCustomer.getCreditCard().getCardCVV())))) {

					//System.out.println(usersCardNumber);
					newOrder.setCardNumber(orderDTO.getCardNumber().getCardNumber());
					newOrder.setAddress(loggedInCustomer.getAddress().get(orderDTO.getAddressType()));
					newOrder.setDate(LocalDate.now());
					newOrder.setOrderStatus(OrderStatusValues.SUCCESS);
					System.out.println(usersCardNumber);
					List<CartItem> cartItemsList= loggedInCustomer.getCustomerCart().getCartItems();
					
					for(CartItem cartItem : cartItemsList ) {
						Integer remainingQuantity = cartItem.getCartProduct().getQuantity()-cartItem.getCartItemQuantity();
						if(remainingQuantity < 0 || cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
							CartDTO cartDto = new CartDTO();
							cartDto.setProductId(cartItem.getCartProduct().getProductId());
							cartService.removeProductFromCart(cartDto, token);
							throw new OrderException("Product "+ cartItem.getCartProduct().getProductName() + " OUT OF STOCK");
						}
						cartItem.getCartProduct().setQuantity(remainingQuantity);
						if(cartItem.getCartProduct().getQuantity()==0) {
							cartItem.getCartProduct().setStatus(ProductStatus.OUTOFSTOCK);
						}
					}
					cartService.clearCart(token);
					//System.out.println(newOrder);
					return orderRepository.save(newOrder);
				}
				else {
					System.out.println("Not same");
					newOrder.setCardNumber(null);
					newOrder.setAddress(loggedInCustomer.getAddress().get(orderDTO.getAddressType()));
					newOrder.setDate(LocalDate.now());
					newOrder.setOrderStatus(OrderStatusValues.PENDING);
					cartService.clearCart(token);
					return orderRepository.save(newOrder);
					
				}
			}
			else {
				throw new OrderException("No products in Cart");
			}
			
		}
		else {
			throw new LoginException("Invalid session token for customer"+"Kindly Login");
		}
	}

	@Override
	public Order getOrderByOrderId(Integer orderId) throws OrderException {
		return orderRepository.findById(orderId).orElseThrow(()-> new OrderException("No order exists with given OrderId "+ orderId));

	}

	@Override
	public List<Order> getAllOrders() throws OrderException {
		// TODO Auto-generated method stub
		List<Order> orders = orderRepository.findAll();
		if(orders.size()>0)
			return orders;
		else
			throw new OrderException("No Orders exists on your account");
	}

	@Override
	public Order cancelOrderByOrderId(Integer orderId,String token) throws OrderException {
		Order order= orderRepository.findById(orderId).orElseThrow(()->new OrderException("No order exists with given OrderId "+ orderId));
		if(order.getCustomer().getCustomerId()== customerService.getLoggedInCustomerDetails(token).getCustomerId()) {
			if(order.getOrderStatus()==OrderStatusValues.PENDING) {
				order.setOrderStatus(OrderStatusValues.CANCELLED);
				orderRepository.save(order);
				return order;
			}
			else if(order.getOrderStatus()==OrderStatusValues.SUCCESS) {
				order.setOrderStatus(OrderStatusValues.CANCELLED);
				List<CartItem> cartItemsList= order.getOrdercartItems();
				
				for(CartItem cartItem : cartItemsList ) {
					Integer addedQuantity = cartItem.getCartProduct().getQuantity()+cartItem.getCartItemQuantity();
					cartItem.getCartProduct().setQuantity(addedQuantity);
					if(cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
						cartItem.getCartProduct().setStatus(ProductStatus.AVAILABLE);
					}
				}
				
				orderRepository.save(order);
				return order;
			}
			else {
				throw new OrderException("Order was already cancelled");
			}
		}
		else {
			throw new LoginException("Invalid session token for customer"+"Kindly Login");
		}

		
	}

	@Override
	public Order updateOrderByOrder(OrderDTO orderDTO, Integer orderId, String token) throws OrderException,LoginException {
		Order existingOrder= orderRepository.findById(orderId).orElseThrow(()->new OrderException("No order exists with given OrderId "+ orderId));

		if(existingOrder.getCustomer().getCustomerId()== customerService.getLoggedInCustomerDetails(token).getCustomerId()) {
			//existingOrder.setCardNumber(orderDTO.getCardNumber().getCardNumber());
			//existingOrder.setAddress(existingOrder.getCustomer().getAddress().get(orderDTO.getAddressType()));
			Customer loggedInCustomer = customerService.getLoggedInCustomerDetails(token);
			String usersCardNumber= loggedInCustomer.getCreditCard().getCardNumber();
			String userGivenCardNumber= orderDTO.getCardNumber().getCardNumber();
//			System.out.println(loggedInCustomer);
			if((usersCardNumber.equals(userGivenCardNumber)) 
					&& (orderDTO.getCardNumber().getCardValidity().equals(loggedInCustomer.getCreditCard().getCardValidity())
						&& (orderDTO.getCardNumber().getCardCVV().equals(loggedInCustomer.getCreditCard().getCardCVV())))) {
				existingOrder.setCardNumber(orderDTO.getCardNumber().getCardNumber());
				existingOrder.setAddress(existingOrder.getCustomer().getAddress().get(orderDTO.getAddressType()));
				existingOrder.setOrderStatus(OrderStatusValues.SUCCESS);
				List<CartItem> cartItemsList= existingOrder.getOrdercartItems();
				for(CartItem cartItem : cartItemsList ) {
					Integer remainingQuantity = cartItem.getCartProduct().getQuantity()-cartItem.getCartItemQuantity();
					if(remainingQuantity < 0 || cartItem.getCartProduct().getStatus() == ProductStatus.OUTOFSTOCK) {
						CartDTO cartDto = new CartDTO();
						cartDto.setProductId(cartItem.getCartProduct().getProductId());
						cartService.removeProductFromCart(cartDto, token);
						throw new OrderException("Product "+ cartItem.getCartProduct().getProductName() + " OUT OF STOCK");
					}
					cartItem.getCartProduct().setQuantity(remainingQuantity);
					if(cartItem.getCartProduct().getQuantity()==0) {
						cartItem.getCartProduct().setStatus(ProductStatus.OUTOFSTOCK);
					}
				}
				return orderRepository.save(existingOrder);
			}
			else {
				throw new OrderException("Incorrect Card Number Again" + usersCardNumber + userGivenCardNumber);
			}
			
			
		}
		else {
			throw new LoginException("Invalid session token for customer"+"Kindly Login");
		}
		
	}

	@Override
	public List<Order> getAllOrdersByDate(LocalDate date) throws OrderException {
		
		List<Order> listOfOrdersOntheDay= orderRepository.findByDate(date);
		return listOfOrdersOntheDay;
	}

	@Override
	public Customer getCustomerByOrderid(Integer orderId) throws OrderException {
		Optional<Order> opt= orderRepository.findById(orderId);
		if(opt.isPresent()) {
			Order existingorder= opt.get();
			
			return orderRepository.getCustomerByOrderid(existingorder.getCustomer().getCustomerId());
		}
		else
			throw new OrderException("No Order exists with orderId "+orderId);
	}

}
