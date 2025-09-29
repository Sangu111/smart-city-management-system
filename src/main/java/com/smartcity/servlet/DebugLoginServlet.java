package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartcity.dao.DBUtil;

@WebServlet("/debug-login")
public class DebugLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Debug Login System</title></head><body>");
        out.println("<h2>🔍 Smart City - Debug Login System</h2>");
        
        try (Connection conn = DBUtil.getConnection()) {
            out.println("<h3>✅ Database Connection: SUCCESS</h3>");
            
            // Check if users table exists and show all users
            PreparedStatement ps = conn.prepareStatement("SELECT username, password, email, role FROM users ORDER BY id");
            ResultSet rs = ps.executeQuery();
            
            out.println("<h3>👥 Users in Database:</h3>");
            out.println("<table border='1' style='border-collapse:collapse;'>");
            out.println("<tr><th>Username</th><th>Password Hash</th><th>Email</th><th>Role</th></tr>");
            
            boolean hasUsers = false;
            while (rs.next()) {
                hasUsers = true;
                out.println("<tr>");
                out.println("<td>" + rs.getString("username") + "</td>");
                out.println("<td>" + rs.getString("password").substring(0, 20) + "...</td>");
                out.println("<td>" + rs.getString("email") + "</td>");
                out.println("<td>" + rs.getString("role") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            
            if (!hasUsers) {
                out.println("<p style='color:red;'>❌ No users found in database!</p>");
                out.println("<p><a href='setup-db'>Click here to setup database</a></p>");
            } else {
                out.println("<h3>🔑 Test Login Credentials:</h3>");
                out.println("<ul>");
                out.println("<li><strong>Admin:</strong> admin / admin123</li>");
                out.println("<li><strong>Citizen:</strong> citizen1 / test</li>");
                out.println("</ul>");
                out.println("<p><a href='jsp/login.jsp'>Go to Login Page</a></p>");
            }
            
        } catch (Exception e) {
            out.println("<h3 style='color:red;'>❌ Database Error:</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("</body></html>");
    }
}