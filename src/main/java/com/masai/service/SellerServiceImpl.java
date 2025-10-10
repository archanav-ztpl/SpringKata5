package com.masai.service;

import java.util.List;
import java.util.Optional;

import com.masai.dto.CreateSellerDTO;
import com.masai.dto.UpdateSellerDTO;
import com.masai.mapper.SellerMapper;
import com.masai.service.interfaces.LoginLogoutService;
import com.masai.service.interfaces.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	private SellerMapper sellerMapper;

	@Autowired
	private LoginLogoutService loginService;
	
	@Autowired
	private SessionRepository sessionRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Seller createSeller(CreateSellerDTO createSellerDTO) {

		// Use MapStruct mapper to convert DTO to entity
		Seller seller = sellerMapper.toEntity(createSellerDTO);

		// Set encoded password separately for security
		seller.setPassword(passwordEncoder.encode(createSellerDTO.getPassword()));

		return sellerRepository.save(seller);
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
	public Seller updateSeller(UpdateSellerDTO updateSellerDTO, String username) {

		// Get seller by username (mobile) from database
		Optional<Seller> existingSellerOpt = sellerRepository.findByMobile(username);
		if (existingSellerOpt.isEmpty()) {
			throw new SellerException("Seller not found for username: " + username);
		}
		
		Seller existingSeller = existingSellerOpt.get();

		// If sellerId is provided in DTO, verify it matches the logged-in seller
		if (updateSellerDTO.getSellerId() != null &&
			!existingSeller.getSellerId().equals(updateSellerDTO.getSellerId())) {
			throw new SellerException("Unauthorized: Cannot update another seller's information");
		}

		// Use MapStruct to update existing entity with DTO values
		sellerMapper.updateEntityFromDTO(updateSellerDTO, existingSeller);

		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSellerById(Integer sellerId, String username) {

		// Get seller by username (mobile) from database
		Optional<Seller> currentSellerOpt = sellerRepository.findByMobile(username);
		if (currentSellerOpt.isEmpty()) {
			throw new SellerException("Seller not found for username: " + username);
		}
		
		Seller currentSeller = currentSellerOpt.get();

		// Verify the seller is trying to delete their own account
		if (!currentSeller.getSellerId().equals(sellerId)) {
			throw new SellerException("Unauthorized: Cannot delete another seller's account");
		}

		sellerRepository.delete(currentSeller);
	}

	@Override
	public Seller updateSellerMobile(SellerDTO sellerDTO, String username) throws SellerException {

		// Get seller by current username (mobile) from database
		Optional<Seller> existingSellerOpt = sellerRepository.findByMobile(username);
		if (existingSellerOpt.isEmpty()) {
			throw new SellerException("Seller not found for username: " + username);
		}
		
		Seller existingSeller = existingSellerOpt.get();

		if(passwordEncoder.matches(sellerDTO.getPassword(), existingSeller.getPassword())) {
			existingSeller.setMobile(sellerDTO.getMobile());
			return sellerRepository.save(existingSeller);
		}
		else {
			throw new SellerException("Error occurred in updating mobile. Please enter correct password");
		}
	}

	@Override
	public Seller getSellerByMobile(String mobile, String username) throws SellerException {

		// Verify the requesting user is a seller (additional security check)
		Optional<Seller> requestingSeller = sellerRepository.findByMobile(username);
		if (requestingSeller.isEmpty()) {
			throw new SellerException("Unauthorized access");
		}
		
		Seller existingSeller = sellerRepository.findByMobile(mobile).orElseThrow(
			() -> new SellerException("Seller not found with given mobile"));

		return existingSeller;
	}
	
	@Override
	public Seller getCurrentlyLoggedInSeller(String username) throws SellerException{

		Seller existingSeller = sellerRepository.findByMobile(username).orElseThrow(
			() -> new SellerException("Seller not found for username: " + username));

		return existingSeller;
	}
	
	
	// Method to update password - based on current username

	@Override
	public SessionDTO updateSellerPassword(SellerDTO sellerDTO, String username) {

		// Get seller by username (mobile) from database
		Optional<Seller> opt = sellerRepository.findByMobile(username);

		if(opt.isEmpty())
			throw new SellerException("Seller does not exist");
			
		Seller existingSeller = opt.get();

		if(!sellerDTO.getMobile().equals(existingSeller.getMobile())) {
			throw new SellerException("Verification error. Mobile number does not match");
		}
			
		existingSeller.setPassword(passwordEncoder.encode(sellerDTO.getPassword()));

		sellerRepository.save(existingSeller);
			
		SessionDTO session = new SessionDTO();
		session.setMessage("Password updated successfully. Please login again with new password");

		return session;

	}
}
