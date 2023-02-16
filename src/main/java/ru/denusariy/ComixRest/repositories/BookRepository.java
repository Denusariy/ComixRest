package ru.denusariy.ComixRest.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.ComixRest.domain.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    //Поиск книг с использованием пагинации
    Page<Book> findAll(Pageable pageable);

    //Поиск книг по части названия
    List<Book> findByTitleContainsIgnoreCase(String title);

    //Поиск книг с альтернативной обложкой
    List<Book> findByIsAltCoverTrue();

    //Поиск книг с автографом
    List<Book> findByIsAutographTrue();
}
