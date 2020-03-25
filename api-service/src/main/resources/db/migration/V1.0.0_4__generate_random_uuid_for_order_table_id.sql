CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
ALTER TABLE sales_order ALTER COLUMN id SET DEFAULT uuid_generate_v4();
ALTER TABLE sales_order_item ALTER COLUMN id SET DEFAULT uuid_generate_v4();