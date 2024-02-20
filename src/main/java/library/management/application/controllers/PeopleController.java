package library.management.application.controllers;

import jakarta.validation.Valid;
import library.management.application.models.Book;
import library.management.application.models.Person;
import library.management.application.services.PeopleService;
import library.management.application.utils.PersonValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    public PeopleController(PeopleService peopleService,
                            PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping
    public String mainPage(Model model,
                           @RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "5") Integer size,
                           @RequestParam(value = "sort_by_name", required = false) Boolean sort) {
        Page<Person> personPage;
        if(sort == null) {
            personPage = peopleService.findAllPeopleWithPagination(page, size);
            model.addAttribute("sort", false);
        } else {
            personPage = peopleService.findAllPeopleSortedByNameWithPagination(page, size, sort);
            model.addAttribute("sort", true);
        }
        model.addAttribute("people", personPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", personPage.getTotalPages());


        return "people/main";
    }

    @GetMapping("/{id}")
    public String showOnePerson(@PathVariable("id") int id,
            Model model) {
        model.addAttribute("person", peopleService.findPersonById(id));

        List<Book> bookList = peopleService.findBooksByPersonId(id);
        if(!bookList.isEmpty()) {
            model.addAttribute("bookList", bookList);
        }

        return "people/showOne";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.createNewPerson(person);
        LOGGER.info("Created new person ");
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findPersonById(id));

        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.updatePerson(id, person);
        LOGGER.info("Person with id = {} was updated", id);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.deletePerson(id);
        LOGGER.info("Person with id = {} was deleted", id);
        return "redirect:/people";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "people/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "year", required = false) Integer year) {
        if(name != null && year == null) {
            model.addAttribute("people", peopleService.findByFullNameContaining(name));
        }
        if(name == null && year != null) {
            model.addAttribute("people", peopleService.findByYearOfBirth(year));
        }
        if(name != null && year != null) {
            model.addAttribute("people", peopleService.findByFullNameContainingAndYearOfBirth(name, year));
        }
        return "people/search";
    }
}
