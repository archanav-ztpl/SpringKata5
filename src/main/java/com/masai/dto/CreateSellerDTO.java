package com.masai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerDTO {

    @NotNull(message = "Please enter the first name")
    @NotBlank(message = "First name cannot be blank")
    @Pattern(regexp = "[A-Za-z\\s]+", message = "First Name should contain alphabets only")
    private String firstName;

    @NotNull(message = "Please enter the last name")
    @NotBlank(message = "Last name cannot be blank")
    @Pattern(regexp = "[A-Za-z\\s]+", message = "Last Name should contain alphabets only")
    private String lastName;

    @NotNull(message = "Please enter a password")
    @NotBlank(message = "Password cannot be blank")
    @Pattern(regexp = "[A-Za-z0-9!@#$%^&*_]{8,15}", message = "Please Enter a valid Password (8-15 characters, alphanumeric and special characters allowed)")
    private String password;

    @NotNull(message = "Please enter your mobile number")
    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter a valid Mobile Number")
    private String mobile;

    @NotNull(message = "Please enter your email")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please enter a valid email address")
    private String emailId;
}
