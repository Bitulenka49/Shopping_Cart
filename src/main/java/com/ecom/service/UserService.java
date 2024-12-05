package com.ecom.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ecom.model.UserDtls;

public interface UserService {
	
	public UserDtls saveUser(UserDtls user);
	
	public UserDtls getUserByEmail(String email);
	
	public List<UserDtls> getUsers(String role);

	

	public Boolean updateAccountStatus(Integer id, Boolean status);
	
	public UserDtls updateUserProfile(UserDtls user,MultipartFile img);
	
	public UserDtls updateUser(UserDtls user);

}
