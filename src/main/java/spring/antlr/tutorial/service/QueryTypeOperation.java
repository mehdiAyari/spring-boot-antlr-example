package spring.antlr.tutorial.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.query.sqm.PathElementException;
import spring.antlr.tutorial.exception.BadRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public enum QueryTypeOperation {

    BOOL {
        @Override
        public Predicate buildPredicate(
                CriteriaBuilder builder,
                Path<?> path,
                String fieldName,
                String operator,
                String value
        ) throws PathElementException {
            Boolean boolValue = Boolean.parseBoolean(value);
            return switch (operator) {
                case ":" -> builder.equal(path.get(fieldName), boolValue);
                case "!=" -> builder.notEqual(path.get(fieldName), boolValue);
                default -> throw new BadRequestException("Unsupported operator for boolean: " + operator);
            };
        }
    },
    NUMBER {
        @Override
        public Predicate buildPredicate(
                CriteriaBuilder builder,
                Path<?> path,
                String fieldName,
                String operator,
                String value
        ) throws PathElementException {
            Number numberValue = parseNumber(value);
            return switch (operator) {
                case ":" -> builder.equal(path.get(fieldName), numberValue);
                case ">" -> builder.greaterThan(path.get(fieldName), numberValue.doubleValue());
                case "<" -> builder.lessThan(path.get(fieldName), numberValue.doubleValue());
                case "!=" -> builder.notEqual(path.get(fieldName), numberValue);
                default -> throw new BadRequestException("Unsupported operator for number: " + operator);
            };
        }

        private Number parseNumber(String text) {
            try {
                if (text.contains(".")) {
                    return Double.parseDouble(text);
                } else {
                    return Integer.parseInt(text);
                }
            } catch (NumberFormatException e) {
                throw new BadRequestException("Invalid number format: " + text);
            }
        }
    },
    STRING {
        @Override
        public Predicate buildPredicate(
                CriteriaBuilder builder,
                Path<?> path,
                String fieldName,
                String operator,
                String value
        ) throws PathElementException {
            return switch (operator) {
                case ":" -> builder.equal(path.get(fieldName), value);
                case "!=" -> builder.notEqual(path.get(fieldName), value);
                default -> throw new BadRequestException("Unsupported operator for string: " + operator);
            };
        }
    },
    DATE {
        @Override
        public Predicate buildPredicate(
                CriteriaBuilder builder,
                Path<?> path,
                String fieldName,
                String operator,
                String value
        ) throws PathElementException {
            LocalDate dateValue = parseDate(value);
            return switch (operator) {
                case ":" -> builder.equal(path.get(fieldName), dateValue);
                case ">" -> builder.greaterThan(path.get(fieldName), dateValue);
                case "<" -> builder.lessThan(path.get(fieldName), dateValue);
                case "!=" -> builder.notEqual(path.get(fieldName), dateValue);
                default -> throw new BadRequestException("Unsupported operator for date: " + operator);
            };
        }

        private LocalDate parseDate(String text) {
            try {
                return LocalDate.parse(text);
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Invalid date format: " + text);
            }
        }
    };

    public abstract Predicate buildPredicate(
            CriteriaBuilder builder,
            Path<?> path,
            String fieldName,
            String operator,
            String value
    ) throws PathElementException;
}