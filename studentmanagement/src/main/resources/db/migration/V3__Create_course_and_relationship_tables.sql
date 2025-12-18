CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE student_course (
    student_id INTEGER REFERENCES student(id),
    course_id INTEGER REFERENCES course(id),
    PRIMARY KEY (student_id, course_id)
);

CREATE TABLE teacher_course (
    teacher_id INTEGER REFERENCES teacher(id),
    course_id INTEGER REFERENCES course(id),
    PRIMARY KEY (teacher_id, course_id)
);