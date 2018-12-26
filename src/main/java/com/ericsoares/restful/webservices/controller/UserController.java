package com.ericsoares.restful.webservices.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ericsoares.restful.webservices.dao.UserDao;
import com.ericsoares.restful.webservices.domain.User;
import com.ericsoares.restful.webservices.exceptions.UserNotFoundException;

@RestController
public class UserController {

	@Autowired
	private UserDao service;

	@GetMapping("/users")
	public List<User> retrieveAllUser() {
		return service.findAll();
	}

	@GetMapping("/user/{id}")
	public User retrieveUser(@PathVariable Integer id) {
		User user = service.findById(id);
		if (user == null) {
			throw new UserNotFoundException("id - " + id);
		}
		return user;
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/user/{id}")
	public void deleteUser(@PathVariable Integer id) {
		User user = service.deleteById(id);
		if (user == null) {
			throw new UserNotFoundException("id - " + id);
		}
	}
}
