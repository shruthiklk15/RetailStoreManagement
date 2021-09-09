package com.myapp.spring;

public class ByCryptPasswordEncoder {
	public static void main(String[] args) {
		
		
		 ByCryptPasswordEncoder passwordEncoder = new ByCryptPasswordEncoder();
		    String password = "chaya";
		    String encodedPassword = passwordEncoder.encode(password);

		    System.out.println();
		    System.out.println("Password is         : " + password);
		    System.out.println("Encoded Password is : " + encodedPassword);
		    System.out.println();

		    boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
		  //  System.out.println("Password : " + password + "   isPasswordMatch    : " + isPasswordMatch);
	}

	private String encode(String password) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean matches(String password, String encodedPassword) {
		// TODO Auto-generated method stub
		return false;
	}

}