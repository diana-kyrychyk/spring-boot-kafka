CREATE TABLE IF NOT EXISTS orders (
                                         id BIGINT NOT NULL AUTO_INCREMENT,
                                         customer_name VARCHAR(255),
                                         order_date VARCHAR(255),
                                         total_amount VARCHAR(255),
                                         item VARCHAR(255),
                                         PRIMARY KEY (id)
);

INSERT INTO orders (customer_name, order_date, total_amount, item) VALUES
                                                             ('Hanna Melnyk', '2023-07-03', '345253454', 'guitar'),
                                                             ('Bohdan Poseyduk', '2023-07-01', '464253454', 'laptop'),
                                                             ('Serhii Honchar', '2023-06-30', '879253454', 'wallet');
