package com.RentRead.Rent_Read_System.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RentRead.Rent_Read_System.DTO.BookDTO;
import com.RentRead.Rent_Read_System.Exchange.PutBookRequest;
import com.RentRead.Rent_Read_System.Service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createBook(@Valid @RequestBody BookDTO book){
        bookService.createBook(book);
        return ResponseEntity.ok().body("Book created in the system!");
    }

    @PutMapping("/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDTO> updateBook(@PathVariable String title, @RequestBody PutBookRequest putBookRequest){
        BookDTO book = bookService.updateBook(title, putBookRequest);
        return ResponseEntity.ok().body(book);
    }

    @DeleteMapping("/{title}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable String title){
        bookService.deleteBook(title);
        return ResponseEntity.ok().body("Book deleted in the system!");
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> bookList = bookService.getAllBooks();
        return ResponseEntity.ok().body(bookList);
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookDTO>> getBooksByGenre(@PathVariable String genre){
        List<BookDTO> bookList = bookService.getBooksByGenre(genre);
        return ResponseEntity.ok().body(bookList);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable String author){
        List<BookDTO> bookList = bookService.getBooksByAuthor(author);
        return ResponseEntity.ok().body(bookList);
    }

    @GetMapping("/{title}")
    public ResponseEntity<BookDTO> getBookByTitle(@PathVariable String title){
        BookDTO book = bookService.getBookByTitle(title);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/{title}/rent")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> rentBook(@PathVariable String title){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        bookService.rentBook(email, title);
        return ResponseEntity.ok().body("Book has been issued!");
    }

    @GetMapping("/{title}/return")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> returnBook(@PathVariable String title){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        bookService.returnBook(email, title);
        return ResponseEntity.ok().body("Book has been returned!");
    }

}
