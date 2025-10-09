package com.masai.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.CategoryEnum;
import com.masai.model.Product;
import com.masai.dto.ProductDTO;
import com.masai.model.ProductStatus;
import com.masai.service.interfaces.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Product Controller", description = "APIs for managing products")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Add a new product", description = "Adds a new product to the catalog")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Product object containing product details", required = true, content = @Content(schema = @Schema(implementation = Product.class)))
    @PostMapping
    public ResponseEntity<Product> addProductToCatalogHandler(@RequestHeader("token") String token, @Valid @RequestBody Product product) {
        logger.info("Adding product to catalog with token: {}", token);
        Product prod = productService.addProductToCatalog(token, product);
        logger.info("Product added successfully with ID: {}", prod.getProductId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(prod.getProductId()).toUri();

        return ResponseEntity.created(location).body(prod);
    }

    @Operation(summary = "Get product by ID", description = "Fetches details of a product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductFromCatalogByIdHandler(@Parameter(name = "id", description = "ID of the product", required = true) @PathVariable("id") Integer id) {

        Product prod = productService.getProductFromCatalogById(id);

        return ResponseEntity.ok(prod);

    }

    @Operation(summary = "Delete a product", description = "Deletes a product from the catalog by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductFromCatalogHandler(@PathVariable("id") Integer id) {

        productService.deleteProductFromCatalog(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update product", description = "Updates the details of an existing product")
    @PutMapping
    public ResponseEntity<Product> updateProductInCatalogHandler(@Valid @RequestBody Product prod) {

        Product prod1 = productService.updateProductIncatalog(prod);

        return ResponseEntity.ok(prod1);

    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProductsHandler() {

        List<Product> list = productService.getAllProductsIncatalog();

        return ResponseEntity.ok(list);
    }

  //this method gets the products mapped to a particular seller
    @GetMapping("/seller/{id}")
    public ResponseEntity<List<ProductDTO>> getAllProductsOfSellerHandler(@PathVariable("id") Integer id) {

        List<ProductDTO> list = productService.getAllProductsOfSeller(id);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/category/{catenum}")
    public ResponseEntity<List<ProductDTO>> getAllProductsInCategory(@PathVariable("catenum") String catenum) {
        CategoryEnum ce = CategoryEnum.valueOf(catenum.toUpperCase());
        List<ProductDTO> list = productService.getProductsOfCategory(ce);
        return ResponseEntity.ok(list);

    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductDTO>> getProductsWithStatusHandler(@PathVariable("status") String status) {

        ProductStatus ps = ProductStatus.valueOf(status.toUpperCase());
        List<ProductDTO> list = productService.getProductsOfStatus(ps);

        return ResponseEntity.ok(list);

    }

    @Operation(summary = "Update product quantity", description = "Updates the quantity of a product by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateQuantityOfProduct(@PathVariable("id") Integer id,@RequestBody ProductDTO prodDto){

         Product prod =   productService.updateProductQuantityWithId(id, prodDto);

         return ResponseEntity.ok(prod);
    }

}
