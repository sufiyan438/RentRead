package com.RentRead.Rent_Read_System;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.RentRead.Rent_Read_System.Service.BookServiceImpl;
import com.RentRead.Rent_Read_System.Repository.BookRepository;
import com.RentRead.Rent_Read_System.Repository.UserRepository;
import com.RentRead.Rent_Read_System.DTO.BookDTO;
import com.RentRead.Rent_Read_System.Exception.ResourceAlreadyExistsException;
import com.RentRead.Rent_Read_System.Exception.ResourceNotFoundException;
import com.RentRead.Rent_Read_System.Model.Book;
import com.RentRead.Rent_Read_System.Model.User;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    private BookDTO bookDTO;
    private Book book;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        bookDTO = new BookDTO("Test Book", "Author", "Genre", true);
        book = new Book(1L, "Test Book", "Author", "Genre", true, null, null, null);
        user = new User();
        user.setEmail("user@example.com");
        user.setBooks(new ArrayList<>());
    }

    @Test
    public void testCreateBookSuccess() {
        when(bookRepository.existsByTitle(bookDTO.getTitle())).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        bookService.createBook(bookDTO);
        verify(bookRepository, times(1)).existsByTitle(bookDTO.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testCreateBookBookAlreadyExists() {
        when(bookRepository.existsByTitle(bookDTO.getTitle())).thenReturn(true);
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookService.createBook(bookDTO);
        });
        verify(bookRepository, times(1)).existsByTitle(bookDTO.getTitle());
    }

    @Test
    public void testGetBookByTitleSuccess() {
        when(bookRepository.findByTitle(bookDTO.getTitle())).thenReturn(Optional.of(book));
        BookDTO result = bookService.getBookByTitle(bookDTO.getTitle());
        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
        verify(bookRepository, times(1)).findByTitle(bookDTO.getTitle());
    }

    @Test
    public void testGetBookByTitleNotFound() {
        when(bookRepository.findByTitle(bookDTO.getTitle())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookByTitle(bookDTO.getTitle());
        });
        verify(bookRepository, times(1)).findByTitle(bookDTO.getTitle());
    }

    @Test
    public void testRentBookAlreadyRented() {
        user.getBooks().add(book);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            bookService.rentBook(user.getEmail(), bookDTO.getTitle());
        });
        verify(bookRepository, times(0)).save(any(Book.class));
    }
}
