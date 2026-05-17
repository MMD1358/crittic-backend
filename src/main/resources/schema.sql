DROP TABLE IF EXISTS profile_comment_like;
DROP TABLE IF EXISTS profile_comment;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS conversation_user;
DROP TABLE IF EXISTS conversation;
DROP TABLE IF EXISTS user_follow;
DROP TABLE IF EXISTS review_like;
DROP TABLE IF EXISTS comment_like;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS user_favorite_videogame;
DROP TABLE IF EXISTS videogame_like;
DROP TABLE IF EXISTS videogame;
DROP TABLE IF EXISTS author;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    image VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_password_change_date TIMESTAMP NULL
);

CREATE TABLE author (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE videogame (
    videogame_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    release_date DATE,
    genre VARCHAR(50),
    image VARCHAR(255),
    description TEXT,
    author_id INT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author(author_id)
);

CREATE TABLE videogame_like (
    user_id BIGINT NOT NULL,
    videogame_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, videogame_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (videogame_id) REFERENCES videogame(videogame_id)
);

CREATE TABLE review (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    videogame_id INT NOT NULL,
    rating INT NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (videogame_id) REFERENCES videogame(videogame_id),
    UNIQUE (user_id, videogame_id)
);

CREATE TABLE comment (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    review_id INT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (review_id) REFERENCES review(review_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE review_like (
    user_id BIGINT NOT NULL,
    review_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, review_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (review_id) REFERENCES review(review_id)
);

CREATE TABLE user_follow (
    follower_id BIGINT NOT NULL,
    followed_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followed_id),
    FOREIGN KEY (follower_id) REFERENCES users(user_id),
    FOREIGN KEY (followed_id) REFERENCES users(user_id),
    CHECK (follower_id <> followed_id)
);

CREATE TABLE conversation (
    conversation_id INT PRIMARY KEY AUTO_INCREMENT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE conversation_user (
    conversation_id INT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (conversation_id, user_id),
    FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE message (
    message_id INT PRIMARY KEY AUTO_INCREMENT,
    conversation_id INT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    read_at TIMESTAMP NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (conversation_id) REFERENCES conversation(conversation_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id)
);

CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE comment_like (
    user_id BIGINT NOT NULL,
    comment_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, comment_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (comment_id) REFERENCES comment(comment_id)
);

CREATE TABLE user_favorite_videogame (
    user_id BIGINT NOT NULL,
    videogame_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, videogame_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (videogame_id) REFERENCES videogame(videogame_id)
);

CREATE TABLE profile_comment (
    profile_comment_id INT PRIMARY KEY AUTO_INCREMENT,
    profile_user_id BIGINT NOT NULL,
    author_user_id BIGINT NOT NULL,
    parent_comment_id INT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (profile_user_id) REFERENCES users(user_id),
    FOREIGN KEY (author_user_id) REFERENCES users(user_id),
    FOREIGN KEY (parent_comment_id) REFERENCES profile_comment(profile_comment_id)
);

CREATE TABLE profile_comment_like (
    user_id BIGINT NOT NULL,
    profile_comment_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, profile_comment_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (profile_comment_id) REFERENCES profile_comment(profile_comment_id)
);