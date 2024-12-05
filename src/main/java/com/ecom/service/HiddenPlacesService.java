package com.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.model.Hidden_Places;
import com.ecom.repository.HiddenPlacesRepo;

@Service
public class HiddenPlacesService 
{
	@Autowired
	private HiddenPlacesRepo hiddenPlacesRepo;
	
	public Hidden_Places saveHiddenPlace(Hidden_Places hidden_Places) {
		return hiddenPlacesRepo.save(hidden_Places);
	}
	
	public List<Hidden_Places> getAll(){
		return hiddenPlacesRepo.findAll();
	}

}
