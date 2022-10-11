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
    category_name VARCHAR(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS location
(
    location_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lat         DOUBLE PRECISION NOT NULL CHECK (lat > -90 and lat <= 90),
    lon         DOUBLE PRECISION NOT NULL CHECK (lon > -180 and lon <= 180)
);
CREATE TABLE IF NOT EXISTS events
(
    event_id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation        VARCHAR(255)                NOT NULL,
    category_id       BIGINT REFERENCES categories (category_id),
    description       VARCHAR(255)                NOT NULL,
    event_date        timestamp WITHOUT TIME ZONE NOT NULL,
    created_date      timestamp WITHOUT TIME ZONE NOT NULL,
    location_id       BIGINT REFERENCES location (location_id),
    initiator_id      BIGINT REFERENCES users (user_id),
    paid              boolean                     NOT NULL,
    title             VARCHAR(255)                NOT NULL,
    participantLimit  int                         NOT NULL,
    requestModeration boolean                     NOT NULL,
    state             VARCHAR(15)                 NOT NULL,
    views             int,
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (user_id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (category_id),
    CONSTRAINT fk_events_to_location FOREIGN KEY (location_id) REFERENCES location (location_id)
);
CREATE TABLE IF NOT EXISTS compilations
(
    compilation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned         boolean      NOT NULL,
    title          varchar(255) NOT NULL
);
CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       int not null references events (event_id) on delete cascade,
    compilation_id int references compilations (compilation_id),
    PRIMARY KEY (event_id, compilation_id)
);
CREATE TABLE IF NOT EXISTS requests
(
    request_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT REFERENCES events (event_id),
    requester_id BIGINT REFERENCES users (user_id),
    status       VARCHAR(255) NOT NULL,
    created      timestamp WITHOUT TIME ZONE,
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (user_id),
    CONSTRAINT fk_requests_to_events FOREIGN KEY (requester_id) REFERENCES events (event_id)
);
