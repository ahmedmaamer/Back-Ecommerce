package com.example.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.entities.Role;
import com.example.ecommerce.service.RoleService;
@CrossOrigin(origins="http://localhost:4200")
@RestController
public class RoleController {
    @Autowired 
    RoleService roleserv;
    
    @PostMapping(value = "/addrole")
    public Role addRole(@RequestBody Role role)
    {
      return roleserv.addRole(role);	
    }
    
    @DeleteMapping(value = "/deleterole/{id}")
    public String deleteRole(@PathVariable Long id)
    {
      return roleserv.deleteRole(id);	
    }
    
}
