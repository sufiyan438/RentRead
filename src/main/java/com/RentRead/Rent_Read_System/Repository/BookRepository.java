package com.RentRead.Rent_Read_System.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RentRead.Rent_Read_System.Model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
    Optional<Book> findByTitle(String title);
    List<Book> findByGenre(String genre);
    List<Book> findAllByAuthor(String author);
    boolean existsByTitle(String title);
    boolean existsByGenre(String genre);
    boolean existsByAuthor(String author);
}
