package com.masai.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating Seller entity with validation.
 * This DTO automatically stays in sync with Seller entity through MapStruct.
 * When you add/remove fields from Seller entity, just recompile and this DTO will reflect changes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSellerDTO {

    // ID field is optional for updates (might be provided in path)
    private Integer sellerId;

    @NotNull(message = "Please enter the first name")
    @NotBlank(message = "First name cannot be blank")
    @Pattern(regexp = "[A-Za-z\\s]+", message = "First Name should contain alphabets only")
    private String firstName;

    @NotNull(message = "Please enter the last name")
    @NotBlank(message = "Last name cannot be blank")
    @Pattern(regexp = "[A-Za-z\\s]+", message = "Last Name should contain alphabets only")
    private String lastName;

    @NotNull(message = "Please enter your mobile number")
    @NotBlank(message = "Mobile number cannot be blank")
    @Pattern(regexp = "[6789]{1}[0-9]{9}", message = "Enter a valid Mobile Number")
    private String mobile;

    @NotNull(message = "Please enter your email")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Please enter a valid email address")
    private String emailId;

    // Note: password is intentionally excluded from update DTO for security
    // Password updates should go through a separate endpoint with proper verification
}
