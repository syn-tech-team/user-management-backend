-- =====================================================
-- Database Setup Script for Distributed Environment
-- =====================================================
-- This script creates the database, tables, and populates data
-- for a distributed user management system
-- =====================================================
-- 1. Create Database
-- =====================================================
drop database if exists userdb20251202;
create database userdb20251202;

-- Use the created database
USE userdb20251202;

-- 2. Create Tables
-- =====================================================

-- Create USERS table (distributed-friendly with UUID primary keys)
CREATE TABLE USERS (
  ID VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
  FIRST_NAME VARCHAR(255) NOT NULL,
  LAST_NAME VARCHAR(255) NOT NULL,
  EMAIL VARCHAR(255) NOT NULL UNIQUE,
  PASSWORD VARCHAR(255) NOT NULL,
  IMAGE VARCHAR(1024),
  CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_email (EMAIL),
  INDEX idx_created_at (CREATED_AT)
);

-- Create ADDRESSES table (distributed-friendly with UUID foreign keys)
CREATE TABLE ADDRESSES (
  ID VARCHAR(36) PRIMARY KEY DEFAULT (UUID()),
  USER_ID VARCHAR(36) NOT NULL,
  STREET VARCHAR(255) NOT NULL,
  CITY VARCHAR(100) NOT NULL,
  STATE VARCHAR(100),
  COUNTRY VARCHAR(100) NOT NULL,
  ZIP_CODE VARCHAR(20) NOT NULL,
  TYPE ENUM('HOME', 'OFFICE', 'BILLING', 'SHIPPING') NOT NULL,
  PRIMARY_ADDRESS BOOLEAN DEFAULT FALSE,
  CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT FK_ADDRESSES_USERS
    FOREIGN KEY (USER_ID) REFERENCES USERS(ID)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  INDEX idx_user_id (USER_ID),
  INDEX idx_type (TYPE),
  INDEX idx_primary_address (PRIMARY_ADDRESS),
  INDEX idx_created_at (CREATED_AT)
);