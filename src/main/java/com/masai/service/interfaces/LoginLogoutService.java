package com.masai.service.interfaces;

import com.masai.dto.CustomerDTO;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;
import com.masai.model.UserSession;


public interface LoginLogoutService {
	
	public UserSession loginCustomer(CustomerDTO customer);
	
	public SessionDTO logoutCustomer(SessionDTO session);
	
	public void checkTokenStatus(String token);
	
	public void deleteExpiredTokens();
	
	
	public UserSession loginSeller(SellerDTO seller);
	
	public SessionDTO logoutSeller(SessionDTO session);
	
	
}
