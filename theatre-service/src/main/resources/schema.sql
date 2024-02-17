drop table if exists booking cascade;
drop table if exists booking_details cascade;
drop table if exists seat_inventory cascade;
create table booking (created_date date, last_modified_date date, created_by uuid not null, id uuid not null, last_modified_by uuid, payment_id uuid, show_id uuid, user_id uuid, payment_status varchar(255) check (payment_status in ('PENDING','CONFIRMED','REJECTED')), status varchar(255) check (status in ('REJECTED','CONFIRMED','PAYMENT_PENDING')), primary key (id));
create table booking_details (created_date date, last_modified_date date, booking_id uuid, created_by uuid not null, id uuid not null, last_modified_by uuid, seat_id uuid unique, primary key (id));
create table seat_inventory (created_date date, last_modified_date date, price numeric(38,2), row_no integer, version integer, created_by uuid not null, id uuid not null, last_modified_by uuid, show_id uuid, availability varchar(255) check (availability in ('AVAILABLE','BLOCKED','BOOKED','UNAVAILABLE')), seat varchar(255), primary key (id));
alter table if exists booking_details add constraint FKpnv1l9jyli4ybj8byn2tf9g5o foreign key (seat_id) references seat_inventory;
alter table if exists booking_details add constraint FKdi4hhcv3pwr6b14qfhf9gahex foreign key (booking_id) references booking;