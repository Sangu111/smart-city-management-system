package com.smartcity.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartcity.dao.PasswordUtil;
import com.smartcity.dao.UserDAO;
import com.smartcity.model.User;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (username == null || username.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            response.sendRedirect("jsp/register.jsp?error=missing_fields");
            return;
        }
        
        // Check password confirmation
        if (confirmPassword != null && !password.equals(confirmPassword)) {
            response.sendRedirect("jsp/register.jsp?error=password_mismatch");
            return;
        }
        
        String role = "citizen";
        String passwordHash = PasswordUtil.hashPassword(password);
        User user = new User(0, username.trim(), email.trim(), passwordHash, role);
        UserDAO userDAO = new UserDAO();
        
        try {
            System.out.println("🔄 Attempting to register user: " + username + " with email: " + email);
            boolean success = userDAO.registerUser(user);
            if (success) {
                System.out.println("✅ Registration successful for: " + username);
                response.sendRedirect("jsp/login.jsp?msg=registered");
            } else {
                System.out.println("❌ Registration failed for: " + username + " - user/email already exists");
                response.sendRedirect("jsp/register.jsp?error=username_exists");
            }
        } catch (Exception e) {
            System.err.println("❌ Error during registration: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("jsp/register.jsp?error=system_error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("jsp/register.jsp");
    }
}
