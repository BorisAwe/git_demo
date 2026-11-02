package com.example.crs.controller;
import com.example.crs.model.Student; // import Student 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(origins = "http://localhost:5173") // Stay consistent with CourseController for Vue
@RestController
@RequestMapping("/api")
public class StudentController {

    // Task 1: Simulate "in-memory" Student "Database"
    private static final Map<String, Student> studentDatabase = new ConcurrentHashMap<>();

    // generate a test account 
    static {
        Student defaultStudent = new Student();
        defaultStudent.setStudentId("1001");
        defaultStudent.setPassword("123456");
        defaultStudent.setName("Test Student");
        defaultStudent.setEmail("test@school.edu");
        studentDatabase.put("1001", defaultStudent);
    }

    // ==========================================
    // Please finish the codes for Step2/3, where you can use AI
    // ==========================================
    
    // @PostMapping("/login")
    // public ResponseEntity<?> login(...) { ... }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
    String studentId = loginRequest.get("studentId");
    String password = loginRequest.get("password");
    // 1. Look up the studentId in studentDatabase
    Student student = studentDatabase.get(studentId);
    // 2. Verify if the student exists AND the password matches
    if(student != null && student.getPassword().equals(password)) {
    	// 3. If valid, return ResponseEntity.ok(...) with their real name and id
    	Map<String, String> response = new HashMap<>();
    	response.put("studentId", student.getStudentId());
    	response.put("name", student.getName());
    	
    	return ResponseEntity.ok(response);
    }
    // 4. If invalid, return HttpStatus.UNAUTHORIZED
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    		.body("Invalid Student ID or password");
    // TODO: Write your implementation here
    }

    // @PostMapping("/register")
    // public ResponseEntity<?> registerStudent(...) { ... }
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody Student newStudent) {
    // 1. Check if studentId already exists using studentDatabase.containsKey()
    	if(studentDatabase.containsKey(newStudent.getStudentId())) {
    		// 2. If it exists, return ResponseEntity.status(HttpStatus.CONFLICT).body(...)
    		return ResponseEntity.status(HttpStatus.CONFLICT)
    				.body("Student ID"+ newStudent.getStudentId() + " already exists.");
    	}
    	// 3. If it doesn't exist, save it to your map:
    	studentDatabase.put(newStudent.getStudentId(), newStudent);
    	// 4. Return HttpStatus.CREATED response.
    	return ResponseEntity.status(HttpStatus.CREATED).body(newStudent);
    // TODO: Write your implementation here
    }
}
