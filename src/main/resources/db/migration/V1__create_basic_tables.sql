create table
    customers (
        id bigint auto_increment primary key,
        `name` varchar(255),
        phone varchar(255),
        tax_exempt varchar(255),
        email varchar(255) not null,
        provider_id varchar(255) not null,
        created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
        constraint email_unique unique (email)
    );

create table
    invoices (
        id bigint auto_increment primary key,
        invoice_number varchar(255),
        customer_id bigint,
        `status` varchar(255),
        provider_id varchar(255),
        amount_due bigint,
        amount_paid bigint,
        amount_remaining bigint,
        amount_shipping bigint,
        subtotal bigint,
        subtotal_excluding_tax bigint,
        total bigint,
        total_excluding_tax bigint,
        billing_reason varchar(255),
        days_expire bigint,
        metadata JSON,
        created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
        constraint invoice_customer_id_fk foreign key (customer_id) references customers (id)
    );

create table
    items (
        id bigint unsigned auto_increment primary key,
        provider_id varchar(255),
        invoice_id bigint,
        amount bigint,
        currency varchar(255),
        description varchar(255),
        quantity bigint,
        created_at timestamp NULL DEFAULT CURRENT_TIMESTAMP,
        constraint items_invoice_id_fk foreign key (invoice_id) references invoices (id)
    );