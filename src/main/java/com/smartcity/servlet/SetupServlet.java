package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.smartcity.util.DatabaseInitializer;

@WebServlet("/setup-db")
public class SetupServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>🔧 Database Setup</h1>");
        
        try {
            // Manually trigger database initialization
            DatabaseInitializer initializer = new DatabaseInitializer();
            initializer.contextInitialized(null);
            
            out.println("<p style='color: green;'>✅ Database setup completed!</p>");
            out.println("<p><strong>Test Credentials:</strong></p>");
            out.println("<ul>");
            out.println("<li>Admin: admin / admin123</li>");
            out.println("<li>Citizen: citizen1 / test</li>");
            out.println("</ul>");
            out.println("<a href='/' style='background: #007bff; color: white; padding: 10px; text-decoration: none;'>Go to Login</a>");
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>❌ Setup failed: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("</body></html>");
    }
}