ALTER TABLE sales_order_item DROP CONSTRAINT sales_order_item_order_id_fkey;
ALTER TABLE sales_order_item DROP CONSTRAINT sales_order_item_pkey;
ALTER TABLE sales_order DROP CONSTRAINT sales_order_pkey;

ALTER TABLE sales_order
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id SET DATA TYPE UUID USING LPAD(TO_HEX(id), 32, '0')::UUID,
    ADD PRIMARY KEY (id);

ALTER TABLE sales_order_item
    ALTER COLUMN id DROP DEFAULT,
    ALTER COLUMN id SET DATA TYPE UUID USING LPAD(TO_HEX(id), 32, '0')::UUID,
    ADD PRIMARY KEY (id),
    ALTER COLUMN order_id SET DATA TYPE UUID USING LPAD(TO_HEX(order_id), 32, '0')::UUID,
    ADD CONSTRAINT sales_order_item_order_id_fkey FOREIGN KEY (order_id) REFERENCES sales_order(id);