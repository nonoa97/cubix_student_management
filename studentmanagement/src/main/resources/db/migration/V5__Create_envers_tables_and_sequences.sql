CREATE SEQUENCE revinfo_seq
    INCREMENT BY 50
    START WITH 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE revinfo (
                         rev INTEGER NOT NULL,
                         revtstmp BIGINT,
                         CONSTRAINT revinfo_pkey PRIMARY KEY (rev)
);

CREATE SEQUENCE course_aud_seq
    INCREMENT BY 50
    START WITH 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE course_aud (
                            id INTEGER NOT NULL,
                            rev INTEGER NOT NULL,
                            revtype SMALLINT,
                            name VARCHAR(255),
                            CONSTRAINT course_aud_pkey PRIMARY KEY (id, rev),
                            CONSTRAINT fk_course_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

CREATE SEQUENCE student_aud_seq
    INCREMENT BY 50
    START WITH 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE student_aud (
                             id INTEGER NOT NULL,
                             rev INTEGER NOT NULL,
                             revtype SMALLINT,
                             birthdate DATE,
                             central_id INTEGER,
                             free_semesters_used INTEGER,
                             name VARCHAR(255),
                             semester INTEGER,
                             CONSTRAINT student_aud_pkey PRIMARY KEY (id, rev),
                             CONSTRAINT fk_student_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

CREATE SEQUENCE teacher_aud_seq
    INCREMENT BY 50
    START WITH 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE teacher_aud (
                             id INTEGER NOT NULL,
                             rev INTEGER NOT NULL,
                             revtype SMALLINT,
                             birthdate DATE,
                             name VARCHAR(255),
                             CONSTRAINT teacher_aud_pkey PRIMARY KEY (id, rev),
                             CONSTRAINT fk_teacher_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

CREATE TABLE student_course_aud (
                                    rev INTEGER NOT NULL,
                                    student_id INTEGER NOT NULL,
                                    course_id INTEGER NOT NULL,
                                    revtype SMALLINT,
                                    CONSTRAINT student_course_aud_pkey PRIMARY KEY (rev, student_id, course_id),
                                    CONSTRAINT fk_student_course_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);

CREATE TABLE teacher_course_aud (
                                    rev INTEGER NOT NULL,
                                    teacher_id INTEGER NOT NULL,
                                    course_id INTEGER NOT NULL,
                                    revtype SMALLINT,
                                    CONSTRAINT teacher_course_aud_pkey PRIMARY KEY (rev, teacher_id, course_id),
                                    CONSTRAINT fk_teacher_course_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo(rev)
);