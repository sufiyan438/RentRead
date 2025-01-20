package com.RentRead.Rent_Read_System.Service;

import java.util.List;

import com.RentRead.Rent_Read_System.DTO.BookDTO;
import com.RentRead.Rent_Read_System.Exchange.AdminUserResponse;
import com.RentRead.Rent_Read_System.Exchange.PutBookRequest;
import com.RentRead.Rent_Read_System.Model.User;

public interface BookService {
    void createBook(BookDTO book);
    void deleteBook(String title);
    BookDTO updateBook(String title, PutBookRequest putBookRequest);
    List<BookDTO> getAllBooks();
    List<BookDTO> getBooksByGenre(String genre);
    List<BookDTO> getBooksByAuthor(String author);
    BookDTO getBookByTitle(String title);
    void rentBook(String email, String title);
    void returnBook(String email, String title);
    AdminUserResponse findUsersBooks(User user);
    AdminUserResponse findBooksUsers(String title);
}
