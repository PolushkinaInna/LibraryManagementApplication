package library.management.application.controllers;

import jakarta.validation.Valid;
import library.management.application.models.Book;
import library.management.application.models.Person;
import library.management.application.services.BooksService;
import library.management.application.services.PeopleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BooksController.class);

    @Autowired
    public BooksController(BooksService booksService,
                           PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping
    public String mainPage(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageStart,
                           @RequestParam(defaultValue = "5") Integer size,
                           @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear) {
        Page<Book> bookPage;
        if(sortByYear == null) {
            bookPage = booksService.findAllBookWithPagination(pageStart, size);
            model.addAttribute("sort", false);
        } else {
            bookPage = booksService.findAllBooksSortedByYearWithPagination(pageStart, size, sortByYear);
            model.addAttribute("sort", true);
        }
        model.addAttribute("books", bookPage.getContent());
        model.addAttribute("currentPage", pageStart);
        model.addAttribute("totalPages", bookPage.getTotalPages());


        return "books/mainList";
    }

    @GetMapping("/{id}")
    public String showOneBook(@PathVariable("id") int id,
                              Model model,
                              @ModelAttribute("person") Person person) {
        Book book = booksService.findBookById(id);
        model.addAttribute("book", book);

        Person bookOwner = booksService.findBookOwner(id);
        if(bookOwner == null) {
            model.addAttribute("people", peopleService.findAll());
        } else {
            model.addAttribute("bookOwner", bookOwner);
        }

        return "books/showOne";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.createNewBook(book);
        LOGGER.info("Book {} was created", book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findBookById(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if(bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.updateBook(id, book);
        LOGGER.info("Book with id = {} was updated", id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        booksService.releaseBook(id);

        LOGGER.info("Book with id = {} was released", id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id,
                             @ModelAttribute("person") Person person) {
        booksService.assignBookToPerson(id, person);
        LOGGER.info("Book with id = {} was assigned to user with id = {}", id, person.getPersonId());
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/move")
    public String moveExpiredDate(@PathVariable("id") int id) {
        booksService.moveExpiredDate(id);
        LOGGER.info("Expired date was moved for book with id = {}", id);
        return "redirect:/books/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.deleteBook(id);
        LOGGER.info("Book with id = {} was deleted", id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model,
                             @RequestParam(value = "title", required = false) String title,
                             @RequestParam(value = "author", required = false) String author,
                             @RequestParam(value = "year", required = false) Integer year) {
        if(title == null && author == null && year == null) {
            model.addAttribute("books", Collections.emptyList());
        }
        if(title != null && author == null && year == null) {
            model.addAttribute("books", booksService.findByTitle(title));
        }
        if(title == null && author != null && year == null) {
            model.addAttribute("books", booksService.findByAuthor(author));
        }
        if(title == null && author == null && year != null) {
            model.addAttribute("books", booksService.findByYearOfProduction(year));
        }
        if(title != null && author != null && year == null) {
            model.addAttribute("books",
                    booksService.findByAuthorAndTitle(author, title));
        }
        if(title != null && author == null && year != null) {
            model.addAttribute("books",
                    booksService.findByTitleAndYearOfProduction(title, year));
        }
        if(title == null && author != null && year != null) {
            model.addAttribute("books",
                    booksService.findByAuthorAndYearOfProduction(author, year));
        }
        if(title != null && author != null && year != null) {
            model.addAttribute("books",
                    booksService.findByTitleAndAuthorAndYearOfProduction(title, author, year));
        }


        return "books/search";
    }
}
