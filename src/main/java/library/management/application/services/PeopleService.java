package library.management.application.services;

import library.management.application.models.Book;
import library.management.application.models.Person;
import library.management.application.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    @Autowired
    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public List<Person> findAllSortedByName() {
        return peopleRepository.findAll(Sort.by("fullName"));
    }

    public Page<Person> findAllPeopleSortedByNameWithPagination(Integer page, Integer size, Boolean sort) {
        if(sort) {
            return peopleRepository.findAll(PageRequest.of(page, size, Sort.by("fullName")));
        } else {
            return peopleRepository.findAll(PageRequest.of(page, size));
        }
    }

    public Page<Person> findAllPeopleWithPagination(Integer page, Integer size) {
        return peopleRepository.findAll(PageRequest.of(page, size));
    }

    public Person findPersonById(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    public Optional<Person> findByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    @Transactional
    public void createNewPerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, Person updatedPerson) {
        updatedPerson.setPersonId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void deletePerson(int personId) {
        peopleRepository.deleteById(personId);
    }

    public List<Book> findBooksByPersonId(int personId) {
        Optional<Person> person = peopleRepository.findById(personId);
        if(person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                // 1209600000 milliseconds = 14 days
                if (diffInMillies > 1209600000)
                    book.setExpired(true); // книга прострочена
            });

            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Person> findByFullNameStartingWith(String startingWith) {
        return peopleRepository.findByFullNameStartingWith(startingWith);
    }

    public List<Person> findByFullNameContaining(String name) {
        return peopleRepository.findByFullNameContaining(name);
    }

    public List<Person> findByYearOfBirth(int yearOfBirth) {
        return peopleRepository.findByYearOfBirth(yearOfBirth);
    }

    public List<Person> findByFullNameStartingWithAndYearOfBirth(String name, int yearOfBirth) {
        return peopleRepository.findByFullNameStartingWithAndYearOfBirth(name, yearOfBirth);
    }

    public List<Person> findByFullNameContainingAndYearOfBirth(String name, int yearOfBirth) {
        return peopleRepository.findByFullNameContainingAndYearOfBirth(name, yearOfBirth);
    }
}
