package com.masai.security;

import java.util.ArrayList;
import java.util.Optional;

import com.masai.model.Customer;
import com.masai.model.Seller;
import com.masai.repository.CustomerRepository;
import com.masai.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try finding as customer by mobile or email
        Optional<Customer> customerOpt = customerRepository.findByMobileNoOrEmailId(username, username);
        if (customerOpt.isPresent()) {
            Customer c = customerOpt.get();
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
            return new User(c.getMobileNo() != null ? c.getMobileNo() : c.getEmailId(), c.getPassword(), authorities);
        }

        // Try seller by mobile
        Optional<Seller> sellerOpt = sellerRepository.findByMobile(username);
        if (sellerOpt.isPresent()) {
            Seller s = sellerOpt.get();
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            return new User(s.getMobile() != null ? s.getMobile() : (s.getEmailId() == null ? "" : s.getEmailId()), s.getPassword(), authorities);
        }

        throw new UsernameNotFoundException("User not found with identifier: " + username);
    }
}

