CREATE TABLE IF NOT EXISTS currency
(
    id         BIGSERIAL PRIMARY KEY,
    code       VARCHAR(3) NOT NULL UNIQUE,
    name       VARCHAR(64),
    created_at TIMESTAMP  NOT NULL,
    updated_at TIMESTAMP  NOT NULL
);

CREATE TABLE IF NOT EXISTS exchange_rates
(
    id                 BIGSERIAL PRIMARY KEY,
    base_currency_Id   BIGINT REFERENCES currency (id) ON DELETE CASCADE,
    target_currency_Id BIGINT REFERENCES currency (id) ON DELETE CASCADE,
    rate               NUMERIC(10,6),
    created_at         TIMESTAMP NOT NULL,
    updated_at         TIMESTAMP NOT NULL,
    UNIQUE (base_currency_Id, target_currency_Id)
);







