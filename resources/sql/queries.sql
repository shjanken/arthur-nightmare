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

-- :name get-phrases :? :*
-- :doc get phrase what is zip with current lesson and ranger of word
select
	main.context || ranger.context as context,
	main.id as main_id,
	ranger.id as range_id
from
	(select *
	from lesson
	where grand=:grand and term=:term and lesson_num=:lesson_num and symbol=:symbol::lesson_type) main
,
	(select * from lesson where id between :begin and :end) ranger


-- :name insert-word :! :n
-- :doc insert a word record
insert into lesson
(lesson_num, grand, term,  symbol, context, py, ord)
values
(:lesson_num, :grand, :term, :symbol::lesson_type, :context, :py, :ord)

