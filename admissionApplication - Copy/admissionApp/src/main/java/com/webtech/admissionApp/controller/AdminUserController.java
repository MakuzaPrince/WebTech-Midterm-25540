package com.webtech.admissionApp.controller;

import com.webtech.admissionApp.model.User;
import com.webtech.admissionApp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.multipart.MultipartFile;

import java.io.*;   

@Controller

public class AdminUserController {

    private final UserService userService; // Assuming you have a UserService to handle user operations


    public AdminUserController(UserService userService) {
        this.userService = userService;

    }
    
    @GetMapping("/admin/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User()); // Create a new User object
        return "add-user"; // Return the add user template
    }

    @PostMapping("/admin/users")
    public String addUser (@ModelAttribute User user) {
        userService.registerUser (user); // Save the user using your service
        return "redirect:/admin"; // Redirect to the admin dashboard after saving
    }

    @PostMapping("/admin/users/delete/{id}")
    public String deleteUser (@PathVariable Long id) {
        userService.deleteUser (id); // Call the service to delete the user
        return "redirect:/admin"; // Redirect to the admin dashboard after deletion
    }

    @GetMapping("/admin/users/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id); // Fetch the user by ID
        model.addAttribute("user", user); // Add the user to the model
        return "edit-user"; // Return the edit user template
    }

    @PostMapping("/admin/users/update")
    public String updateUser (@ModelAttribute User user) {
        userService.updateUser (user); // Call the service to update the user
        return "redirect:/admin"; // Redirect to the admin dashboard after updating
    }

    @GetMapping("/admin/search")
    public String showSearchForm() {
        return "search-user"; // Return the search user template
    }

    @GetMapping("/admin/search/results")
    public String searchUsers(@RequestParam(required = false) String username,
                              @RequestParam(required = false) String email,
                              Model model) {
        List<User> users = userService.searchUsers(username, email); // Call the service to search for users
        model.addAttribute("users", users); // Add the list of users to the model
        return "teacher"; // Return the template that displays the list of users
    }



    @GetMapping("/admin/download/users")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadUsers() throws IOException {
        List<User> users = userService.getAllUsers(); // Fetch all users from the service

        // Create CSV content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Write CSV header
        writer.println("ID,Username,Email"); // Adjust according to your User fields

        // Write user data
        for (User  user : users) {
            writer.printf("%d,%s,%s%n", user.getId(), user.getUsername(), user.getEmail()); // Adjust according to your User fields
        }
        writer.flush();
        writer.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        // Set the content type and attachment header
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }



    @GetMapping("/admin/upload/furniture")
    public String showUploadForm() {
        return "upload-furniture"; // Return the upload form template
    }
}