-- =========================================
-- V1__create_tables.sql
-- Создание таблиц для CookMate
-- =========================================

-- Таблица пользователей
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Таблица ингредиентов
CREATE TABLE ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Таблица рецептов
CREATE TABLE recipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),

    image_url VARCHAR(512)
);

-- Таблица шагов приготовления (instructions)
CREATE TABLE recipe_instructions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    instruction VARCHAR(2000),
    instruction_order INTEGER,
    CONSTRAINT fk_recipe_instruction FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
);

-- Таблица связи рецепт ↔ ингредиенты
CREATE TABLE recipe_ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipe_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    amount VARCHAR(50),
    CONSTRAINT fk_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_ingredient FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE,
    CONSTRAINT uq_recipe_ingredient UNIQUE (recipe_id, ingredient_id)  -- Уникальное сочетание рецепта и ингредиента
);


