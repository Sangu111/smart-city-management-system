<%@ page import="com.smartcity.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
User user = (User) session.getAttribute("user");
if (user == null) {
    response.sendRedirect("login.jsp");
    return;
}
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Complaint - Smart City</title>
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
        
        .nav-links {
            display: flex;
            align-items: center;
            gap: 1rem;
        }
        
        .nav-links a {
            color: white;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            background: rgba(255, 255, 255, 0.2);
            border: 1px solid rgba(255, 255, 255, 0.3);
            transition: all 0.3s ease;
        }
        
        .nav-links a:hover {
            background: rgba(255, 255, 255, 0.3);
        }
        
        .container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 2rem;
        }
        
        .form-container {
            background: white;
            border-radius: 12px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            overflow: hidden;
        }
        
        .form-header {
            background: #f8f9fa;
            padding: 2rem;
            text-align: center;
            border-bottom: 1px solid #e9ecef;
        }
        
        .form-header i {
            font-size: 3rem;
            color: #667eea;
            margin-bottom: 1rem;
        }
        
        .form-header h2 {
            color: #333;
            font-size: 1.8rem;
            font-weight: 300;
            margin-bottom: 0.5rem;
        }
        
        .form-header p {
            color: #666;
            font-size: 1rem;
        }
        
        .form-content {
            padding: 2rem;
        }
        
        .form-group {
            margin-bottom: 1.5rem;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #333;
            font-weight: 500;
        }
        
        .required {
            color: #dc3545;
        }
        
        .form-group input,
        .form-group select,
        .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #e1e5e9;
            border-radius: 8px;
            font-size: 16px;
            font-family: inherit;
            transition: all 0.3s ease;
            background: #f8f9fa;
        }
        
        .form-group input:focus,
        .form-group select:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #667eea;
            background: white;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .form-group textarea {
            resize: vertical;
            min-height: 120px;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }
        
        .file-input-wrapper {
            position: relative;
            display: inline-block;
            width: 100%;
        }
        
        .file-input {
            display: none;
        }
        
        .file-input-button {
            display: block;
            width: 100%;
            padding: 12px;
            border: 2px dashed #e1e5e9;
            border-radius: 8px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
            background: #f8f9fa;
            color: #666;
        }
        
        .file-input-button:hover {
            border-color: #667eea;
            background: #f0f2ff;
            color: #667eea;
        }
        
        .file-input-button i {
            font-size: 2rem;
            margin-bottom: 0.5rem;
            display: block;
        }
        
        .submit-btn {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            margin-top: 1rem;
        }
        
        .submit-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 25px rgba(102, 126, 234, 0.3);
        }
        
        .info-box {
            background: #e7f3ff;
            border-left: 4px solid #2196f3;
            padding: 1rem;
            border-radius: 8px;
            margin-bottom: 1.5rem;
        }
        
        .info-box h4 {
            color: #1976d2;
            margin-bottom: 0.5rem;
        }
        
        .info-box p {
            color: #555;
            font-size: 0.9rem;
            margin: 0;
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
            
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .nav-links {
                flex-wrap: wrap;
                justify-content: center;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="header-content">
            <h1><i class="fas fa-city"></i> Submit Complaint</h1>
            <div class="nav-links">
                <a href="${pageContext.request.contextPath}/complaint"><i class="fas fa-home"></i> Dashboard</a>
                <a href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
            </div>
        </div>
    </header>
    
    <div class="container">
        <div class="form-container">
            <div class="form-header">
                <i class="fas fa-edit"></i>
                <h2>Submit a Complaint</h2>
                <p>Help us improve our city by reporting issues and concerns</p>
            </div>
            
            <div class="form-content">
                <div class="info-box">
                    <h4><i class="fas fa-info-circle"></i> Before You Submit</h4>
                    <p>Please provide as much detail as possible to help us address your concern effectively. Include specific locations, dates, and any relevant information.</p>
                </div>
                
                <form method="post" action="${pageContext.request.contextPath}/complaint" enctype="multipart/form-data">
                    <div class="form-row">
                        <div class="form-group">
                            <label for="category">Category <span class="required">*</span></label>
                            <select id="category" name="category" required>
                                <option value="">Select a category</option>
                                <option value="Roads">Roads & Transportation</option>
                                <option value="Water">Water & Sewerage</option>
                                <option value="Electricity">Electricity & Power</option>
                                <option value="Waste">Waste Management</option>
                                <option value="Public Safety">Public Safety</option>
                                <option value="Parks">Parks & Recreation</option>
                                <option value="Noise">Noise Pollution</option>
                                <option value="Pollution">Environmental Issues</option>
                                <option value="Traffic">Traffic & Parking</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>
                        
                        <div class="form-group">
                            <label for="priority">Priority</label>
                            <select id="priority" name="priority">
                                <option value="Low">Low</option>
                                <option value="Medium" selected>Medium</option>
                                <option value="High">High</option>
                                <option value="Urgent">Urgent</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label for="title">Complaint Title <span class="required">*</span></label>
                        <input type="text" id="title" name="title" placeholder="Brief description of the issue" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="location">Location <span class="required">*</span></label>
                        <input type="text" id="location" name="location" placeholder="Street address or landmark" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="description">Detailed Description <span class="required">*</span></label>
                        <textarea id="description" name="description" 
                                  placeholder="Please provide a detailed description of the issue, including when it occurred and any relevant circumstances..." 
                                  required></textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="photo">Photo Evidence (Optional)</label>
                        <div class="file-input-wrapper">
                            <input type="file" id="photo" name="photo" class="file-input" accept="image/*">
                            <label for="photo" class="file-input-button">
                                <i class="fas fa-camera"></i>
                                Click to upload photo
                                <small style="display: block; font-size: 0.8rem; margin-top: 0.5rem;">
                                    Supported formats: JPG, PNG, GIF (Max 5MB)
                                </small>
                            </label>
                        </div>
                    </div>
                    
                    <button type="submit" class="submit-btn">
                        <i class="fas fa-paper-plane"></i> Submit Complaint
                    </button>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        // File input enhancement
        const fileInput = document.getElementById('photo');
        const fileButton = document.querySelector('.file-input-button');
        
        fileInput.addEventListener('change', function(e) {
            if (e.target.files.length > 0) {
                const fileName = e.target.files[0].name;
                fileButton.innerHTML = `
                    <i class="fas fa-check"></i>
                    File selected: ${fileName}
                    <small style="display: block; font-size: 0.8rem; margin-top: 0.5rem;">
                        Click to change file
                    </small>
                `;
                fileButton.style.borderColor = '#28a745';
                fileButton.style.backgroundColor = '#d4edda';
                fileButton.style.color = '#155724';
            }
        });
        
        // Form validation enhancement
        const form = document.querySelector('form');
        form.addEventListener('submit', function(e) {
            const title = document.getElementById('title').value.trim();
            const description = document.getElementById('description').value.trim();
            
            if (title.length < 10) {
                e.preventDefault();
                alert('Please provide a more descriptive title (at least 10 characters).');
                return;
            }
            
            if (description.length < 20) {
                e.preventDefault();
                alert('Please provide a more detailed description (at least 20 characters).');
                return;
            }
        });
    </script>
</body>
</html>