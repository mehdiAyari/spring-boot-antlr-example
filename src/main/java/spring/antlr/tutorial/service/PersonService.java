package spring.antlr.tutorial.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.antlr.tutorial.model.Person;
import spring.antlr.tutorial.repository.PersonRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository ;
    private final QueryService<Person> queryService;

    public List<Person> findPersonsByQuery(String query) {
        // Convert the query string to a Specification
        Specification<Person> specification = queryService.parseQuery(query);

        // Use the Specification to find persons from the repository
        return personRepository.findAll(specification);
    }
}
