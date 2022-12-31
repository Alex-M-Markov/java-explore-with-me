CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(512)                            NOT NULL,
    CONSTRAINT PK_USER PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_CATEGORY PRIMARY KEY (id),
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(255)                            NOT NULL,
    annotation         VARCHAR(1000)                           NOT NULL,
    description        VARCHAR(10000)                          NOT NULL,
    category_id        BIGINT,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    location_lat       FLOAT,
    location_lon       FLOAT,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INTEGER                                 NOT NULL,
    request_moderation BOOLEAN                                 NOT NULL,
    initiator_id       BIGINT,
    state              VARCHAR(255)                            NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT PK_EVENT PRIMARY KEY (id),
    CONSTRAINT FK_CATEGORY_ID_ON_EVENT FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT FK_USER_ID_ON_EVENT FOREIGN KEY (initiator_id) REFERENCES users (id)
);

INSERT INTO categories (id, name)
VALUES (0, 'NOT ASSIGNED')
ON CONFLICT DO NOTHING
;

CREATE TABLE IF NOT EXISTS requests
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id BIGINT,
    user_id  BIGINT,
    status   VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_REQUESTS PRIMARY KEY (id),
    CONSTRAINT FK_EVENT_ID_ON_REQUEST FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT FK_USER_ID_ON_REQUEST FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    title  VARCHAR(255)                            NOT NULL,
    CONSTRAINT PK_COMPILATIONS PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id       BIGINT,
    compilation_id BIGINT,
    CONSTRAINT PK_EVENTS_COMPILATIONS PRIMARY KEY (event_id, compilation_id),
    CONSTRAINT FK_EVENTS FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT FK_COMPILATIONS FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);