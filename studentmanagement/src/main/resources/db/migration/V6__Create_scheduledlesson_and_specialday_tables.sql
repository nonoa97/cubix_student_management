CREATE SEQUENCE special_day_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE scheduled_lesson_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE special_day (
    id INT4 NOT NULL,
    source DATE,
    target DATE,
    PRIMARY KEY (id)
);

CREATE TABLE scheduled_lesson (
    id INT4 NOT NULL,
    day_of_week INT4 NOT NULL,
    start_time TIME,
    end_time TIME,
    course_id INT4,
    PRIMARY KEY (id)
);

ALTER TABLE scheduled_lesson
    ADD CONSTRAINT fk_scheduled_lesson_course
    FOREIGN KEY (course_id)
    REFERENCES course(id);

ALTER TABLE course
    ADD COLUMN semester_type SMALLINT CHECK (semester_type BETWEEN 0 AND 1),
    ADD COLUMN year INTEGER;