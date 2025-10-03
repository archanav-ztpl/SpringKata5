package com.masai.service;

import java.util.List;
import java.util.Optional;

import com.masai.service.interfaces.LoginLogoutService;
import com.masai.service.interfaces.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.masai.exception.LoginException;
import com.masai.exception.SellerException;
import com.masai.model.Seller;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;
import com.masai.model.UserSession;
import com.masai.repository.SellerRepository;
import com.masai.repository.SessionRepository;

@Service
public class SellerServiceImpl implements SellerService {
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private LoginLogoutService loginService;
	
	@Autowired
	private SessionRepository sessionRepository;
	

	@Override
	public Seller addSeller(Seller seller) {
		
		Seller add= sellerRepository.save(seller);
		
		return add;
	}

	@Override
	public List<Seller> getAllSellers() throws SellerException {
		
		List<Seller> sellers= sellerRepository.findAll();
		
		if(sellers.size()>0) {
			return sellers;
		}
		else throw new SellerException("No Seller Found !");
		
	}

	@Override
	public Seller getSellerById(Integer sellerId) {
		
		Optional<Seller> seller= sellerRepository.findById(sellerId);
		
		if(seller.isPresent()) {
			return seller.get();
		}
		else throw new SellerException("Seller not found for this ID: "+sellerId);
	}

	@Override
	public Seller updateSeller(Seller seller, String token) {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		Seller existingSeller= sellerRepository.findById(seller.getSellerId()).orElseThrow(()-> new SellerException("Seller not found for this Id: "+seller.getSellerId()));
		Seller newSeller= sellerRepository.save(seller);
		return newSeller;
	}

	@Override
	public void deleteSellerById(Integer sellerId, String token) {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		Optional<Seller> opt= sellerRepository.findById(sellerId);
		
		if(opt.isPresent()) {
			
			UserSession user = sessionRepository.findByToken(token).get();
			
			Seller existingseller=opt.get();
			
			if(user.getUserId().equals(existingseller.getSellerId())) {
				sellerRepository.delete(existingseller);
				
				// logic to log out a seller after he deletes his account
				SessionDTO session = new SessionDTO();
				session.setToken(token);
				loginService.logoutSeller(session);

            }
			else {
				throw new SellerException("Verification Error in deleting seller account");
			}
		}
		else throw new SellerException("Seller not found for this ID: "+sellerId);
		
	}

	@Override
	public Seller updateSellerMobile(SellerDTO sellerDTO, String token) throws SellerException {

		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionRepository.findByToken(token).get();
		
		Seller existingSeller= sellerRepository.findById(user.getUserId()).orElseThrow(()->new SellerException("Seller not found for this ID: "+ user.getUserId()));
		
		if(existingSeller.getPassword().equals(sellerDTO.getPassword())) {
			existingSeller.setMobile(sellerDTO.getMobile());
			return sellerRepository.save(existingSeller);
		}
		else {
			throw new SellerException("Error occured in updating mobile. Please enter correct password");
		}
	}

	@Override
	public Seller getSellerByMobile(String mobile, String token) throws SellerException {
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		Seller existingSeller = sellerRepository.findByMobile(mobile).orElseThrow( () -> new SellerException("Seller not found with given mobile"));
		
		return existingSeller;
	}
	
	@Override
	public Seller getCurrentlyLoggedInSeller(String token) throws SellerException{
		
		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}
		
		loginService.checkTokenStatus(token);
		
		UserSession user = sessionRepository.findByToken(token).get();
		
		Seller existingSeller= sellerRepository.findById(user.getUserId()).orElseThrow(()->new SellerException("Seller not found for this ID"));
		
		return existingSeller;
		
	}
	
	
	// Method to update password - based on current token
	
	@Override
	public SessionDTO updateSellerPassword(SellerDTO sellerDTO, String token) {

		if(!token.contains("seller")) {
			throw new LoginException("Invalid session token for seller");
		}


		loginService.checkTokenStatus(token);
			
		UserSession user = sessionRepository.findByToken(token).get();
			
		Optional<Seller> opt = sellerRepository.findById(user.getUserId());
			
		if(opt.isEmpty())
			throw new SellerException("Seller does not exist");
			
		Seller existingSeller = opt.get();
			

		if(!sellerDTO.getMobile().equals(existingSeller.getMobile())) {
			throw new SellerException("Verification error. Mobile number does not match");
		}
			
		existingSeller.setPassword(sellerDTO.getPassword());
			
		sellerRepository.save(existingSeller);
			
		SessionDTO session = new SessionDTO();
			
		session.setToken(token);
			
		loginService.logoutSeller(session);
			
		session.setMessage("Updated password and logged out. Login again with new password");
			
		return session;

	}

}
