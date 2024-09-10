package spring.antlr.tutorial.service;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.antlr.tutorial.grammar.SimpleQueryLexer;
import spring.antlr.tutorial.grammar.SimpleQueryParser;

@Service
@RequiredArgsConstructor
public class QueryService<T> {

    public Specification<T> parseQuery(String query) {
        SimpleQueryLexer lexer = new SimpleQueryLexer(CharStreams.fromString(query));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleQueryParser parser = new SimpleQueryParser(tokens);
        SimpleQueryParser.InputContext context = parser.input();

        SimpleQueryVisitor<T> visitor = new SimpleQueryVisitor<>();
        return visitor.visitInput(context);
    }
}
