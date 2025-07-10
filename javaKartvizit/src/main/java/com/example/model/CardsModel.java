package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table (name = "cards")
@Data
public class CardsModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "full_name", nullable = false, length = 100)
	private String fullName;
	@Column(name = "job_title", length = 50)
	private String jobTitle;
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;
	@Column(name = "email", length = 100)
	private String email;
	@Column(name = "address", columnDefinition = "TEXT")
	private String address;
	
	
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersModel user;
	
    public CardsModel() {}
    
	

}
