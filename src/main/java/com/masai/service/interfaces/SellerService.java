package com.masai.service.interfaces;

import java.util.List;

import com.masai.exception.SellerException;
import com.masai.model.Seller;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;

public interface SellerService {
	
	public Seller addSeller(Seller seller);
	
	public List<Seller> getAllSellers() throws SellerException;
	
	public Seller getSellerById(Integer sellerId)throws SellerException;
	
	public Seller getSellerByMobile(String mobile, String token) throws SellerException;
	
	public Seller getCurrentlyLoggedInSeller(String token) throws SellerException;
	
	public SessionDTO updateSellerPassword(SellerDTO sellerDTO, String token) throws SellerException;
	
	public Seller updateSeller(Seller seller, String token)throws SellerException;
	
	public Seller updateSellerMobile(SellerDTO sellerDTO, String token)throws SellerException;

	public void deleteSellerById(Integer sellerId, String token)throws SellerException;

}
