package com.masai.dto;

import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;

import com.masai.model.CreditCard;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class OrderDTO {
	
	@NotNull
	@Embedded
	private CreditCard cardNumber;
	@NotNull
	private String addressType;
}
