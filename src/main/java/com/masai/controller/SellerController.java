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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.masai.model.Seller;
import com.masai.dto.SellerDTO;
import com.masai.dto.SessionDTO;
import com.masai.service.interfaces.SellerService;

@RestController
public class SellerController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }


    //Add seller-------------------------------------

    @PostMapping("/addseller")
    public ResponseEntity<Seller> addSellerHandler(@Valid @RequestBody Seller seller){

        Seller addseller= sellerService.addSeller(seller);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addseller.getSellerId())
                .toUri();

        return ResponseEntity.created(location).body(addseller);
    }



    //Get the list of seller-----------------------


    @GetMapping("/sellers")
    public ResponseEntity<List<Seller>> getAllSellerHandler(){

        List<Seller> sellers= sellerService.getAllSellers();

        return ResponseEntity.ok(sellers);
    }


    //Get the seller by Id............................


    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Seller> getSellerByIdHandler(@PathVariable("sellerId") Integer sellerId){

        Seller getSeller= sellerService.getSellerById(sellerId);

        return ResponseEntity.ok(getSeller);
    }


    // Get Seller by mobile Number

    @GetMapping("/seller")
    public ResponseEntity<Seller> getSellerByMobileHandler(@RequestParam("mobile") String mobile, @RequestHeader("token") String token){

        Seller getSeller= sellerService.getSellerByMobile(mobile, token);

        return ResponseEntity.ok(getSeller);
    }


    // Get currently logged in seller

    @GetMapping("/seller/current")
    public ResponseEntity<Seller> getLoggedInSellerHandler(@RequestHeader("token") String token){

        Seller getSeller = sellerService.getCurrentlyLoggedInSeller(token);

        return ResponseEntity.ok(getSeller);
    }

    //Update the seller..............................


    @PutMapping("/seller")
    public ResponseEntity<Seller> updateSellerHandler(@RequestBody Seller seller, @RequestHeader("token") String token){
        Seller updatedseller= sellerService.updateSeller(seller, token);

        return ResponseEntity.ok(updatedseller);

    }


    @PutMapping("/seller/update/mobile")
    public ResponseEntity<Seller> updateSellerMobileHandler(@Valid @RequestBody SellerDTO sellerDto, @RequestHeader("token") String token){
        Seller updatedseller= sellerService.updateSellerMobile(sellerDto, token);

        return ResponseEntity.ok(updatedseller);
    }


    @PutMapping("/seller/update/password")
    public ResponseEntity<SessionDTO> updateSellerPasswordHandler(@Valid @RequestBody SellerDTO sellerDto, @RequestHeader("token") String token){
        return ResponseEntity.ok(sellerService.updateSellerPassword(sellerDto, token));
    }

    @DeleteMapping("/seller/{sellerId}")
    public ResponseEntity<Void> deleteSellerByIdHandler(@PathVariable("sellerId") Integer sellerId, @RequestHeader("token") String token){

        sellerService.deleteSellerById(sellerId, token);

        return ResponseEntity.noContent().build();

    }


}
