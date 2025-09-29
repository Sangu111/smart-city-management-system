package com.smartcity.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.smartcity.dao.PasswordUtil;
import com.smartcity.dao.UserDAO;
import com.smartcity.model.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("🔐 Login attempt - Username: " + username + ", Password: " + password);
        
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username);
        
        if (user != null) {
            System.out.println("👤 User found: " + user.getUsername() + ", Role: " + user.getRole());
            String inputHash = PasswordUtil.hashPassword(password);
            String storedHash = user.getPasswordHash();
            System.out.println("🔑 Input hash: " + inputHash);
            System.out.println("🔑 Stored hash: " + storedHash);
            System.out.println("🔍 Hashes match: " + storedHash.equals(inputHash));
            
            if (storedHash.equals(inputHash)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                System.out.println("🎉 Login successful! User role: " + user.getRole());
                if ("admin".equals(user.getRole())) {
                    System.out.println("🔄 Redirecting to admin dashboard");
                    response.sendRedirect("admin");
                } else {
                    System.out.println("🔄 Redirecting to complaint dashboard");
                    response.sendRedirect("complaint");
                }
            } else {
                System.out.println("❌ Password mismatch - redirecting to login");
                response.sendRedirect("jsp/login.jsp?error=1");
            }
        } else {
            System.out.println("❌ User not found: " + username);
            response.sendRedirect("jsp/login.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("jsp/login.jsp");
    }
}
