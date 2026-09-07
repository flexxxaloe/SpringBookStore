CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         bio VARCHAR(500),
                         born_date DATE NOT NULL,

                         CONSTRAINT uk_author_name_born_date
                             UNIQUE (name, born_date)
);


CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(255) NOT NULL,

                       CONSTRAINT uk_users_username UNIQUE (username),
                       CONSTRAINT uk_users_email UNIQUE (email)
);


CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(255),
                       publication DATE NOT NULL,
                       written_year INTEGER,
                       amount INTEGER NOT NULL,
                       price BIGINT NOT NULL,
                       book_status VARCHAR(255),
                       author_id BIGINT NOT NULL,
                       version BIGINT,

                       CONSTRAINT fk_books_author
                           FOREIGN KEY (author_id)
                               REFERENCES authors(id),

                       CONSTRAINT chk_books_amount
                           CHECK (amount >= 0),

                       CONSTRAINT chk_books_price
                           CHECK (price >= 0)
);


CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        total_price BIGINT NOT NULL,
                        created_at TIMESTAMP NOT NULL,

                        CONSTRAINT fk_orders_user
                            FOREIGN KEY (user_id)
                                REFERENCES users(id),

                        CONSTRAINT chk_orders_total_price
                            CHECK (total_price >= 0)
);


CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             book_id BIGINT NOT NULL,
                             quantity INTEGER NOT NULL,
                             price_at_purchase BIGINT NOT NULL,

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id),

                             CONSTRAINT fk_order_items_book
                                 FOREIGN KEY (book_id)
                                     REFERENCES books(id),

                             CONSTRAINT chk_order_items_quantity
                                 CHECK (quantity > 0),

                             CONSTRAINT chk_order_items_price
                                 CHECK (price_at_purchase >= 0)
);