package com.example.webchat;
import com.example.webchat.Controllers.ExerciseController;
import com.example.webchat.Controllers.TrainingController;
import com.example.webchat.Controllers.UserController;
import com.example.webchat.DTOs.ExerciseDTO;
import com.example.webchat.DTOs.TrainingDTO;
import com.example.webchat.DTOs.UserDTO;
import com.example.webchat.Models.Exercise;
import com.example.webchat.Models.Training;
import com.example.webchat.Models.User;
import com.example.webchat.Repositories.UserRepository;
import com.example.webchat.Services.ExerciseService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class WebchatApplicationTests {

	@Autowired
	private ExerciseController exerciseController;
	@Autowired
	private UserController userController;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private TrainingController trainingController;

	@Test
	void getAllUsers_WithValidToken_ShouldReturnListOfUsers() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszak");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		String validToken = loginResponse.getBody();
		System.out.println(validToken);

		UserDTO userRequest = new UserDTO();
		userRequest.setToken(validToken);

		ResponseEntity<List<User>> response = userController.getAllUsers(userRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void getUserByEmail_WithValidToken_ShouldReturnUser() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszak");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		String validToken = loginResponse.getBody();

		UserDTO userRequest = new UserDTO();
		userRequest.setToken(validToken);

		ResponseEntity<Optional<User>> response = userController.getUserByEmail("dyszak@dyszak.pl", userRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void login_WithInvalidCredentials_ShouldReturnNotFound() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszakensYYY");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		assertEquals(HttpStatus.NOT_FOUND, loginResponse.getStatusCode());
	}

	@Test
	void getAllTrainings_WithValidToken_ShouldReturnTrainings() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszak");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		String validToken = loginResponse.getBody();

		TrainingDTO trainingRequest = new TrainingDTO();
		trainingRequest.setToken(validToken);

		ResponseEntity<List<Training>> response = trainingController.getAllTrainings(trainingRequest);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void findExercise_WithInvalidCredentials_ShouldReturnForbidden() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszakens");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		String invalidToken = loginResponse.getBody();

		ExerciseDTO exerciseRequest = new ExerciseDTO();
		exerciseRequest.setToken(invalidToken);

		String name = "testExercise";

		ResponseEntity<List<Exercise>> exerciseResponse = exerciseController.searchExercises(name, exerciseRequest);

		assertEquals(HttpStatus.FORBIDDEN, exerciseResponse.getStatusCode());
	}

	@Test
	void createExercise_WithValidExerciseData_ShouldReturnCreatedExercise() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszak");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		String validToken = loginResponse.getBody();

		ExerciseDTO exerciseRequest = new ExerciseDTO();
		exerciseRequest.setName("testName123");
		exerciseRequest.setDescription("testDesc321");
		exerciseRequest.setCategory("testCat123");
		exerciseRequest.setToken(validToken);

		ResponseEntity<Exercise> response = exerciseController.createExercise(exerciseRequest);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	void deleteExercise_WithValidCredentials_ShouldReturnOk() {
		UserDTO loginRequest = new UserDTO();
		loginRequest.setUsername("dyszak");
		loginRequest.setPassword("dyszak");

		ResponseEntity<String> loginResponse = userController.login(loginRequest, null);

		String validToken = loginResponse.getBody();

		ExerciseDTO exerciseRequest = new ExerciseDTO();
		exerciseRequest.setToken(validToken);

		String id = "65676a7467fba73754b7b8cf";

		ResponseEntity<Exercise> exerciseResponse = exerciseController.deleteExerciseById(id, exerciseRequest);

		assertEquals(HttpStatus.OK, exerciseResponse.getStatusCode());
	}

//	@Test
//	void createUser_ValidUserData_ShouldReturnCreatedUser() {
//		UserDTO userRequest = new UserDTO();
//		userRequest.setEmail("test@example1dd.com");
//		userRequest.setUsername("testUser1dd");
//		userRequest.setPassword("testPassword");
//
//		ResponseEntity<User> response = userController.createUser(userRequest);
//
//		assertEquals(HttpStatus.CREATED, response.getStatusCode());
//	}
	@Test
	void createUser_ValidUserData_ShouldReturnCreatedUser() {
		UserDTO userRequest = new UserDTO();
		userRequest.setEmail("test@example1dd.com");
		userRequest.setUsername("testUser1dd");
		userRequest.setPassword("testPassword");

		ResponseEntity<User> response = userController.createUser(userRequest);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

}
