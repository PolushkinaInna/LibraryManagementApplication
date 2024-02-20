package library.management.application.services;

import library.management.application.models.Person;
import library.management.application.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import library.management.application.models.Book;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    @Autowired
    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAllBooksSortedByYear() {
        return booksRepository.findAll(Sort.by("yearOfProduction"));
    }


    public Page<Book> findAllBooksSortedByYearWithPagination(Integer page, Integer size, Boolean sortByYear) {
        if(sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, size, Sort.by("yearOfProduction")));
        } else {
            return booksRepository.findAll(PageRequest.of(page, size));
        }
    }

    public Page<Book> findAllBookWithPagination(Integer page, Integer size) {
        return booksRepository.findAll(PageRequest.of(page, size));
    }

    public Book findBookById(int id) {
        Optional<Book> book = booksRepository.findById(id);
        book.ifPresent(this::setBookExpiredValue);

        return book.orElse(null);
    }

    private void setBookExpiredValue(Book book) {
        if (book.getAssignedPerson() != null) {
            long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
            // 1209600000 milliseconds = 14 days
            if (diffInMillies > 1209600000)
                book.setExpired(true);
        }
    }

    @Transactional
    public void createNewBook(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook) {
        Book book = booksRepository.findById(id).get();

        updatedBook.setBookId(id);
        updatedBook.setAssignedPerson(book.getAssignedPerson());

        booksRepository.save(updatedBook);
    }

    public Person findBookOwner(int id) {
        return booksRepository.findById(id).map(Book::getAssignedPerson).orElse(null);
    }

    @Transactional
    public void assignBookToPerson(int bookId, Person selectedPerson) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setAssignedPerson(selectedPerson);
                    book.setTakenAt(new Date());
                }
        );
    }

    @Transactional
    public void releaseBook(int bookId) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setAssignedPerson(null);
                    book.setTakenAt(null);
                }
        );
    }

    @Transactional
    public void moveExpiredDate(int bookId) {
        booksRepository.findById(bookId).ifPresent(
                book -> {
                    book.setTakenAt(new Date());
                }
        );
    }

    @Transactional
    public void deleteBook(int bookId) {
        booksRepository.deleteById(bookId);
    }

    public List<Book> findByTitle(String startingWith) {
        return booksRepository.findByTitleStartingWith(startingWith);
    }

    public List<Book> findByAuthor(String author) {
        return booksRepository.findByAuthorStartingWith(author);
    }

    public List<Book> findByYearOfProduction(int yearOfProduction) {
        return booksRepository.findByYearOfProduction(yearOfProduction);
    }
    public List<Book> findByAuthorAndTitle(String author, String title) {
        return booksRepository.findByAuthorStartingWithAndTitleStartingWith(author, title);
    }
    public List<Book> findByTitleAndYearOfProduction(String title, int yearOfProduction) {
        return booksRepository.findByTitleStartingWithAndYearOfProduction(title, yearOfProduction);
    }
    public List<Book> findByAuthorAndYearOfProduction(String author, int yearOfProduction) {
        return booksRepository.findByAuthorStartingWithAndYearOfProduction(author, yearOfProduction);
    }
    public List<Book> findByTitleAndAuthorAndYearOfProduction(String title, String author, int yearOfProduction) {
        return booksRepository.findByTitleStartingWithAndAuthorStartingWithAndYearOfProduction(title, author, yearOfProduction);
    }
}
