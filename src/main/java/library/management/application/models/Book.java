package library.management.application.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Entity
@Table
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookId;

    @NotEmpty(message = "Назва не може бути порожньою")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Книга не може бути без автора")
    @Column(name = "author")
    private String author;

    @Min(value = 1950, message = "Мінімальний рік видання - 1950")
    @Max(value = 2024, message = "Рік видання не може бути більше 2024")
    @Column(name = "year_of_production")
    private int yearOfProduction;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person assignedPerson;

    @Column(name = "taken_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenAt;

    @Transient
    private boolean expired;


    private static final Logger LOGGER = LoggerFactory.getLogger(Book.class);

    public Book() {
    }

    public Book(int bookId, String name, String author, int year) {
        this.bookId = bookId;
        this.title = name;
        this.author = author;
        this.yearOfProduction = year;
    }

    public Book(String name, String author, int year) {
        this.title = name;
        this.author = author;
        this.yearOfProduction = year;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfProduction() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction = yearOfProduction;
    }

    public Person getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(Person assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public boolean isExpired() {
      /* if((takenAt.getTime() - 1209600000) <= 0)
           expired = true;*/
        LOGGER.info("get expired date");
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
