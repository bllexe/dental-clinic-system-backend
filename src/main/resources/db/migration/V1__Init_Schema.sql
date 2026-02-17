-- Enable UUID extension if needed (though we use BIGSERIAL for IDs mostly)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table: clinics
CREATE TABLE IF NOT EXISTS clinics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    phone_number VARCHAR(255),
    email VARCHAR(255),
    tax_number VARCHAR(255),
    active BOOLEAN DEFAULT TRUE,
    subscription_end_date DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: users
CREATE TABLE  IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    role VARCHAR(50) NOT NULL,
    clinic_id BIGINT REFERENCES clinics(id),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: patients
CREATE TABLE IF NOT EXISTS patients (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    identity_number VARCHAR(50),
    birth_date DATE,
    gender VARCHAR(20),
    address VARCHAR(255),
    emergency_contact_name VARCHAR(255),
    emergency_contact_phone VARCHAR(50),
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: dentists
CREATE TABLE IF NOT EXISTS dentists (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id) NOT NULL,
    specialization VARCHAR(255),
    license_number VARCHAR(100),
    biography VARCHAR(1000),
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: treatments
CREATE TABLE IF NOT EXISTS treatments (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    default_price NUMERIC(19, 2),
    estimated_duration_minutes INTEGER,
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: appointments
CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT REFERENCES patients(id) NOT NULL,
    dentist_id BIGINT REFERENCES dentists(id) NOT NULL,
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    notes VARCHAR(255),
    treatment_id BIGINT REFERENCES treatments(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: invoices
CREATE TABLE IF NOT EXISTS invoices (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT REFERENCES patients(id) NOT NULL,
    appointment_id BIGINT REFERENCES appointments(id),
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    invoice_number VARCHAR(100) NOT NULL UNIQUE,
    total_amount NUMERIC(19, 2),
    paid_amount NUMERIC(19, 2),
    due_date DATE,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: inventory_items
CREATE TABLE IF NOT EXISTS inventory_items (
    id BIGSERIAL PRIMARY KEY,
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    name VARCHAR(255) NOT NULL,
    sku VARCHAR(100),
    quantity INTEGER,
    low_stock_threshold INTEGER,
    unit VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: medical_records
CREATE TABLE IF NOT EXISTS medical_records (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT REFERENCES patients(id) NOT NULL,
    appointment_id BIGINT REFERENCES appointments(id),
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    diagnosis TEXT,
    treatment_notes TEXT,
    prescription TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: dentist_availabilities
CREATE TABLE IF NOT EXISTS dentist_availabilities (
    id BIGSERIAL PRIMARY KEY,
    dentist_id BIGINT REFERENCES dentists(id) NOT NULL,
    clinic_id BIGINT REFERENCES clinics(id) NOT NULL,
    day_of_week VARCHAR(20),
    start_time TIME,
    end_time TIME,
    is_day_off BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Table: refresh_tokens
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT REFERENCES users(id),
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT,
    is_deleted BOOLEAN DEFAULT FALSE
);

-- Indexes for performance optimization
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_clinic_id ON users(clinic_id);

CREATE INDEX idx_patients_user_id ON patients(user_id);
CREATE INDEX idx_patients_clinic_id ON patients(clinic_id);
CREATE INDEX idx_patients_identity_number ON patients(identity_number);

CREATE INDEX idx_dentists_user_id ON dentists(user_id);
CREATE INDEX idx_dentists_clinic_id ON dentists(clinic_id);

CREATE INDEX idx_treatments_clinic_id ON treatments(clinic_id);

CREATE INDEX idx_appointments_patient_id ON appointments(patient_id);
CREATE INDEX idx_appointments_dentist_id ON appointments(dentist_id);
CREATE INDEX idx_appointments_clinic_id ON appointments(clinic_id);
CREATE INDEX idx_appointments_treatment_id ON appointments(treatment_id);
CREATE INDEX idx_appointments_start_time ON appointments(start_time);
CREATE INDEX idx_appointments_end_time ON appointments(end_time);
CREATE INDEX idx_appointments_status ON appointments(status);

CREATE INDEX idx_invoices_patient_id ON invoices(patient_id);
CREATE INDEX idx_invoices_appointment_id ON invoices(appointment_id);
CREATE INDEX idx_invoices_clinic_id ON invoices(clinic_id);
CREATE INDEX idx_invoices_invoice_number ON invoices(invoice_number);

CREATE INDEX idx_inventory_items_clinic_id ON inventory_items(clinic_id);
CREATE INDEX idx_inventory_items_sku ON inventory_items(sku);

CREATE INDEX idx_medical_records_patient_id ON medical_records(patient_id);
CREATE INDEX idx_medical_records_appointment_id ON medical_records(appointment_id);
CREATE INDEX idx_medical_records_clinic_id ON medical_records(clinic_id);

CREATE INDEX idx_dentist_availabilities_dentist_id ON dentist_availabilities(dentist_id);
CREATE INDEX idx_dentist_availabilities_clinic_id ON dentist_availabilities(clinic_id);
CREATE INDEX idx_dentist_availabilities_day_of_week ON dentist_availabilities(day_of_week);

CREATE INDEX idx_refresh_tokens_token ON refresh_tokens(token);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens(user_id);
