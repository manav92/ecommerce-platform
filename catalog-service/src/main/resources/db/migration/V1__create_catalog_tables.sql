create table if not exists products (
  id uuid primary key,
  product_code varchar(255) not null unique,
  name varchar(255) not null,
  brand varchar(255),
  category varchar(255),
  description varchar(2000),
  status varchar(50) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

create table if not exists product_variants (
  id uuid primary key,
  product_id uuid not null references products(id),
  sku varchar(255) not null unique,
  size varchar(255) not null,
  design varchar(255) not null,
  price_amount numeric(12,2) not null,
  currency varchar(3) not null,
  status varchar(50) not null,
  created_at timestamp not null,
  updated_at timestamp not null
);

create table if not exists product_import_jobs (
  id uuid primary key,
  file_name varchar(255) not null,
  file_path varchar(2000) not null,
  uploaded_by varchar(255) not null,
  status varchar(50) not null,
  total_rows integer,
  processed_rows integer,
  success_rows integer,
  failed_rows integer,
  failure_reason varchar(2000),
  submitted_at timestamp not null,
  started_at timestamp,
  completed_at timestamp
);

create table if not exists product_import_errors (
  id uuid primary key,
  job_id uuid not null,
  row_number integer not null,
  raw_record varchar(4000),
  error_code varchar(255) not null,
  error_message varchar(1000) not null,
  created_at timestamp not null
);
