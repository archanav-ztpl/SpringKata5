package com.masai.service.interfaces;

import java.util.List;

import com.masai.exception.SellerException;
import com.masai.model.Seller;
import com.masai.dto.CreateSellerDTO;
import com.masai.dto.UpdateSellerDTO;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;

public interface SellerService {
	
	public Seller createSeller(CreateSellerDTO createSellerDTO);

	public List<Seller> getAllSellers() throws SellerException;
	
	public Seller getSellerById(Integer sellerId)throws SellerException;
	
	public Seller getSellerByMobile(String mobile, String username) throws SellerException;

	public Seller getCurrentlyLoggedInSeller(String username) throws SellerException;

	public SessionDTO updateSellerPassword(SellerDTO sellerDTO, String username) throws SellerException;

	public Seller updateSeller(UpdateSellerDTO updateSellerDTO, String username)throws SellerException;

	public Seller updateSellerMobile(SellerDTO sellerDTO, String username)throws SellerException;

	public void deleteSellerById(Integer sellerId, String username)throws SellerException;

}
