package spring.antlr.tutorial;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles({"test"})
@Sql(scripts = "/insert_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PersonControllerTest {

    private final MockMvc mockMvc;

    @Test
    public void searchPersons_ValidQuery_ReturnsCorrectResults() throws Exception {
        // Define query to search for Bob
        String query = "name:\"Bob\"";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Bob"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname").value("Brown"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(35))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city").value("Smallville"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthDate").value("1989-04-22"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true));
    }

    @Test
    public void searchPersons_ValidComplexQuery_ReturnsCorrectResults() throws Exception {
        // Define query to search for persons named John who are active
        String query = "name:\"John\" AND isActive:true";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthDate").value("1993-07-15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(28))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city").value("Star City"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].birthDate").value("1995-05-05"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active").value(true));
    }

    @Test
    public void searchPersons_ValidComplexQueryWithOrOperator_ReturnsCorrectResults() throws Exception {
        // Define query to search for persons named John OR those born after 1990
        String query = "name:\"John\" OR birthDate>1990-01-01";

        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].city").value("Springfield"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].birthDate").value("1993-07-15"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true))

                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(25))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].city").value("Metropolis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].birthDate").value("1998-02-20"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active").value(true))

                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].lastname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].age").value(40))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].city").value("Gotham"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].birthDate").value("1983-09-10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].active").value(false))

                .andExpect(MockMvcResultMatchers.jsonPath("$[3].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].lastname").value("Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].age").value(28))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].city").value("Star City"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].birthDate").value("1995-05-05"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[3].active").value(true))

                .andExpect(MockMvcResultMatchers.jsonPath("$[4].name").value("Jane"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].lastname").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].age").value(32))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].city").value("Gotham"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].birthDate").value("1991-03-14"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].active").value(false))

                .andExpect(MockMvcResultMatchers.jsonPath("$[5].name").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].lastname").value("Anderson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].city").value("Bludhaven"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].birthDate").value("2002-08-18"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[5].active").value(true));
    }

    @Test
    public void searchPersons_SizeOfResults_ReturnsExpectedSize() throws Exception {
        // Define query to search for active persons
        String query = "isActive:true";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(8)); // Update based on actual expected size
    }

    @Test
    public void searchPersons_InvalidQuery_ReturnsBadRequest() throws Exception {
        // Define query with invalid field
        String query = "name:\"mehdi\" AND invalid_field:\"value\"";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/persons")
                        .param("query", query)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}