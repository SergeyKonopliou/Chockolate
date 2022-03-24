package com.chockolate.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.chockolate.model.User;
import com.chockolate.repository.UserRepository;

/**
 * UserDetailsService, используется чтобы создать UserDetails объект путем
 * реализации единственного метода этого интерфейса. UserDetails предоставляет
 * необходимую информацию для построения объекта Authentication из DAO объектов
 * приложения или других источников данных системы безопасности. Authentication
 * представляет пользователя (Principal) с точки зрения Spring Security.
 * UserDetailsServiceImpl работает с методами репозиторияж.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Метод вызывающий метод репозитория для поиска объекта по названию и
	 * возвращения UserDetails объекта
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}

		return new MyUserDetails(user);
	}

	/**
	 * Метод вызывающий метод репозитория для загрузки всех объектов из базы данных
	 */
	public List<User> allUsers() {
		return userRepository.findAll();
	}

	/**
	 * Метод вызывающий метод репозитория для сохранения или изменения объекта
	 */
	public boolean saveUser(User user) {
		System.out.println(user.getUsername());
		User userFromDB = userRepository.getUserByUsername(user.getUsername());

		if (userFromDB != null) {
			return false;
		}

		user.setRole("ROLE_USER");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		userRepository.save(user);
		List<User> users = new ArrayList<User>();
		users = allUsers();
		System.out.println(users);
		return true;
	}

	/**
	 * Метод вызывающий метод репозитория для удаления объекта
	 */
	public boolean deleteUser(Long userId) {
		if (userRepository.findById(userId).isPresent()) {
			userRepository.deleteById(userId);
			return true;
		}
		return false;
	}

}
