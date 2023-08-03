package com.example.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entities.Role;
import com.example.ecommerce.repository.RoleRepository;

@Service
public class RoleService implements InterRoleService{
    
	@Autowired
    RoleRepository rolerep;
	
	
	@Override
	public Role addRole(Role role) {
		return rolerep.save(role);
	}


	@Override
	public String deleteRole(Long id) {
		String ch="";
		rolerep.deleteById(id);
		ch="role deleted successfuly !!!";
		return ch;
	}

}
