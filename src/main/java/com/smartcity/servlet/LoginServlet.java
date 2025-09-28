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
        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsername(username);
        
        if (user != null) {
            String inputHash = PasswordUtil.hashPassword(password);
            String storedHash = user.getPasswordHash();
            
            if (storedHash.equals(inputHash)) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect("admin");
                } else {
                    response.sendRedirect("complaint");
                }
            } else {
                response.sendRedirect("jsp/login.jsp?error=1");
            }
        } else {
            response.sendRedirect("jsp/login.jsp?error=1");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("jsp/login.jsp");
    }
}
