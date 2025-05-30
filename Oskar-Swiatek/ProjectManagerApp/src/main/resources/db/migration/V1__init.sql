-- Base User Table (Single Table Inheritance)
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- PrivateUser fields (extend 'users')
CREATE TABLE private_user (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    pesel VARCHAR(20) NOT NULL,
    id_card_number VARCHAR(20) NOT NULL,
    driving_license_number VARCHAR(20) NOT NULL,
    verified BOOLEAN NOT NULL,

    -- Embedded Address
    address_house_number VARCHAR(20),
    address_street VARCHAR(100),
    address_city VARCHAR(100),
    address_state VARCHAR(100),
    address_zip_code VARCHAR(20),
    address_country VARCHAR(100),

    FOREIGN KEY (id) REFERENCES users(id)
);

-- CompanyUser fields (extend 'users')
CREATE TABLE company_user (
    id BIGINT PRIMARY KEY,
    company_name VARCHAR(100) NOT NULL,
    tax_id VARCHAR(20) NOT NULL UNIQUE,
    regon VARCHAR(20),
    krs VARCHAR(20),
    contact_person VARCHAR(100) NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    contact_phone VARCHAR(20) NOT NULL,

    -- Embedded CompanyAddress
    company_address_house_number VARCHAR(20),
    company_address_street VARCHAR(100),
    company_address_city VARCHAR(100),
    company_address_state VARCHAR(100),
    company_address_zip_code VARCHAR(20),
    company_address_country VARCHAR(100),

    FOREIGN KEY (id) REFERENCES users(id)
);

-- Table for CarModel
CREATE TABLE car_model (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    engine_type VARCHAR(50),
    seats INT,
    body_type VARCHAR(50),
    production_year INT
);


-- Table for Car
CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    car_model_id BIGINT NOT NULL,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    price_per_day NUMERIC(10, 2) NOT NULL,
    available BOOLEAN NOT NULL,

    CONSTRAINT fk_car_model FOREIGN KEY (car_model_id) REFERENCES car_model(id)
);


-- Reservation Table
CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT fk_res_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_res_car FOREIGN KEY (car_id) REFERENCES cars(id)
);


-- Superclass: billing_document (JOINED inheritance)
CREATE TABLE billing_document (
    id SERIAL PRIMARY KEY,
    issued_date DATE NOT NULL,
    total_amount NUMERIC(10,2) NOT NULL
);

-- Receipt inherits billing_document
CREATE TABLE receipt (
    id BIGINT PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL,
    private_user_id BIGINT NOT NULL,
    reservation_id BIGINT UNIQUE,
    FOREIGN KEY (id) REFERENCES billing_document(id),
    FOREIGN KEY (private_user_id) REFERENCES users(id),
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);

-- Invoice inherits billing_document
CREATE TABLE invoice (
    id BIGINT PRIMARY KEY,
    invoice_number VARCHAR(50) NOT NULL,
    company_user_id BIGINT NOT NULL,
    reservation_id BIGINT UNIQUE,
    FOREIGN KEY (id) REFERENCES billing_document(id),
    FOREIGN KEY (company_user_id) REFERENCES users(id),
    FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);