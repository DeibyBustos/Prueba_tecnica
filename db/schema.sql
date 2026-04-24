CREATE DATABASE IF NOT EXISTS franchise_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE franchise_db;

-- Table: franchises

CREATE TABLE IF NOT EXISTS franchises (
                                          id   BIGINT       NOT NULL AUTO_INCREMENT,
                                          name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_franchises     PRIMARY KEY (id),
    CONSTRAINT uq_franchise_name UNIQUE (name)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- Table: branches

CREATE TABLE IF NOT EXISTS branches (
                                        id           BIGINT       NOT NULL AUTO_INCREMENT,
                                        name         VARCHAR(255) NOT NULL,
    franchise_id BIGINT       NOT NULL,
    CONSTRAINT pk_branches PRIMARY KEY (id),
    CONSTRAINT fk_branches_franchise
    FOREIGN KEY (franchise_id)
    REFERENCES franchises (id)
    ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_branches_franchise_id ON branches (franchise_id);


-- Table: products

CREATE TABLE IF NOT EXISTS products (
                                        id        BIGINT       NOT NULL AUTO_INCREMENT,
                                        name      VARCHAR(255) NOT NULL,
    stock     INT          NOT NULL DEFAULT 0,
    branch_id BIGINT       NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT fk_products_branch
    FOREIGN KEY (branch_id)
    REFERENCES branches (id)
    ON DELETE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE INDEX idx_products_branch_id ON products (branch_id);