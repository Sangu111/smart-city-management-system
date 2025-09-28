<%@ page import="java.util.List" %>
<%@ page import="com.smartcity.model.Complaint" %>
<%@ page import="com.smartcity.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect("login.jsp");
    return;
}
List<Complaint> userComplaints = (List<Complaint>) request.getAttribute("complaints");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Citizen Dashboard - Smart City</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f8f9fa;
            line-height: 1.6;
        }
        
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1rem 2rem;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .header-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            max-width: 1200px;
            margin: 0 auto;
        }
        
        .header h1 {
            font-size: 1.8rem;
            font-weight: 300;
        }
        
        .header-actions {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        
        .btn {
            padding: 0.5rem 1rem;
            border-radius: 8px;
            text-decoration: none;
            transition: all 0.3s ease;
            font-weight: 500;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
        }
        
        .btn-primary {
            background: rgba(255, 255, 255, 0.2);
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.3);
        }
        
        .btn-primary:hover {
            background: rgba(255, 255, 255, 0.3);
            color: white;
        }
        
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 2rem;
        }
        
        .alert {
            padding: 1rem 1.5rem;
            border-radius: 8px;
            margin-bottom: 2rem;
            display: flex;
            align-items: center;
            gap: 0.75rem;
            font-weight: 500;
        }
        
        .alert-success {
            background: linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%);
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert i {
            font-size: 1.2rem;
        }
        
        .welcome-section {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            margin-bottom: 2rem;
        }
        
        .welcome-section h2 {
            color: #333;
            margin-bottom: 1rem;
        }
        
        .welcome-section p {
            color: #666;
            font-size: 1.1rem;
        }
        
        .quick-actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .action-card {
            background: white;
            padding: 2rem;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            text-align: center;
            transition: transform 0.3s ease;
        }
        
        .action-card:hover {
            transform: translateY(-5px);
        }
        
        .action-card i {
            font-size: 3rem;
            margin-bottom: 1rem;
        }
        
        .action-card.submit {
            border-top: 4px solid #28a745;
        }
        
        .action-card.submit i {
            color: #28a745;
        }
        
        .action-card.track {
            border-top: 4px solid #17a2b8;
        }
        
        .action-card.track i {
            color: #17a2b8;
        }
        
        .action-card h3 {
            color: #333;
            margin-bottom: 1rem;
        }
        
        .action-card p {
            color: #666;
            margin-bottom: 1.5rem;
        }
        
        .btn-action {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s ease;
        }
        
        .btn-action:hover {
            transform: translateY(-2px);
            color: white;
        }
        
        .complaints-section {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }
        
        .section-header {
            background: #f8f9fa;
            padding: 1.5rem;
            border-bottom: 1px solid #e9ecef;
        }
        
        .section-header h3 {
            color: #333;
            font-size: 1.4rem;
            font-weight: 500;
        }
        
        .complaints-grid {
            padding: 1.5rem;
            display: grid;
            gap: 1rem;
        }
        
        .complaint-card {
            border: 1px solid #e9ecef;
            border-radius: 8px;
            padding: 1.5rem;
            transition: all 0.3s ease;
        }
        
        .complaint-card:hover {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }
        
        .complaint-header {
            display: flex;
            justify-content: between;
            align-items: flex-start;
            margin-bottom: 1rem;
        }
        
        .complaint-title {
            color: #333;
            font-size: 1.2rem;
            font-weight: 600;
            flex-grow: 1;
        }
        
        .status-badge {
            padding: 0.25rem 0.75rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
            text-transform: uppercase;
            margin-left: 1rem;
        }
        
        .status-submitted {
            background: #fff3cd;
            color: #856404;
        }
        
        .status-progress {
            background: #d1ecf1;
            color: #0c5460;
        }
        
        .status-resolved {
            background: #d4edda;
            color: #155724;
        }
        
        .status-rejected {
            background: #f8d7da;
            color: #721c24;
        }
        
        .complaint-meta {
            display: flex;
            gap: 2rem;
            margin-bottom: 1rem;
            font-size: 0.9rem;
            color: #666;
        }
        
        .complaint-description {
            color: #555;
            margin-bottom: 1rem;
        }
        
        .complaint-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-top: 1rem;
            border-top: 1px solid #e9ecef;
            font-size: 0.9rem;
            color: #666;
        }
        
        .empty-state {
            padding: 3rem;
            text-align: center;
            color: #6c757d;
        }
        
        .empty-state i {
            font-size: 3rem;
            margin-bottom: 1rem;
            color: #dee2e6;
        }
        
        @media (max-width: 768px) {
            .header-content {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }
            
            .container {
                padding: 0 1rem;
            }
            
            .complaint-header {
                flex-direction: column;
                align-items: stretch;
            }
            
            .status-badge {
                margin-left: 0;
                margin-top: 0.5rem;
                align-self: flex-start;
            }
            
            .complaint-meta {
                flex-direction: column;
                gap: 0.5rem;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="header-content">
            <h1><i class="fas fa-city"></i> Smart City Portal</h1>
            <div class="header-actions">
                <span><i class="fas fa-user"></i> Welcome, <%= user.getUsername() %></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-primary">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>
        </div>
    </header>
    
    <div class="container">
        <% String msg = request.getParameter("msg"); 
           if ("complaint_submitted".equals(msg)) { %>
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> Your complaint has been submitted successfully!
            </div>
        <% } %>
        
        <div class="welcome-section">
            <h2><i class="fas fa-home"></i> Welcome to Smart City Services</h2>
            <p>Your digital gateway to city services. Report issues, track complaints, and help make our city better.</p>
        </div>
        
        <div class="quick-actions">
            <div class="action-card submit">
                <i class="fas fa-plus-circle"></i>
                <h3>Submit New Complaint</h3>
                <p>Report infrastructure issues, public service problems, or community concerns.</p>
                <a href="${pageContext.request.contextPath}/jsp/complaint_form.jsp" class="btn-action">
                    <i class="fas fa-edit"></i> Submit Complaint
                </a>
            </div>
            
            <div class="action-card track">
                <i class="fas fa-search"></i>
                <h3>Track Your Complaints</h3>
                <p>Monitor the status of your submitted complaints and see updates from city officials.</p>
                <a href="${pageContext.request.contextPath}/complaint" class="btn-action">
                    <i class="fas fa-list"></i> View All Complaints
                </a>
            </div>
        </div>
        
        <div class="complaints-section" id="complaints">
            <div class="section-header">
                <h3><i class="fas fa-clipboard-list"></i> Your Complaints</h3>
            </div>
            
            <div class="complaints-grid">
                <% if (userComplaints != null && !userComplaints.isEmpty()) { %>
                    <% for (Complaint c : userComplaints) { %>
                        <div class="complaint-card">
                            <div class="complaint-header">
                                <div class="complaint-title"><%= c.getTitle() %></div>
                                <span class="status-badge status-<%= c.getStatus().toLowerCase().replace(" ", "") %>">
                                    <%= c.getStatus() %>
                                </span>
                            </div>
                            
                            <div class="complaint-meta">
                                <span><i class="fas fa-tag"></i> <%= c.getCategory() %></span>
                                <span><i class="fas fa-map-marker-alt"></i> <%= c.getLocation() %></span>
                                <span><i class="fas fa-calendar"></i> <%= c.getCreatedAt() %></span>
                            </div>
                            
                            <div class="complaint-description">
                                <%= c.getDescription() %>
                            </div>
                            
                            <div class="complaint-footer">
                                <span>Complaint #<%= c.getId() %></span>
                                <span>
                                    <% if (c.getAssignedTo() != null && !c.getAssignedTo().isEmpty()) { %>
                                        Assigned to: <%= c.getAssignedTo() %>
                                    <% } else { %>
                                        <em>Not assigned yet</em>
                                    <% } %>
                                </span>
                            </div>
                        </div>
                    <% } %>
                <% } else { %>
                    <div class="empty-state">
                        <i class="fas fa-inbox"></i>
                        <h3>No Complaints Yet</h3>
                        <p>You haven't submitted any complaints. Use the "Submit New Complaint" button above to report an issue.</p>
                        <a href="${pageContext.request.contextPath}/jsp/complaint_form.jsp" class="btn-action" style="margin-top: 1rem;">
                            <i class="fas fa-plus"></i> Submit Your First Complaint
                        </a>
                    </div>
                <% } %>
            </div>
        </div>
    </div>
</body>
</html>