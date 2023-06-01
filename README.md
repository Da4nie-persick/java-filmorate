# java-filmorate
Template repository for Filmorate project.
https://dbdiagram.io/d/64608aecdca9fb07c40aab96
![2023-05-29_13-33-01](https://github.com/Da4nie-persick/java-filmorate/assets/115876126/0fd42641-87bb-4675-bad2-ee5cc23addf2)

База данных состоит из 8 таблиц, представленных на картинке, в каждой таблице присутствует первичный ключ по нему можно однозначно обратиться к одной конкретной записи. Некоторые таблицы включают в себя и внешние ключи, ссылающийся на первичный ключ другой таблицы.
К примеру, таблица Film представляет собой список фильмов, состоящий из описания, названия, жанра, продолжительности и так далее. Линиями обозначены связи таблиц, по внешнему ключу rating_id можно обратиться к таблице Rating сопоставив rating_id из таблицы Film и из таблицы Rating можно определить рейтинг Ассоциации кинокомпаний. Подобные связи можно увидеть и в остальных таблицах.
Примеры команд для обращения к таблицам:
1)	Выгрузить все данные из таблицы User 
 ```
 SELECT *
 FROM user;
 ```
2)	Выгрузить название фильма и рейтинг первых 10 фильмов 
 ```
 SELECT f.name, r.name
 FROM film AS f
 INNER JOIN rating AS r ON f.rating_id = r.rating_id
 LIMIT 10;
 ```
3)	Выгрузить названия и количество лайков топ-5 самых популярных фильмов в порядке убывания
```
 SELECT f.name, COUNT(l.film_id) AS likes_film
 FROM film AS f
 INNER JOIN likes AS l ON f.film_id = l.film_id
 GROUP_BY f.name
 ORDER BY likes_film DESC
 LIMIT 5;
```
