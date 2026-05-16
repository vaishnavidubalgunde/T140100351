import React, { useState, useEffect } from 'react';
import StudentForm from './components/StudentForm';
import StudentList from './components/StudentList';
import './App.css';

const App = () => {
  const [students, setStudents] = useState([]);
  const [editingStudent, setEditingStudent] = useState(null);

  // Fetch all students
  const fetchStudents = () => {
    fetch('http://localhost:8080/api/students')
      .then((res) => res.json())
      .then((data) => setStudents(data))
      .catch((err) => console.error('Error fetching students:', err));
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  // Add student
  const addStudent = (student) => {
    fetch('http://localhost:8080/api/students', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(student),
    })
      .then((res) => res.json())
      .then(() => fetchStudents())
      .catch((err) => console.error('Error adding student:', err));
  };

  // Update student
  const updateStudent = (student) => {
    fetch(`http://localhost:8080/api/students/${student.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(student),
    })
      .then((res) => res.json())
      .then(() => {
        fetchStudents();
        setEditingStudent(null);
      })
      .catch((err) => console.error('Error updating student:', err));
  };

  // Delete student
  const deleteStudent = (id) => {
    fetch(`http://localhost:8080/api/students/${id}`, {
      method: 'DELETE',
    })
      .then(() => fetchStudents())
      .catch((err) => console.error('Error deleting student:', err));
  };

  // Edit student
  const editStudent = (student) => {
    setEditingStudent(student);
  };

  // Clear editing
  const clearEditing = () => {
    setEditingStudent(null);
  };

  return (
    <div className="container">
      <h1>Student Management System</h1>
      <StudentForm
        addStudent={addStudent}
        updateStudent={updateStudent}
        editingStudent={editingStudent}
        clearEditing={clearEditing}
      />
      <StudentList
        students={students}
        editStudent={editStudent}
        deleteStudent={deleteStudent}
      />
    </div>
  );
};

export default App;
