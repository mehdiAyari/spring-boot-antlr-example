package spring.antlr.tutorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.antlr.tutorial.model.Person;
import spring.antlr.tutorial.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    /**
     * Retrieves a list of persons based on multiple search criteria.
     *
     * @param query A mandatory query parameter specifying the search criteria.
     * @return A list of persons matching the criteria (HTTP status 200 OK)
     * or an error response if the request is invalid (HTTP status 400 Bad Request),
     * or if there is a backend error (HTTP status 500 Internal Server Error).
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Person>> searchPersons(@RequestParam(name = "query") String query) {
        List<Person> persons = personService.findPersonsByQuery(query);
        return ResponseEntity.ok(persons);
    }
}
