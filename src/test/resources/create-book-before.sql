delete from comic;
delete from book;

insert into book(id, title, year, format, autograph, alt_cover, signature) values
    (1, 'Batman New52', 2010, 'TPB', false, false, null);

insert into book(id, title, year, format, autograph, alt_cover, signature) values
    (2, 'Batman and Robin', 2010, 'TPB', true, true, 'Frank Miller');

insert into comic(id, book_id, title, year, writer, artist) values
    (1, 1, 'Batman New52 Vol 1 #1', 2010, 'Jeff Parker', 'Ron Lim');

insert into comic(id, book_id, title, year, writer, artist) values
    (2, 2, 'Batman and Robin Vol 1 #1', 2009, 'John Byrne', 'Frank Miller');

insert into comic(id, book_id, title, year, writer, artist) values
    (3, 2, 'Batman and Robin Vol 1 #2', 2009, 'Jonathan Hickman', 'Frank Miller');

alter sequence comic_id_seq restart with 10;

