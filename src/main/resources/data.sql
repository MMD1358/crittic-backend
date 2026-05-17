INSERT INTO roles (id, name) VALUES
                                 (1, 'ADMIN'),
                                 (2, 'MANAGER'),
                                 (3, 'USER');

INSERT INTO users (
    user_id,
    username,
    email,
    password,
    enabled,
    first_name,
    last_name,
    image,
    description,
    created_at,
    last_modified_date,
    last_password_change_date
)
VALUES
    (1, 'admin', 'admin@gamereview.com', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', TRUE, 'Admin', 'User', 'admin.jpg', 'Admin profile description.', NOW(), NOW(), NOW()),
    (2, 'manager', 'manager@gamereview.com', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', TRUE, 'Manager', 'User', 'manager.jpg', 'Manager profile description.', NOW(), NOW(), NOW()),
    (3, 'normal', 'user@gamereview.com', '$2b$12$FVRijCavVZ7Qt15.CQssHe9m/6eLAdjAv0PiOKFIjMU161wApxzye', TRUE, 'Regular', 'User', 'user.jpg', 'Regular user profile description.', NOW(), NOW(), NOW());

INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1),
                                              (2, 2),
                                              (3, 3);

INSERT INTO author (author_id, name) VALUES
                                         (1, 'Nintendo'),
                                         (2, 'CD Projekt Red'),
                                         (3, 'FromSoftware');

INSERT INTO videogame (
    videogame_id,
    title,
    release_date,
    genre,
    image,
    description,
    author_id
)
VALUES
    (1, 'The Legend of Zelda: Breath of the Wild', '2017-03-03', 'Adventure', 'zelda.jpg',
     'Explore the kingdom of Hyrule in an open-world adventure full of mystery, combat, exploration and freedom.',
     1),

    (2, 'The Witcher 3', '2015-05-19', 'RPG', 'witcher3.jpg',
     'Become Geralt of Rivia and travel through a dark fantasy world filled with monsters, political conflict and difficult choices.',
     2),

    (3, 'Elden Ring', '2022-02-25', 'Action RPG', 'elden-ring.jpg',
     'Rise as a Tarnished and explore the Lands Between in a challenging action RPG full of bosses, secrets and dark fantasy lore.',
     3);

INSERT INTO review (
    review_id,
    user_id,
    videogame_id,
    rating,
    content,
    created_at
)
VALUES
    (1, 3, 1, 4.0,
     'This game is amazing. I really enjoyed the exploration, the combat and the world design.',
     NOW()),

    (2, 1, 2, 5.0,
     'The Witcher 3 is one of the best RPGs I have ever played. The story and characters are incredible.',
     NOW()),

    (3, 2, 3, 4.0,
     'Elden Ring is hard, but the exploration and boss fights are really satisfying.',
     NOW());

INSERT INTO comment (
    comment_id,
    review_id,
    user_id,
    content,
    created_at
)
VALUES
    (1, 1, 1,
     'I agree! The world feels really alive and fun to explore.',
     NOW()),

    (2, 1, 2,
     'The freedom in this game is amazing.',
     NOW()),

    (3, 2, 3,
     'I still need to finish The Witcher 3, but I really like it so far.',
     NOW());

INSERT INTO review_like (user_id, review_id) VALUES
                                                 (1, 1),
                                                 (2, 1),
                                                 (3, 1),
                                                 (2, 2),
                                                 (3, 2),
                                                 (1, 3);

INSERT INTO comment_like (user_id, comment_id) VALUES
                                                   (2, 1),
                                                   (3, 1),
                                                   (1, 2),
                                                   (1, 3);

INSERT INTO videogame_like (user_id, videogame_id) VALUES
                                                       (1, 1),
                                                       (2, 1),
                                                       (3, 1),
                                                       (3, 2),
                                                       (1, 3);

INSERT INTO user_follow (follower_id, followed_id) VALUES
                                                       (2, 1),
                                                       (3, 1),
                                                       (3, 2),
                                                       (1, 3);

INSERT INTO user_favorite_videogame (user_id, videogame_id) VALUES
                                                                (1, 1),
                                                                (1, 3),
                                                                (2, 2),
                                                                (2, 3),
                                                                (3, 1),
                                                                (3, 2);

INSERT INTO profile_comment (
    profile_comment_id,
    profile_user_id,
    author_user_id,
    parent_comment_id,
    content,
    created_at
)
VALUES
    (1, 3, 1, NULL,
     'Nice profile! I saw your Zelda review.',
     NOW()),

    (2, 3, 3, 1,
     'Thanks! I really liked that game.',
     NOW()),

    (3, 3, 2, NULL,
     'Your favorite games list is really cool.',
     NOW()),

    (4, 1, 3, NULL,
     'Hey admin, nice profile!',
     NOW()),

    (5, 2, 1, NULL,
     'I liked your Elden Ring review.',
     NOW());

INSERT INTO profile_comment_like (user_id, profile_comment_id) VALUES
                                                                   (1, 2),
                                                                   (2, 1),
                                                                   (3, 1),
                                                                   (1, 3),
                                                                   (2, 4),
                                                                   (3, 5);

INSERT INTO conversation (conversation_id) VALUES
                                               (1),
                                               (2),
                                               (3);

INSERT INTO conversation_user (conversation_id, user_id) VALUES
                                                             (1, 1),
                                                             (1, 3),

                                                             (2, 2),
                                                             (2, 3),

                                                             (3, 1),
                                                             (3, 2);

INSERT INTO message (
    message_id,
    conversation_id,
    sender_id,
    content,
    sent_at,
    read_at
)
VALUES
    (1, 1, 1,
     'Yo bro, I read your review and it was really cool!',
     NOW() - INTERVAL 20 MINUTE,
     NULL),

    (2, 1, 3,
     'Hellooooooooooo',
     NOW() - INTERVAL 18 MINUTE,
     NOW() - INTERVAL 17 MINUTE),

    (3, 1, 1,
     'Hi',
     NOW() - INTERVAL 15 MINUTE,
     NULL),

    (4, 1, 3,
     'Did you play the game I told you yesterday? Because it is really cool and I think you will like it.',
     NOW() - INTERVAL 12 MINUTE,
     NOW() - INTERVAL 11 MINUTE),

    (5, 1, 3,
     'I hope you like it.',
     NOW() - INTERVAL 10 MINUTE,
     NOW() - INTERVAL 9 MINUTE),

    (6, 2, 2,
     'I guess the game is not bad, but I expected more.',
     NOW() - INTERVAL 1 DAY,
     NULL),

    (7, 2, 3,
     'Really? I liked it a lot, but I understand what you mean.',
     NOW() - INTERVAL 23 HOUR,
     NOW() - INTERVAL 22 HOUR),

    (8, 3, 1,
     'Have you tried Elden Ring yet?',
     NOW() - INTERVAL 2 DAY,
     NOW() - INTERVAL 2 DAY),

    (9, 3, 2,
     'Yes! It is amazing but really difficult.',
     NOW() - INTERVAL 1 DAY,
     NULL);