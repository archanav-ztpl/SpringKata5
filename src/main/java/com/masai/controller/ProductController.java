package com.masai.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.CategoryEnum;
import com.masai.model.Product;
import com.masai.dto.ProductDTO;
import com.masai.model.ProductStatus;
import com.masai.service.interfaces.ProductService;

@RestController
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProductToCatalogHandler(@RequestHeader("token") String token,
            @Valid @RequestBody Product product) {

        Product prod = productService.addProductToCatalog(token, product);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(prod.getProductId())
                .toUri();

        return ResponseEntity.created(location).body(prod);

    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductFromCatalogByIdHandler(@PathVariable("id") Integer id) {

        Product prod = productService.getProductFromCatalogById(id);

        return ResponseEntity.ok(prod);

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<Void> deleteProductFromCatalogHandler(@PathVariable("id") Integer id) {

        productService.deleteProductFromCatalog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/products")
    public ResponseEntity<Product> updateProductInCatalogHandler(@Valid @RequestBody Product prod) {

        Product prod1 = productService.updateProductIncatalog(prod);

        return ResponseEntity.ok(prod1);

    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProductsHandler() {

        List<Product> list = productService.getAllProductsIncatalog();

        return ResponseEntity.ok(list);
    }

  //this method gets the products mapped to a particular seller
    @GetMapping("/products/seller/{id}")
    public ResponseEntity<List<ProductDTO>> getAllProductsOfSellerHandler(@PathVariable("id") Integer id) {

        List<ProductDTO> list = productService.getAllProductsOfSeller(id);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/products/{catenum}")
    public ResponseEntity<List<ProductDTO>> getAllProductsInCategory(@PathVariable("catenum") String catenum) {
        CategoryEnum ce = CategoryEnum.valueOf(catenum.toUpperCase());
        List<ProductDTO> list = productService.getProductsOfCategory(ce);
        return ResponseEntity.ok(list);

    }

    @GetMapping("/products/status/{status}")
    public ResponseEntity<List<ProductDTO>> getProductsWithStatusHandler(@PathVariable("status") String status) {

        ProductStatus ps = ProductStatus.valueOf(status.toUpperCase());
        List<ProductDTO> list = productService.getProductsOfStatus(ps);

        return ResponseEntity.ok(list);

    }


    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateQuantityOfProduct(@PathVariable("id") Integer id,@RequestBody ProductDTO prodDto){

         Product prod =   productService.updateProductQuantityWithId(id, prodDto);

         return ResponseEntity.ok(prod);
    }

}
