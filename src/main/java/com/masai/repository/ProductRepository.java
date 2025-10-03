package com.masai.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.masai.model.CategoryEnum;
import com.masai.model.Product;
import com.masai.dto.ProductDTO;
import com.masai.model.ProductStatus;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	
	@Query("select new com.masai.dto.ProductDTO(p.productName,p.manufacturer,p.price,p.quantity) "
			+ "from Product p where p.category=:categoryEnum")
	public List<ProductDTO> getAllProductsInACategory(@Param("categoryEnum") CategoryEnum categoryEnum);
	
	
	@Query("select new com.masai.dto.ProductDTO(p.productName,p.manufacturer,p.price,p.quantity) "
			+ "from Product p where p.status=:status")
	public List<ProductDTO> getProductsWithStatus(@Param("status") ProductStatus status);
	
	@Query("select new com.masai.dto.ProductDTO(p.productName,p.manufacturer,p.price,p.quantity) "
			+ "from Product p where p.seller.sellerId=:id")
	public List<ProductDTO> getProductsOfASeller(@Param("id") Integer id);
	

}
