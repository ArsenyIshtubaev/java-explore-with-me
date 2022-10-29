CREATE TABLE IF NOT EXISTS users
(
    user_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    UNIQUE (email)
);
CREATE TABLE IF NOT EXISTS categories
(
    category_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name VARCHAR NOT NULL,
    UNIQUE (category_name)
);
CREATE TABLE IF NOT EXISTS location
(
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat         DOUBLE PRECISION NOT NULL,
    lon         DOUBLE PRECISION NOT NULL
);
CREATE TABLE IF NOT EXISTS events
(
    event_id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)               NOT NULL,
    category_id        BIGINT REFERENCES categories (category_id),
    description        VARCHAR(7000)               NOT NULL,
    event_date         timestamp WITHOUT TIME ZONE NOT NULL,
    created_date       timestamp WITHOUT TIME ZONE NOT NULL,
    published_date     timestamp WITHOUT TIME ZONE,
    location_id        BIGINT REFERENCES location (location_id),
    initiator_id       BIGINT REFERENCES users (user_id),
    paid               boolean                     NOT NULL,
    title              VARCHAR                     NOT NULL,
    participant_limit  int                         NOT NULL,
    request_moderation boolean                     NOT NULL,
    state              VARCHAR(15)                 NOT NULL,
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (user_id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (category_id),
    CONSTRAINT fk_events_to_location FOREIGN KEY (location_id) REFERENCES location (location_id)
);
CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned         boolean      NOT NULL,
    title          varchar NOT NULL
);
CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       int not null references events (event_id) on delete cascade,
    compilation_id int references compilations (compilation_id) on delete cascade
);
CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT REFERENCES events (event_id),
    requester_id BIGINT REFERENCES users (user_id),
    status       VARCHAR(20) NOT NULL,
    created      timestamp WITHOUT TIME ZONE,
    CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (event_id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (user_id),
    UNIQUE (event_id, request_id)
);
