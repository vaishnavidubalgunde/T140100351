import React, { useState, useEffect } from 'react';
import StudentForm from './components/StudentForm';
import StudentList from './components/StudentList';
import './App.css';

const App = () => {
  const [students, setStudents] = useState([]);
  const [editingStudent, setEditingStudent] = useState(null);

  // Backend URL deployed on Render
  const API_URL = 'https://t140100351-1.onrender.com/api/students';

  // Fetch all students
  const fetchStudents = () => {
    fetch(API_URL)
      .then((res) => res.json())
      .then((data) => setStudents(data))
      .catch((err) => console.error('Error fetching students:', err));
  };

  useEffect(() => {
    fetchStudents();
  }, []);

  // Add student
  const addStudent = (student) => {
    fetch(API_URL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(student),
    })
      .then((res) => res.json())
      .then(() => fetchStudents())
      .catch((err) => console.error('Error adding student:', err));
  };

  // Update student
  const updateStudent = (student) => {
    fetch(`${API_URL}/${student.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
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
    fetch(`${API_URL}/${id}`, {
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