package library.management.application.repositories;

import library.management.application.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByFullName(String fullName);
    List<Person> findByFullNameStartingWith(String startingWith);
    List<Person> findByYearOfBirth(int yearOfBirth);
    List<Person> findByFullNameStartingWithAndYearOfBirth(String fullName, int yearOfBirth);
    List<Person> findByFullNameContaining(String name);
    List<Person> findByFullNameContainingAndYearOfBirth(String name, int year);
}
