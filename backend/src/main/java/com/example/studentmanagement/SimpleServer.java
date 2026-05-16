package com.example.studentmanagement;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleServer {
    private static final int PORT = 8080;
    private static List<SimpleStudent> students = new ArrayList<>();
    
    static {
        // Add some sample data
        students.add(new SimpleStudent(1L, "John Doe", "john@example.com", "1234567890", "Computer Science", "Engineering", "123 Main St"));
        students.add(new SimpleStudent(2L, "Jane Smith", "jane@example.com", "0987654321", "Mathematics", "Science", "456 Oak Ave"));
    }
    
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleRequest(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream())) {
            
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];
            
            // Send CORS headers
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: application/json");
            out.println("Access-Control-Allow-Origin: *");
            out.println("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
            out.println("Access-Control-Allow-Headers: Content-Type");
            out.println();
            
            if ("OPTIONS".equals(method)) {
                return;
            }
            
            if ("/api/students".equals(path) && "GET".equals(method)) {
                // Return all students
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < students.size(); i++) {
                    if (i > 0) json.append(",");
                    json.append(students.get(i).toJson());
                }
                json.append("]");
                out.println(json.toString());
            } else if ("/api/students".equals(path) && "POST".equals(method)) {
                // Add new student - read the request body and parse JSON
                StringBuilder requestBody = new StringBuilder();
                String line;
                
                // Skip headers first
                int contentLength = 0;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    if (line.startsWith("Content-Length:")) {
                        contentLength = Integer.parseInt(line.split(":")[1].trim());
                    }
                }
                
                // Read the actual JSON body
                char[] buffer = new char[contentLength];
                in.read(buffer, 0, contentLength);
                String jsonBody = new String(buffer);
                if (jsonBody.contains("studentName")) {
                    // Create a new student with auto-incremented ID
                    Long newId = students.stream().mapToLong(SimpleStudent::getId).max().orElse(0L) + 1;
                    
                    // Extract values from JSON (simplified parsing)
                    String studentName = extractValue(jsonBody, "studentName");
                    String email = extractValue(jsonBody, "email");
                    String phone = extractValue(jsonBody, "phone");
                    String course = extractValue(jsonBody, "course");
                    String department = extractValue(jsonBody, "department");
                    String address = extractValue(jsonBody, "address");
                    
                    SimpleStudent newStudent = new SimpleStudent(newId, studentName, email, phone, course, department, address);
                    students.add(newStudent);
                    
                    out.println("{\"success\": true, \"message\": \"Student added successfully\", \"student\": " + newStudent.toJson() + "}");
                } else {
                    out.println("{\"error\": \"Invalid student data\"}");
                }
            } else if (path.startsWith("/api/students/") && "GET".equals(method)) {
                // Return specific student
                try {
                    Long id = Long.parseLong(path.substring("/api/students/".length()));
                    SimpleStudent student = students.stream()
                        .filter(s -> s.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                    if (student != null) {
                        out.println(student.toJson());
                    } else {
                        out.println("{\"error\": \"Student not found\"}");
                    }
                } catch (NumberFormatException e) {
                    out.println("{\"error\": \"Invalid ID\"}");
                }
            } else if (path.startsWith("/api/students/") && "PUT".equals(method)) {
                // Update student - read request body and parse JSON
                try {
                    Long id = Long.parseLong(path.substring("/api/students/".length()));
                    
                    // Read request body
                    int contentLength = 0;
                    String line;
                    while ((line = in.readLine()) != null && !line.isEmpty()) {
                        if (line.startsWith("Content-Length:")) {
                            contentLength = Integer.parseInt(line.split(":")[1].trim());
                        }
                    }
                    
                    char[] buffer = new char[contentLength];
                    in.read(buffer, 0, contentLength);
                    String jsonBody = new String(buffer);
                    
                    // Find and update student
                    SimpleStudent studentToUpdate = students.stream()
                        .filter(s -> s.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                    
                    if (studentToUpdate != null) {
                        studentToUpdate.setStudentName(extractValue(jsonBody, "studentName"));
                        studentToUpdate.setEmail(extractValue(jsonBody, "email"));
                        studentToUpdate.setPhone(extractValue(jsonBody, "phone"));
                        studentToUpdate.setCourse(extractValue(jsonBody, "course"));
                        studentToUpdate.setDepartment(extractValue(jsonBody, "department"));
                        studentToUpdate.setAddress(extractValue(jsonBody, "address"));
                        
                        out.println("{\"success\": true, \"message\": \"Student updated successfully\", \"student\": " + studentToUpdate.toJson() + "}");
                    } else {
                        out.println("{\"error\": \"Student not found\"}");
                    }
                } catch (Exception e) {
                    out.println("{\"error\": \"Invalid request\"}");
                }
            } else if (path.startsWith("/api/students/") && "DELETE".equals(method)) {
                // Delete student
                try {
                    Long id = Long.parseLong(path.substring("/api/students/".length()));
                    boolean removed = students.removeIf(s -> s.getId().equals(id));
                    
                    if (removed) {
                        out.println("{\"success\": true, \"message\": \"Student deleted successfully\"}");
                    } else {
                        out.println("{\"error\": \"Student not found\"}");
                    }
                } catch (Exception e) {
                    out.println("{\"error\": \"Invalid ID\"}");
                }
            } else {
                out.println("{\"message\": \"API endpoint not found\"}");
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String extractValue(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) return "";
        
        startIndex += searchKey.length();
        int endIndex = json.indexOf("\"", startIndex);
        if (endIndex == -1) return "";
        
        return json.substring(startIndex, endIndex).replace("\\\"", "\"");
    }
}
