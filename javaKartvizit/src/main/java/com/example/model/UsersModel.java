package com.example.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users_table")
@Data

public class UsersModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String name;
	
	String password;
	
	String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return name;
	}

	public void setLogin(String login) {
		this.name = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, id, name, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsersModel other = (UsersModel) obj;
		return Objects.equals(email, other.email) && Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password);
	}

	@Override
	public String toString() {
		return "UsersModel [id=" + id + ", login=" + name + ", email=" + email + "]";
	}
	
	
}
