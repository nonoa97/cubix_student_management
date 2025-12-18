CREATE TABLE student_management_user (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
                                         birthdate DATE,
                                         username VARCHAR(255) NOT NULL UNIQUE,
                                         password VARCHAR(255) NOT NULL
);

CREATE TABLE user_course (
                             user_id INTEGER REFERENCES student_management_user(id),
                             course_id INTEGER REFERENCES course(id),
                             PRIMARY KEY (user_id, course_id)
);

ALTER TABLE student
    DROP COLUMN name,
    DROP COLUMN birthdate,
    ADD CONSTRAINT fk_student_user FOREIGN KEY (id) REFERENCES student_management_user(id);

ALTER TABLE teacher
    DROP COLUMN name,
    DROP COLUMN birthdate,
    ADD CONSTRAINT fk_teacher_user FOREIGN KEY (id) REFERENCES student_management_user(id);

CREATE SEQUENCE student_management_user_aud_seq
    INCREMENT BY 50
    START WITH 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE student_management_user_aud (
                                             id INTEGER NOT NULL,
                                             rev INTEGER NOT NULL,
                                             revtype SMALLINT,
                                             name VARCHAR(255),
                                             birthdate DATE,
                                             username VARCHAR(255),
                                             password VARCHAR(255),
                                             CONSTRAINT student_management_user_aud_pkey PRIMARY KEY (id, rev),
                                             CONSTRAINT fk_student_management_user_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

CREATE TABLE user_course_aud (
                                 rev INTEGER NOT NULL,
                                 user_id INTEGER NOT NULL,
                                 course_id INTEGER NOT NULL,
                                 revtype SMALLINT,
                                 CONSTRAINT user_course_aud_pkey PRIMARY KEY (rev, user_id, course_id),
                                 CONSTRAINT fk_user_course_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

ALTER TABLE student_aud
    DROP COLUMN name,
    DROP COLUMN birthdate;

ALTER TABLE teacher_aud
    DROP COLUMN name,
    DROP COLUMN birthdate;

