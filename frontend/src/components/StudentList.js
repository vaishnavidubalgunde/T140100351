import React from 'react';

const StudentList = ({ students, editStudent, deleteStudent }) => {
  return (
    <div className="student-list">
      {students.length === 0 ? (
        <p>No students found.</p>
      ) : (
        students.map((student) => (
          <div key={student.id} className="card">
            <h3>{student.studentName}</h3>
            <p><strong>Email:</strong> {student.email}</p>
            <p><strong>Phone:</strong> {student.phone}</p>
            <p><strong>Course:</strong> {student.course}</p>
            <p><strong>Department:</strong> {student.department}</p>
            <p><strong>Address:</strong> {student.address}</p>
            <div className="button-group">
              <button className="btn-green" onClick={() => editStudent(student)}>Edit</button>
              <button className="btn-red" onClick={() => deleteStudent(student.id)}>Delete</button>
            </div>
          </div>
        ))
      )}
    </div>
  );
};

export default StudentList;
