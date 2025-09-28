<%@ page import="java.util.List" %>
<%@ page import="com.smartcity.model.Complaint" %>
<%@ page import="com.smartcity.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
User user = (User) session.getAttribute("user");
if (user == null || !"admin".equals(user.getRole())) {
    response.sendRedirect("login.jsp");
    return;
}
List<Complaint> complaints = (List<Complaint>) request.getAttribute("complaints");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Smart City Management</title>
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
        
        .header .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        
        .logout-btn {
            background: rgba(255, 255, 255, 0.2);
            color: white;
            padding: 0.5rem 1rem;
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 8px;
            text-decoration: none;
            transition: all 0.3s ease;
        }
        
        .logout-btn:hover {
            background: rgba(255, 255, 255, 0.3);
            color: white;
        }
        
        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 2rem;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        
        .stat-card {
            background: white;
            padding: 1.5rem;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            border-left: 4px solid;
        }
        
        .stat-card.total {
            border-color: #667eea;
        }
        
        .stat-card.pending {
            border-color: #ffc107;
        }
        
        .stat-card.progress {
            border-color: #17a2b8;
        }
        
        .stat-card.resolved {
            border-color: #28a745;
        }
        
        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            color: #333;
        }
        
        .stat-label {
            color: #666;
            font-size: 0.9rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
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
        
        .section-header h2 {
            color: #333;
            font-size: 1.4rem;
            font-weight: 500;
        }
        
        .table-responsive {
            overflow-x: auto;
        }
        
        .complaints-table {
            width: 100%;
            border-collapse: collapse;
        }
        
        .complaints-table th,
        .complaints-table td {
            padding: 1rem;
            text-align: left;
            border-bottom: 1px solid #e9ecef;
        }
        
        .complaints-table th {
            background: #f8f9fa;
            color: #495057;
            font-weight: 600;
            text-transform: uppercase;
            font-size: 0.8rem;
            letter-spacing: 0.5px;
        }
        
        .complaints-table tr:hover {
            background: #f8f9fa;
        }
        
        .status-badge {
            padding: 0.25rem 0.75rem;
            border-radius: 20px;
            font-size: 0.8rem;
            font-weight: 500;
            text-transform: uppercase;
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
        
        .action-form {
            display: flex;
            gap: 0.5rem;
            align-items: center;
            flex-wrap: wrap;
        }
        
        .action-form select,
        .action-form input {
            padding: 0.5rem;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 0.9rem;
        }
        
        .update-btn {
            background: #667eea;
            color: white;
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 0.9rem;
            transition: all 0.3s ease;
        }
        
        .update-btn:hover {
            background: #5a6fd8;
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
            
            .complaints-table {
                font-size: 0.9rem;
            }
            
            .complaints-table th,
            .complaints-table td {
                padding: 0.75rem 0.5rem;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="header-content">
            <h1><i class="fas fa-city"></i> Smart City Admin Dashboard</h1>
            <div class="user-info">
                <span><i class="fas fa-user-shield"></i> Welcome, <%= user.getUsername() %></span>
                <a href="${pageContext.request.contextPath}/logout" class="logout-btn">
                    <i class="fas fa-sign-out-alt"></i> Logout
                </a>
            </div>
        </div>
    </header>
    
    <div class="container">
        <div class="stats-grid">
            <%
                int total = 0, submitted = 0, inProgress = 0, resolved = 0;
                if (complaints != null) {
                    total = complaints.size();
                    for (Complaint c : complaints) {
                        String status = c.getStatus();
                        if ("Submitted".equals(status)) submitted++;
                        else if ("In Progress".equals(status)) inProgress++;
                        else if ("Resolved".equals(status)) resolved++;
                    }
                }
            %>
            <div class="stat-card total">
                <div class="stat-number"><%= total %></div>
                <div class="stat-label">Total Complaints</div>
            </div>
            <div class="stat-card pending">
                <div class="stat-number"><%= submitted %></div>
                <div class="stat-label">Pending Review</div>
            </div>
            <div class="stat-card progress">
                <div class="stat-number"><%= inProgress %></div>
                <div class="stat-label">In Progress</div>
            </div>
            <div class="stat-card resolved">
                <div class="stat-number"><%= resolved %></div>
                <div class="stat-label">Resolved</div>
            </div>
        </div>
        
        <div class="complaints-section">
            <div class="section-header">
                <h2><i class="fas fa-list"></i> All Complaints</h2>
            </div>
            
            <% if (complaints != null && !complaints.isEmpty()) { %>
                <div class="table-responsive">
                    <table class="complaints-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Title</th>
                                <th>Category</th>
                                <th>Location</th>
                                <th>Status</th>
                                <th>Assigned To</th>
                                <th>Created</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Complaint c : complaints) { %>
                                <tr>
                                    <td><strong>#<%= c.getId() %></strong></td>
                                    <td><%= c.getTitle() %></td>
                                    <td><%= c.getCategory() %></td>
                                    <td><%= c.getLocation() %></td>
                                    <td>
                                        <span class="status-badge status-<%= c.getStatus().toLowerCase().replace(" ", "") %>">
                                            <%= c.getStatus() %>
                                        </span>
                                    </td>
                                    <td><%= c.getAssignedTo() != null ? c.getAssignedTo() : "Unassigned" %></td>
                                    <td><%= c.getCreatedAt() %></td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/admin" class="action-form">
                                            <input type="hidden" name="id" value="<%= c.getId() %>">
                                            <select name="status" required>
                                                <option value="Submitted" <%= "Submitted".equals(c.getStatus()) ? "selected" : "" %>>Submitted</option>
                                                <option value="In Progress" <%= "In Progress".equals(c.getStatus()) ? "selected" : "" %>>In Progress</option>
                                                <option value="Resolved" <%= "Resolved".equals(c.getStatus()) ? "selected" : "" %>>Resolved</option>
                                                <option value="Rejected" <%= "Rejected".equals(c.getStatus()) ? "selected" : "" %>>Rejected</option>
                                            </select>
                                            <input type="text" name="assigned_to" 
                                                   value="<%= c.getAssignedTo() != null ? c.getAssignedTo() : "" %>" 
                                                   placeholder="Assign to">
                                            <button type="submit" class="update-btn">
                                                <i class="fas fa-save"></i> Update
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <i class="fas fa-inbox"></i>
                    <h3>No Complaints Found</h3>
                    <p>No citizen complaints have been submitted yet.</p>
                </div>
            <% } %>
        </div>
    </div>
</body>
</html>