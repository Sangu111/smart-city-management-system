package com.smartcity.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.smartcity.dao.ComplaintDAO;
import com.smartcity.model.Complaint;
import com.smartcity.model.User;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        
        ComplaintDAO dao = new ComplaintDAO();
        List<Complaint> complaints = dao.getAllComplaints();

        request.setAttribute("complaints", complaints);
        request.getRequestDispatcher("jsp/admin_dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        String status = request.getParameter("status");
        String assignedTo = request.getParameter("assigned_to");
        ComplaintDAO dao = new ComplaintDAO();
        dao.updateComplaintStatus(id, status, assignedTo);
        response.sendRedirect("admin");
    }
}
