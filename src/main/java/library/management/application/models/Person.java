package library.management.application.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;
    @NotEmpty(message = "Ім'я не може бути відсутнє")
    @Size(min = 5, max = 150, message = "Ім'я повинно бути від 5 до 150 символів")
    @Column(name = "full_name")
    private String fullName;

    @Min(value = 1943, message = "Рік народження не може бути менше за 1943")
    @Max(value = 2016, message = "Рік народження не може бути більше за 2016")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "assignedPerson")
    private List<Book> books;

    public Person() {
    }

    public Person(int personId, String fullName, int yearOfBirth) {
        this.personId = personId;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public Person(String fullName, int yearOfBirth) {
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if(books == null)
            books = new ArrayList<>();
        books.add(book);
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fullName='" + fullName + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}
