// package com.example.demo;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/api")
// public class AuthController {
// 	@Autowired
// 	private UserService userService;

// 	@Autowired
// 	private AuthenticationManager authenticationManager;

// 	@PostMapping("/register")
// 	public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
// 		User user = userService.registerUser(request.getUsername(), request.getPassword(), "USER");
// 		return ResponseEntity.ok(user);
// 	}

// 	@PostMapping("/login")
// 	public ResponseEntity<String> login(@RequestBody LoginRequest request) {
// 		Authentication auth = authenticationManager.authenticate(
// 				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
// 		if (auth.isAuthenticated()) {
// 			return ResponseEntity.ok("Login successful");
// 		}
// 		return ResponseEntity.status(401).body("Login failed");
// 	}
// }

// class RegisterRequest {
// 	private String username;
// 	private String password;

// 	// Getters and setters
// 	public String getUsername() {
// 		return username;
// 	}

// 	public void setUsername(String username) {
// 		this.username = username;
// 	}

// 	public String getPassword() {
// 		return password;
// 	}

// 	public void setPassword(String password) {
// 		this.password = password;
// 	}
// }

// class LoginRequest {
// 	private String username;
// 	private String password;

// 	// Getters and setters
// 	public String getUsername() {
// 		return username;
// 	}

// 	public void setUsername(String username) {
// 		this.username = username;
// 	}

// 	public String getPassword() {
// 		return password;
// 	}

// 	public void setPassword(String password) {
// 		this.password = password;
// 	}
// }

package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
    
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            if (auth.isAuthenticated()) {
                return ResponseEntity.ok("Login successful");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
        return ResponseEntity.status(401).body("Login failed");
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> userRequest) {
        String username = userRequest.get("username");
        String password = userRequest.get("password");
        String role = userRequest.get("role");

        if (role == null || role.isEmpty()) {
            role = "USER"; // Default role
        }

        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Username already exists");
            return ResponseEntity.badRequest().body(response);
        }

        User newUser = userService.registerUser(username, password, role);
        return ResponseEntity.ok(newUser);
    }

    // Get user by username
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}