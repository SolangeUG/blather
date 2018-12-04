DELETE FROM messages;

DELETE FROM users;

INSERT INTO
    USERS(user_name)
VALUES('Isimbi');

INSERT INTO
    USERS(user_name)
VALUES('Benjamin');

INSERT INTO
    MESSAGES(user_id, message_text, message_date)
VALUES(1, 'Testing message for recipient 1', now());
