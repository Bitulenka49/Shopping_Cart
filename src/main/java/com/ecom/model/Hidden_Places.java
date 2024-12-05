package com.ecom.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;




@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="hidden_places")
public class Hidden_Places 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long slno;
   private String placeName;
   private double placeLat;
   private double placeLong;

}
