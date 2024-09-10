package spring.antlr.tutorial.service;

import org.hibernate.query.sqm.PathElementException;
import org.springframework.data.jpa.domain.Specification;
import spring.antlr.tutorial.exception.BadRequestException;
import spring.antlr.tutorial.grammar.SimpleQueryBaseVisitor;
import spring.antlr.tutorial.grammar.SimpleQueryParser;

public class SimpleQueryVisitor<T> extends SimpleQueryBaseVisitor<Specification> {

    @Override
    public Specification<T> visitOperatorQuery(SimpleQueryParser.OperatorQueryContext ctx) {
        // Check if the query is a composite query (with AND/OR) or a single criteria
        if (ctx.left != null && ctx.right != null) {
            Specification<T> leftSpec = visit(ctx.left);
            Specification<T> rightSpec = visit(ctx.right);

            if (ctx.logicalOp.getText().equalsIgnoreCase("AND")) {
                return (root, query, cb) -> cb.and(leftSpec.toPredicate(root, query, cb), rightSpec.toPredicate(root, query, cb));
            } else if (ctx.logicalOp.getText().equalsIgnoreCase("OR")) {
                return (root, query, cb) -> cb.or(leftSpec.toPredicate(root, query, cb), rightSpec.toPredicate(root, query, cb));
            }
        }
        // Otherwise, it is a single criteria query
        return visitChildren(ctx);
    }

    @Override
    public Specification<T> visitPriorityQuery(SimpleQueryParser.PriorityQueryContext ctx) {
        return visit(ctx.query());
    }

    @Override
    public Specification<T> visitCriteriaQuery(SimpleQueryParser.CriteriaQueryContext ctx) {
        return visit(ctx.criteria());
    }


    @Override
    public Specification<T> visitCriteria(SimpleQueryParser.CriteriaContext ctx) {
        String key = ctx.key().getText();
        String operator = ctx.op().getText();
        String value = ctx.value().getText().replace("\"", "");  // Remove quotes around string values

        QueryTypeOperation operationType = determineOperationType(ctx.value());
        return (root, query, cb) -> {
            try {
                return operationType.buildPredicate(cb, root, key, operator, value);
            } catch (PathElementException e) {
                throw new BadRequestException(e.getMessage());
            }
        };
    }

    private QueryTypeOperation determineOperationType(SimpleQueryParser.ValueContext ctx) {
        if (ctx.BOOL() != null) {
            return QueryTypeOperation.BOOL;
        } else if (ctx.NUMBER() != null) {
            return QueryTypeOperation.NUMBER;
        } else if (ctx.STRING() != null) {
            return QueryTypeOperation.STRING;
        } else if (ctx.DATE() != null) {
            return QueryTypeOperation.DATE;
        } else {
            throw new BadRequestException("Unsupported value type: " + ctx.getText());
        }
    }

    @Override
    public Specification<T> visitInput(SimpleQueryParser.InputContext ctx) {
        return visit(ctx.query());
    }
}
