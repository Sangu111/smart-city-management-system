package com.smartcity.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.smartcity.dao.ComplaintDAO;
import com.smartcity.model.Complaint;
import com.smartcity.model.User;

@WebServlet("/complaint")
@MultipartConfig
public class ComplaintServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        String category = request.getParameter("category");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        Part photoPart = request.getPart("photo");
        String photoUrl = null;
        if (photoPart != null && photoPart.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + photoPart.getSubmittedFileName();
            String uploadPath = getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            photoPart.write(uploadPath + File.separator + fileName);
            photoUrl = "uploads/" + fileName;
        }
        Complaint complaint = new Complaint(0, user.getId(), category, title, description, location, photoUrl, "Submitted", "", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        ComplaintDAO dao = new ComplaintDAO();
        boolean success = dao.submitComplaint(complaint);
        if (success) {
            response.sendRedirect("complaint?msg=complaint_submitted");
        } else {
            response.sendRedirect("jsp/complaint_form.jsp?error=submission_failed");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }
        ComplaintDAO dao = new ComplaintDAO();
        List<Complaint> complaints = dao.getComplaintsByUser(user.getId());
        request.setAttribute("complaints", complaints);
        request.getRequestDispatcher("jsp/dashboard.jsp").forward(request, response);
    }
}
