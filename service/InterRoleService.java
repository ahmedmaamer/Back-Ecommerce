package com.example.ecommerce.service;

import com.example.ecommerce.entities.Role;

public interface InterRoleService {
	public Role addRole(Role role);
	
	public String deleteRole(Long id);
}
