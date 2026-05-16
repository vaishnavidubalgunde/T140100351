package com.example.studentmanagement;

public class SimpleStudent {
    private Long id;
    private String studentName;
    private String email;
    private String phone;
    private String course;
    private String department;
    private String address;
    
    public SimpleStudent() {}
    
    public SimpleStudent(Long id, String studentName, String email, String phone, String course, String department, String address) {
        this.id = id;
        this.studentName = studentName;
        this.email = email;
        this.phone = phone;
        this.course = course;
        this.department = department;
        this.address = address;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String toJson() {
        return String.format(
            "{\"id\":%d,\"studentName\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"course\":\"%s\",\"department\":\"%s\",\"address\":\"%s\"}",
            id, studentName, email, phone, course, department, address
        );
    }
}
