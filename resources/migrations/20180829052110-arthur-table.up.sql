CREATE TYPE lesson_type AS ENUM ('word', 'lesson', 'garden');

CREATE TABLE lesson (
       id serial,
       lesson_num integer not null,
       grand integer not null,
       term integer not null,
       symbol lesson_type not null,
       context varchar(20) not null,
       py varchar(50),
       ord integer not null
);
