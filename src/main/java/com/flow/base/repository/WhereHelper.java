package com.flow.base.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WhereHelper {

    public enum ComparatorOperator {EQUAL, NOT_EQUAL, LIKE, NOT_LIKE, BETWEEN, GREATER_THAN, GREATER_EQUAL, LESS_THAN, LESS_EQUAL, IN}

    public enum LogicalOperator {AND, OR}

    private List<AndEntry> ands = new ArrayList<>();

    private List<OrEntry> ors = new ArrayList<>();

    private class AndEntry {
        private String fieldName;

        private ComparatorOperator comparatorOperator;

        private Object valueIni;

        private Object valueEnd;

        public AndEntry(String fieldName, ComparatorOperator comparatorOperator, Object valueIni, Object valueEnd) {
            this.fieldName = fieldName;
            this.comparatorOperator = comparatorOperator;
            this.valueIni = valueIni;
            this.valueEnd = valueEnd;
        }
    }

    private class OrEntry {
        private List<AndEntry> entries = new ArrayList<>();

        public OrEntry eq(String field, String value) {
            entries.add(new AndEntry(field, ComparatorOperator.EQUAL, value, null));
            return this;
        }

        public OrEntry ge(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.GREATER_EQUAL, value, null));
            return this;
        }

        public OrEntry le(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.LESS_EQUAL, value, null));
            return this;
        }

        public OrEntry gt(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.GREATER_THAN, value, null));
            return this;
        }

        public OrEntry lt(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.LESS_THAN, value, null));
            return this;
        }

        public OrEntry like(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.LIKE, value, null));
            return this;
        }

        public OrEntry notLike(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.NOT_LIKE, value, null));
            return this;
        }

        public OrEntry between(String field,Object value1,Object value2){
            entries.add(new AndEntry(field, ComparatorOperator.BETWEEN, value1, value2));
            return this;
        }

        public OrEntry in(String field,Object value){
            entries.add(new AndEntry(field, ComparatorOperator.IN, value, null));
            return this;
        }
    }

    public WhereHelper and(String filed, ComparatorOperator comparatorOperator, String value) {

    }

    public WhereHelper or(Consumer<OrEntry> action) {
        OrEntry orEntry = new OrEntry();
        action.accept(orEntry);
        this.ors.add(orEntry);
        return this;
    }
}
