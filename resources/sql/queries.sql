-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE FROM users
WHERE id = :id

-- :name create-lesson! :! :n
-- :doc create a lesson record
insert into lesson
(grand, term, lesson_num, symbol, context, py, ord)
values
(:grand, :term, :lesson_num, :symbol::lesson_type, :context, :py, :order)


-- :name get-word :? :1
-- :doc get word by unique id
select * from lesson
where id = :id


-- :name get-lessons :? :*
-- :doc get words by lesson detail
select * from lesson
where grand = :grand
 and symbol = :symbol::lesson_type
 and lesson_num = :lesson_num
 and term = :term

-- :name get-word-by-range :? :*
-- :doc get words by start and end range(id)
select * from lesson
where id between :begin and :end
