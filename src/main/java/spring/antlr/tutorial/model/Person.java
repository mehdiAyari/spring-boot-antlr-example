package spring.antlr.tutorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "city")
    private String city;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "birth_date")
    private LocalDate birthDate;
}
