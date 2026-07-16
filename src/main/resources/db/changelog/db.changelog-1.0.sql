
drop table lessons;
drop table schedule;
drop table students;


CREATE TABLE students
(
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE schedule
(
    id               BIGSERIAL PRIMARY KEY,
    student_id       BIGINT                 NOT NULL REFERENCES students (id),
    day_of_week      SMALLINT               NOT NULL,
    start_time       TIME WITHOUT TIME ZONE NOT NULL,
    duration_minutes SMALLINT               NOT NULL,
    is_active        BOOLEAN                  DEFAULT TRUE,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE lessons
(
    id          BIGSERIAL PRIMARY KEY,
    student_id  BIGINT         NOT NULL REFERENCES students (id),
    schedule_id BIGINT         REFERENCES schedule (id),
    lesson_date DATE           NOT NULL,
    start_time  TIME WITHOUT TIME ZONE,
    topic       VARCHAR(255),
    price       DECIMAL(10, 2) NOT NULL,
    is_paid     BOOLEAN                  DEFAULT FALSE,
    paid_date   DATE,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);




