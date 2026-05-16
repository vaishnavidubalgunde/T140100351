import React, { useState, useEffect } from 'react';

const StudentForm = ({ addStudent, updateStudent, editingStudent, clearEditing }) => {
  const [student, setStudent] = useState({
    studentName: '',
    email: '',
    phone: '',
    course: '',
    department: '',
    address: '',
  });

  useEffect(() => {
    if (editingStudent) {
      setStudent(editingStudent);
    } else {
      setStudent({
        studentName: '',
        email: '',
        phone: '',
        course: '',
        department: '',
        address: '',
      });
    }
  }, [editingStudent]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setStudent({ ...student, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (editingStudent) {
      updateStudent(student);
    } else {
      addStudent(student);
    }
    setStudent({
      studentName: '',
      email: '',
      phone: '',
      course: '',
      department: '',
      address: '',
    });
  };

  const handleClear = () => {
    clearEditing();
    setStudent({
      studentName: '',
      email: '',
      phone: '',
      course: '',
      department: '',
      address: '',
    });
  };

  return (
    <div className="card">
      <h2>{editingStudent ? 'Edit Student' : 'Add Student'}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="studentName"
          placeholder="Student Name"
          value={student.studentName}
          onChange={handleChange}
          required
          className="full-width"
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={student.email}
          onChange={handleChange}
          required
          className="full-width"
        />
        <input
          type="text"
          name="phone"
          placeholder="Phone"
          value={student.phone}
          onChange={handleChange}
          required
          className="full-width"
        />
        <input
          type="text"
          name="course"
          placeholder="Course"
          value={student.course}
          onChange={handleChange}
          required
          className="full-width"
        />
        <input
          type="text"
          name="department"
          placeholder="Department"
          value={student.department}
          onChange={handleChange}
          required
          className="full-width"
        />
        <input
          type="text"
          name="address"
          placeholder="Address"
          value={student.address}
          onChange={handleChange}
          required
          className="full-width"
        />
        <div className="button-group">
          <button type="submit" className={editingStudent ? 'btn-green' : 'btn-blue'}>
            {editingStudent ? 'Update' : 'Add'}
          </button>
          {editingStudent && (
            <button type="button" className="btn-gray" onClick={handleClear}>
              Clear
            </button>
          )}
        </div>
      </form>
    </div>
  );
};

export default StudentForm;
