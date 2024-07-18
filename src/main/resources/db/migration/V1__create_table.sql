CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    categories_code VARCHAR(7) NOT NULL UNIQUE,
    categories_name VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE brand (
    id BIGSERIAL PRIMARY KEY,
    brand_code VARCHAR(7) NOT NULL UNIQUE,
    brand_name VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE sub_categories (
    id BIGSERIAL PRIMARY KEY,
    subcategories_code VARCHAR(7) NOT NULL UNIQUE,
    subcategories_name VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    categories_id BIGINT,
    CONSTRAINT fk_categories
        FOREIGN KEY (categories_id)
        REFERENCES categories (id)
);

CREATE TABLE members (
    id BIGSERIAL PRIMARY KEY,
    member_number VARCHAR(7) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(360) NOT NULL,
    phone_number VARCHAR(13) NOT NULL,
    address TEXT NOT NULL,
    point BIGINT,
    is_deleted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    receipt_number VARCHAR(10) NOT NULL UNIQUE,
    order_date TIMESTAMP WITH TIME ZONE NOT NULL,
    members_id BIGINT,
    CONSTRAINT fk_members
        FOREIGN KEY (members_id)
        REFERENCES members (id)
);

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    produk_code VARCHAR(13) NOT NULL UNIQUE,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL NOT NULL,
    current_stock INTEGER NOT NULL,
    is_deleted BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    subcategories_id BIGINT,
    brand_id BIGINT,
    CONSTRAINT fk_subcategories
        FOREIGN KEY (subcategories_id)
        REFERENCES sub_categories (id),
    CONSTRAINT fk_brand
        FOREIGN KEY (brand_id)
        REFERENCES brand (id)
);

CREATE TABLE promo (
    id BIGSERIAL PRIMARY KEY,
    promo_code VARCHAR(7) NOT NULL UNIQUE,
    promo_name VARCHAR(100) NOT NULL,
    promo_type VARCHAR(255) NOT NULL,
    promo_value DECIMAL NOT NULL,
    start_date TIMESTAMP WITH TIME ZONE NOT NULL,
    end_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    product_id BIGINT,
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES product (id)
);

CREATE TABLE orders_detail (
    id BIGSERIAL PRIMARY KEY,
    quantity INTEGER NOT NULL,
    product_code VARCHAR(13) NOT NULL,
    product_name VARCHAR(100) NOT NULL,
    product_price DECIMAL NOT NULL,
    product_final_price DECIMAL NOT NULL,
    orders_id BIGINT,
    product_id BIGINT,
    CONSTRAINT fk_orders
        FOREIGN KEY (orders_id)
        REFERENCES orders (id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES product (id)
);

CREATE TABLE orders_promo (
    id BIGSERIAL PRIMARY KEY,
    promo_id BIGINT NOT NULL,
    promo_code VARCHAR(7) NOT NULL,
    promo_name VARCHAR(100) NOT NULL,
    promo_type VARCHAR(20) NOT NULL,
    promo_value DECIMAL NOT NULL,
    orders_detail_id BIGINT,
    CONSTRAINT fk_orders_detail
        FOREIGN KEY (orders_detail_id)
        REFERENCES orders_detail (id)
);

CREATE TABLE log_stock (
    id BIGSERIAL PRIMARY KEY,
    log_stock_date TIMESTAMP with time zone,
    type_stock VARCHAR(15) NOT NULL,
    quantity INTEGER NOT NULL,
    product_id BIGINT,
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
        REFERENCES product (id)
);