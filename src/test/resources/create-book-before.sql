delete from comix_rest_db_test.public.comic;
delete from comix_rest_db_test.public.book;

insert into comix_rest_db_test.public.book(id, title, year, format, autograph, alt_cover, signature) values
(1, 'Бэтмен Навсегда', 2010, 'TPB', false, false, null);

insert into comix_rest_db_test.public.book(id, title, year, format, autograph, alt_cover, signature) values
    (2, 'Бэтмен и Робин', 2010, 'TPB', false, false, null);
