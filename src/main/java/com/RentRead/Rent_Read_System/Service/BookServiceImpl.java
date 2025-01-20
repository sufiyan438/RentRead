package com.RentRead.Rent_Read_System.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RentRead.Rent_Read_System.Controller.AuthController;
import com.RentRead.Rent_Read_System.DTO.BookDTO;
import com.RentRead.Rent_Read_System.Exception.ResourceAlreadyExistsException;
import com.RentRead.Rent_Read_System.Model.Book;
import com.RentRead.Rent_Read_System.Model.User;
import com.RentRead.Rent_Read_System.Repository.BookRepository;
import com.RentRead.Rent_Read_System.Repository.UserRepository;
import com.RentRead.Rent_Read_System.Exception.ResourceNotFoundException;
import com.RentRead.Rent_Read_System.Exchange.AdminUserResponse;
import com.RentRead.Rent_Read_System.Exchange.PutBookRequest;

@Service
public class BookServiceImpl implements BookService{

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final int BOOK_LIMIT = 2;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createBook(BookDTO book) {
        if(bookRepository.existsByTitle(book.getTitle())){
            logger.info("Book already present in system!");
            throw new ResourceAlreadyExistsException("Book already present in system!");
        }
        Book savedBook = bookRepository.save(mapToBook(book));
        logger.info("Book saved!");
    }

    Book mapToBook(BookDTO book){
        ModelMapper modelMapper = new ModelMapper();
        Book b = modelMapper.map(book, Book.class);
        return b;
    }

    BookDTO mapToBookDTO(Book book){
        ModelMapper modelMapper = new ModelMapper();
        BookDTO b = modelMapper.map(book, BookDTO.class);
        return b;
    }

    @Override
    public void deleteBook(String title) {
        Book book = bookRepository.findByTitle(title).
                        orElseThrow(() -> new ResourceNotFoundException("Book not present in the system!"));
        bookRepository.deleteById(book.getId());
    }

    @Override
    public BookDTO updateBook(String title, PutBookRequest putBookRequest) {
        Book book = bookRepository.findByTitle(title).
                        orElseThrow(() -> new ResourceNotFoundException("Book not present in the system!"));
        if(bookRepository.existsByTitle(putBookRequest.getTitle())){
            logger.info("Book already present in system!");
            throw new ResourceAlreadyExistsException("Book already exists");
        }
        if(putBookRequest.getTitle() != null)book.setTitle(putBookRequest.getTitle());
        if(putBookRequest.getGenre() != null)book.setGenre(putBookRequest.getGenre());
        if(putBookRequest.getAuthor() != null)book.setAuthor(putBookRequest.getAuthor());
        Book book2 = bookRepository.save(book);
        logger.info("Book updated!");
        return mapToBookDTO(book2);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();
        List<BookDTO> bookDTOList = new ArrayList<>();
        for(Book b: bookList){
            bookDTOList.add(mapToBookDTO(b));
        }
        return bookDTOList;
    }

    @Override
    public List<BookDTO> getBooksByGenre(String genre) {
        if(!bookRepository.existsByGenre(genre)){
            logger.error("Genre doesn't exist!");
            throw new ResourceNotFoundException("Genre doesn't exist!");
        }
        List<Book> bookList = bookRepository.findByGenre(genre);
        List<BookDTO> bookDTOList = new ArrayList<>();
        for(Book b: bookList){
            bookDTOList.add(mapToBookDTO(b));
        }
        return bookDTOList;
    }

    @Override
    public List<BookDTO> getBooksByAuthor(String author) {
        if(!bookRepository.existsByAuthor(author)){
            logger.error("Author doesn't exist!");
            throw new ResourceNotFoundException("Author doesn't exist!");
        }
        List<Book> bookList = bookRepository.findAllByAuthor(author);
        List<BookDTO> bookDTOList = new ArrayList<>();
        for(Book b: bookList){
            bookDTOList.add(mapToBookDTO(b));
        }
        return bookDTOList;
    }

    @Override
    public BookDTO getBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title).
                            orElseThrow(() -> new ResourceNotFoundException("Book not present in the system!"));
        return mapToBookDTO(book);
    }

    @Override
    public void rentBook(String email, String title) {
        User user = userRepository.findByEmail(email);
        if(user.getBooks().size() >= BOOK_LIMIT){
            logger.info("2 books have been rented!");
            throw new ResourceAlreadyExistsException("You have already rented 2 books. No more renting until one of them is returned!");
        }
        List<Book> books = user.getBooks();
        for(Book book : books){
            if(title.equals(book.getTitle())){
                logger.info("Book has already been rented!");
                throw new ResourceAlreadyExistsException("You have already rented this book!");
            }
        }
        Book bookToBeRented = bookRepository.findByTitle(title).
                                        orElseThrow(() -> new ResourceNotFoundException("Book not present in the system!"));
        bookToBeRented.setAvailibility(false);
        //setting users who have borrorwed
        List<User> usersWhoRented = bookToBeRented.getUsers();
        usersWhoRented.add(user);
        bookToBeRented.setUsers(usersWhoRented);
        //end
        bookRepository.save(bookToBeRented);
        books.add(bookToBeRented);
        user.setBooks(books);
        userRepository.save(user);
        logger.info("Rental processed successfully!");
    }

    @Override
    public void returnBook(String email, String title) {
        Book bookToBeRented = bookRepository.findByTitle(title).
                                        orElseThrow(() -> new ResourceNotFoundException("Book not present in the system!"));
        User user = userRepository.findByEmail(email);
        List<Book> books = user.getBooks();
        Set<Book> bookSet = new HashSet<>(books);
        boolean rented = false;
        for(Book book : bookSet){
            if(title.equals(book.getTitle())){
                bookSet.remove(book);
                rented = true;
            }
        }
        if(!rented){
            logger.info("Book has not been rented!");
            throw new ResourceNotFoundException("Book not rented!");
        }
        bookToBeRented.setAvailibility(true);
        bookRepository.save(bookToBeRented);
        List<Book> bookList = new ArrayList<>(bookSet);
        user.setBooks(bookList);
        userRepository.save(user);
        logger.info("Rental returned successfully!");
    }

    @Override
    public AdminUserResponse findUsersBooks(User user) {
        List<Book> bookList = user.getBooks();
        List<String> title = new ArrayList<>();
        AdminUserResponse aur = new AdminUserResponse();
        for(Book book : bookList){
            title.add(book.getTitle());
        }
        aur.setNames(title);
        return aur;
    }

    @Override
    public AdminUserResponse findBooksUsers(String title) {
        Book book = bookRepository.findByTitle(title).
                            orElseThrow(() -> new ResourceNotFoundException("Book not present in the system!"));
        List<String> emails = new ArrayList<>();
        List<User> users = book.getUsers();
        for(User u : users){
            emails.add(u.getEmail());
        }
        AdminUserResponse aur = new AdminUserResponse();
        aur.setNames(emails);
        return aur;
    }
    
}
