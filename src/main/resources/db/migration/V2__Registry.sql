alter table packages add constraint const foreign key (addressee_id) references addressees_table (id);