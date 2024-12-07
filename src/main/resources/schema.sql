-- DROP TABLE IF EXISTS comments CASCADE;
-- DROP TABLE IF EXISTS bookings CASCADE;
-- DROP TABLE IF EXISTS items CASCADE;
-- DROP TABLE IF EXISTS requests CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;


CREATE TABLE IF NOT EXISTS users (
    id      BIGSERIAL PRIMARY KEY,
    name    VARCHAR(255)    NOT NULL,
    email   VARCHAR(255)    NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS requests (
    id          BIGSERIAL PRIMARY KEY,
    description TEXT      NOT NULL,
    user_id     BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    description TEXT            NOT NULL,
    available   BOOLEAN         NOT NULL DEFAULT TRUE,
    user_id     BIGINT          NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    request_id  BIGINT          --NOT NULL REFERENCES requests(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings (
    id          BIGSERIAL PRIMARY KEY,
    start_date  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id     BIGINT                      NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    user_id     BIGINT                      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status      VARCHAR(30)                 NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    id      BIGSERIAL PRIMARY KEY,
    text    TEXT                        NOT NULL,
    item_id BIGINT                      NOT NULL REFERENCES items(id) ON DELETE CASCADE,
    user_id BIGINT                      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
