CREATE TABLE sales_order(
  id serial CONSTRAINT sales_order_pkey NOT NULL PRIMARY KEY,
  source TEXT,
  destination TEXT
);

CREATE TABLE sales_order_item(
  id serial CONSTRAINT sales_order_item_pkey NOT NULL PRIMARY KEY,
  name TEXT,
  price DECIMAL,
  order_id INTEGER REFERENCES sales_order(id)
);