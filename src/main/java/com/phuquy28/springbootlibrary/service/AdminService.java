package com.phuquy28.springbootlibrary.service;

import com.phuquy28.springbootlibrary.dao.BookRepository;
import com.phuquy28.springbootlibrary.dao.CheckoutRepository;
import com.phuquy28.springbootlibrary.dao.ReviewRepository;
import com.phuquy28.springbootlibrary.entity.Book;
import com.phuquy28.springbootlibrary.requestmodels.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class AdminService {
    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;
    private ReviewRepository reviewRepository;

    @Autowired
    public AdminService(BookRepository bookRepository, CheckoutRepository checkoutRepository, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.reviewRepository = reviewRepository;
    }

    public void decreaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }
        Book book1 = book.get();
        if (book1.getCopies() == 0) {
            throw new Exception("Book is out of stock");
        }
        book1.setCopies(book1.getCopies() - 1);
        book1.setCopiesAvailable(book1.getCopiesAvailable() - 1);
        bookRepository.save(book1);
    }

    public void increaseBookQuantity(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);
        if (!book.isPresent()) {
            throw new Exception("Book not found");
        }
        Book book1 = book.get();
        book1.setCopies(book1.getCopies() + 1);
        book1.setCopiesAvailable(book1.getCopiesAvailable() + 1);
        bookRepository.save(book1);
    }

    public void postBook(AddBookRequest addBookRequest) {
        Book book = new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());
        bookRepository.save(book);
    }

    public void deleteBook(Long bookId) throws Exception {
        Optional<Book> book = bookRepository.findById(bookId);

        if (!book.isPresent()){
            throw new Exception ("Book not found");
        }

        bookRepository.delete(book.get());
        checkoutRepository.deleteAllByBookId(bookId);
        reviewRepository.deleteAllByBookId(bookId);
    }
}
