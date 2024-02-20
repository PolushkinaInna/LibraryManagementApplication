package library.management.application.repositories;

import library.management.application.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleStartingWith(String startingWith);
    List<Book> findByAuthorStartingWith(String author);
    List<Book> findByYearOfProduction(int yearOfProduction);
    List<Book> findByAuthorStartingWithAndTitleStartingWith(String author, String title);
    List<Book> findByTitleStartingWithAndYearOfProduction(String title, int yearOfProduction);
    List<Book> findByAuthorStartingWithAndYearOfProduction(String author, int yearOfProduction);
    List<Book> findByTitleStartingWithAndAuthorStartingWithAndYearOfProduction(String title, String author, int yearOfProduction);
}
