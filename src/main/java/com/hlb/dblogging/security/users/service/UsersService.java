package com.hlb.dblogging.security.users.service;

import java.util.HashSet;
import java.util.List;

import com.hlb.dblogging.jpa.model.Users;

public interface UsersService {

	  Users create(Users users);
	  List<Users> findAll();
	  List<Users> findAllUsers();
	  List<String> findUsersList(String username);
	  boolean findUserExistInApplication(String username);
	  HashSet<String>	getAccessRightsMapForUser(String username);
	  Users changePassword(Users users, String oldPassword, String newPassword);
	  Users update(Users users);
	  Users findByUsername(String username);
	  int deleteUser(String username);
	  Users delete(int id);
	  
}
