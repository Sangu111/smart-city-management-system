package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.smartcity.model.User;

@WebServlet("/test-login")
public class TestLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Test Login</title></head><body>");
        out.println("<h2>🧪 Login Test Form</h2>");
        
        // Check current session
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                out.println("<div style='background: #d4edda; padding: 10px; border-radius: 5px; margin: 10px 0;'>");
                out.println("✅ You are already logged in as: <strong>" + user.getUsername() + "</strong> (" + user.getRole() + ")");
                out.println("</div>");
                if ("admin".equals(user.getRole())) {
                    out.println("<p><a href='admin'>Go to Admin Dashboard</a></p>");
                } else {
                    out.println("<p><a href='complaint'>Go to Complaint Dashboard</a></p>");
                }
                out.println("<p><a href='logout'>Logout</a></p>");
            }
        }
        
        out.println("<form method='post' action='login' style='max-width: 400px;'>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Username:</label><br>");
        out.println("<input type='text' name='username' value='admin' style='width: 100%; padding: 8px;'>");
        out.println("</div>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Password:</label><br>");
        out.println("<input type='password' name='password' value='admin123' style='width: 100%; padding: 8px;'>");
        out.println("</div>");
        out.println("<button type='submit' style='background: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px;'>Login as Admin</button>");
        out.println("</form>");
        
        out.println("<form method='post' action='login' style='max-width: 400px; margin-top: 20px;'>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<input type='hidden' name='username' value='citizen1'>");
        out.println("<input type='hidden' name='password' value='test'>");
        out.println("</div>");
        out.println("<button type='submit' style='background: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 5px;'>Login as Citizen</button>");
        out.println("</form>");
        
        out.println("<p><a href='debug-login'>Debug Users</a> | <a href='/'>Home</a></p>");
        out.println("</body></html>");
    }
}