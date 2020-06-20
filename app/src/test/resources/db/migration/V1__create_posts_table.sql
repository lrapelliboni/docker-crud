DROP TABLE IF EXISTS posts;

CREATE TABLE posts (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  title VARCHAR(250) NOT NULL,
  description VARCHAR(250) NOT NULL,
  votes INT NULL DEFAULT 0
);

INSERT INTO posts(id, title, description, votes) VALUES(null, 'Java 8 Programming', 'This post is awesome', 10);