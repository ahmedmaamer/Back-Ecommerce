package com.example.ecommerce.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignupRequest {

	private String username;
	private String unom;
    private String uprenom;
    private long utel;
	private String email;
	private String password;
	
	private List<String> roles;
	

	
}
