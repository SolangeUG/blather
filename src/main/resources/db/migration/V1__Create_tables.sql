CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(40) NOT NULL CHECK (user_name <> ''),
    UNIQUE(user_name)
);

CREATE TABLE IF NOT EXISTS messages(
    message_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    message_text TEXT NOT NULL,
    message_date TIMESTAMP NOT NULL
);

