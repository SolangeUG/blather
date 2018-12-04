CREATE TABLE IF NOT EXISTS users(
    user_name VARCHAR(40) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS messages(
    message_id SERIAL PRIMARY KEY,
    user_name VARCHAR(40) REFERENCES users(user_name),
    message_text TEXT NOT NULL,
    message_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS followers(
    user_name VARCHAR(40) REFERENCES users(user_name),
    follows_name VARCHAR(40) REFERENCES users(user_name)
);

