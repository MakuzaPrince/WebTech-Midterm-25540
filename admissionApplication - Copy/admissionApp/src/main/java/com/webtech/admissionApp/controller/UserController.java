package com.webtech.admissionApp.controller;

import com.webtech.admissionApp.model.Role;
import com.webtech.admissionApp.model.User;
import com.webtech.admissionApp.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Template name for registration
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register"; // If form validation fails, return the registration page
        }

        // Register user
        userService.registerUser(user);
        model.addAttribute("message", "Registration successful! You can log in now.");
        return "login"; // Redirect to login page
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Template name for login
    }

    @PostMapping("/login")
    public String loginUser(String username, String password, HttpSession session, Model model) {
        User user = userService.loginUser(username);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Show login page again with error message
        }

        // Set user information in session
        session.setAttribute("loggedInUser", user);

        // Redirect based on the role
        if (user.getRole() == Role.ROLE_ADMIN) {
            return "redirect:/admin"; // Admin page
        } else if (user.getRole() == Role.ROLE_TEACHER) {
            return "redirect:/teacher"; // Teacher page
        } else if (user.getRole() == Role.ROLE_USER) {
            return "redirect:/student"; // Student page
        }

        // Default redirect if no role matches (optional safety fallback)
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        List<User> users = userService.getAllUsers(); // Fetch all users
        model.addAttribute("users", users); // Add users to the model
        return "admin"; // Return the name of your admin dashboard template
    }



    @GetMapping("/teacher")
    public String sellerPage(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.ROLE_TEACHER) {
            return "redirect:/login"; // Redirect to login if not logged in or not seller
        }
        return "teacher"; // Seller dashboard template
    }

    @GetMapping("/student")
    public String customerPage(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.ROLE_USER) {
            return "redirect:/login"; // Redirect to log in if not logged in or not customer
        }
        return "student"; // Customer dashboard template
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // Invalidate session on logout

        // Add a flash attribute for the logout message
        redirectAttributes.addFlashAttribute("message", "You are logged out.");

        return "redirect:/login"; // Redirect to login after logout
    }

}
