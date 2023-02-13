package ru.denusariy.ComixRest.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denusariy.ComixRest.domain.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findAll(Pageable pageable);
    List<Book> findByTitleContainsIgnoreCase(String title);
    List<Book> findByIsAltCoverTrue();
    List<Book> findByIsAutographTrue();
}
