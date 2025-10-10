package com.masai.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.Seller;
import com.masai.dto.CreateSellerDTO;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;
import com.masai.dto.UpdateSellerDTO;
import com.masai.service.interfaces.SellerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Tag(name = "Seller Controller", description = "APIs for managing sellers")
@RestController
@RequestMapping("/api/v1/sellers")
@SecurityRequirement(name = "bearerAuth")
public class SellerController {

    private static final Logger logger = LoggerFactory.getLogger(SellerController.class);

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    //Add seller-------------------------------------

    @Operation(summary = "Add a new seller", description = "Creates a new seller account")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller creation data", required = true, content = @Content(schema = @Schema(implementation = CreateSellerDTO.class)))
    @PostMapping
    public ResponseEntity<Seller> createSeller(@Valid @RequestBody CreateSellerDTO createSellerDTO){

        logger.info("Adding a new seller: {}", createSellerDTO.getFirstName());
        Seller addedSeller = sellerService.createSeller(createSellerDTO);
        logger.info("Seller added successfully with ID: {}", addedSeller.getSellerId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addedSeller.getSellerId())
                .toUri();

        return ResponseEntity.created(location).body(addedSeller);
    }

    //Get the list of seller-----------------------

    @Operation(summary = "Get all sellers", description = "Fetches a list of all sellers")
    @GetMapping
    public ResponseEntity<List<Seller>> getAllSeller(){

        List<Seller> sellers= sellerService.getAllSellers();

        return ResponseEntity.ok(sellers);
    }

    //Get the seller by Id............................

    @Operation(summary = "Get seller by ID", description = "Fetches details of a seller by their ID")
    @Parameter(name = "sellerId", description = "ID of the seller", required = true)
    @GetMapping("/{sellerId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Seller> getSellerById(@PathVariable("sellerId") Integer sellerId){

        Seller getSeller= sellerService.getSellerById(sellerId);

        return ResponseEntity.ok(getSeller);
    }

    // Get Seller by mobile Number

    @Operation(summary = "Get seller by mobile", description = "Fetches details of a seller by their mobile number")
    @GetMapping("/seller")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Seller> getSellerByMobile(@Parameter(name = "mobile", description = "Mobile number of the seller", required = true) @RequestParam("mobile") String mobile) {

        String currentUsername = getCurrentUsername();
        Seller getSeller = sellerService.getSellerByMobile(mobile, currentUsername);

        return ResponseEntity.ok(getSeller);
    }

    // Get currently logged in seller

    @Operation(summary = "Get logged-in seller details", description = "Fetches details of the currently logged-in seller")
    @GetMapping("/current")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Seller> getLoggedInSeller(){

        String currentUsername = getCurrentUsername();
        Seller getSeller = sellerService.getCurrentlyLoggedInSeller(currentUsername);

        return ResponseEntity.ok(getSeller);
    }

    //Update the seller..............................

    @Operation(summary = "Update an existing seller", description = "Updates an existing seller account")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller update data", required = true, content = @Content(schema = @Schema(implementation = UpdateSellerDTO.class)))
    @PutMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Seller> updateSeller(@Valid @RequestBody UpdateSellerDTO updateSellerDTO){
        String currentUsername = getCurrentUsername();
        Seller updatedSeller = sellerService.updateSeller(updateSellerDTO, currentUsername);

        return ResponseEntity.ok(updatedSeller);
    }

    @Operation(summary = "Update seller mobile number", description = "Updates seller mobile number")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller object containing required seller details", required = true, content = @Content(schema = @Schema(implementation = SellerDTO.class)))
    @PutMapping("/update/mobile")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Seller> updateSellerMobile(@Valid @RequestBody SellerDTO sellerDto){
        String currentUsername = getCurrentUsername();
        Seller updatedseller= sellerService.updateSellerMobile(sellerDto, currentUsername);

        return ResponseEntity.ok(updatedseller);
    }

    @Operation(summary = "Update seller password", description = "Updates seller password")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Seller object containing required seller details", required = true, content = @Content(schema = @Schema(implementation = SellerDTO.class)))
    @PutMapping("/update/password")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<SessionDTO> updateSellerPassword(@Valid @RequestBody SellerDTO sellerDto){
        String currentUsername = getCurrentUsername();
        return ResponseEntity.ok(sellerService.updateSellerPassword(sellerDto, currentUsername));
    }

    @Operation(summary = "Delete seller account", description = "Deletes seller account by ID")
    @DeleteMapping("/{sellerId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteSellerById(@Parameter(name = "sellerId", description = "ID of the seller", required = true) @PathVariable("sellerId") Integer sellerId){

        String currentUsername = getCurrentUsername();
        sellerService.deleteSellerById(sellerId, currentUsername);

        return ResponseEntity.noContent().build();
    }

}
