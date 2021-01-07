package com.flow.base.repository;

import javax.persistence.criteria.JoinType;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class SqlHelper {
    public enum ComparatorOperator {EQUAL, NOT_EQUAL, LIKE, NOT_LIKE, BETWEEN, GREATER_THAN, GREATER_EQUAL, LESS_THAN, LESS_EQUAL, IN}

    public enum LogicalOperator {AND, OR}

    public enum OrderDirection {ASC, DESC}

    public enum SqlOperation {SELECT, UPDATE, INSERT, DELETE}

    private String table;
    private String alias;
    private SqlOperation sqlOperation;
    private List<Entry> entries;
    private List<OrderEntry> orders;

    public static SqlHelper select(String table) {
        SqlHelper helper = new SqlHelper();
        helper.sqlOperation = SqlOperation.SELECT;
        helper.table = table;
        return helper;
    }

    public static SqlHelper select(String table, String alias) {
        SqlHelper helper = select(table);
        helper.alias = alias;
        return helper;
    }

    public static SqlHelper update(String table) {
        SqlHelper helper = new SqlHelper();
        helper.sqlOperation = SqlOperation.UPDATE;
        helper.table = table;
        return helper;
    }

    public static SqlHelper insert(String table) {
        SqlHelper helper = new SqlHelper();
        helper.sqlOperation = SqlOperation.INSERT;
        helper.table = table;
        return helper;
    }

    public static SqlHelper delete(String table) {
        SqlHelper helper = new SqlHelper();
        helper.sqlOperation = SqlOperation.DELETE;
        helper.table = table;
        return helper;
    }

    public SqlHelper eq(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.EQUAL, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper not(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.NOT_EQUAL, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper ge(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.GREATER_EQUAL, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper gt(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.GREATER_THAN, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper le(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.LESS_EQUAL, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper lt(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.LESS_THAN, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper like(String fieldName, Object value) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.LIKE, value, null, LogicalOperator.AND));
        return this;
    }

    public SqlHelper between(String fieldName, Object value1, Object value2) {
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.BETWEEN, value1, value2, LogicalOperator.AND));
        return this;
    }

    public SqlHelper in(String fieldName, Object value1) {
        assert (value1 instanceof Collection);
        this.whereEntries.add(new WhereEntry(aliasField(fieldName, true), aliasField(fieldName, false), ComparatorOperator.BETWEEN, value1, null, LogicalOperator.AND));
        return this;
    }

    private void demandsOperation(SqlOperation sqlOperation) {
        if (!this.sqlOperation.equals(sqlOperation)) {
            throw new RuntimeException("非法的sql操作 " + this.sqlOperation.name() + ".");
        }
    }

    private String aliasField(String fieldName, boolean alias) {
        if (alias) return fieldName.indexOf("\\.") != -1 ? fieldName.split("\\.")[0] : this.table;
        return fieldName.indexOf("\\.") != -1 ? fieldName.split("\\.")[1] : fieldName;
    }


    public SqlHelper or(Consumer<SqlHelper> action) {
        action.accept(this);
        return this;
    }

    private class Entry {

        private String fieldName;

        private ComparatorOperator comparatorOperator;

        private Object valueIni;

        private Object valueEnd;

        private LogicalOperator logicalOperator;

        private List<Entry> ors;

        public Entry(String fieldName
                , ComparatorOperator comparatorOperator
                , Object valueIni
                , Object valueEnd
                , LogicalOperator logicalOperator) {
            this.fieldName = fieldName;
            this.comparatorOperator = comparatorOperator;
            this.valueIni = valueIni;
            this.valueEnd = valueEnd;
            this.logicalOperator = logicalOperator;
        }
    }

    private class OrderEntry {

        private List<String> fieldNames;

        private OrderDirection order;

        public OrderEntry(List<String> fieldNames, OrderDirection order) {
            this.fieldNames = fieldNames;
            this.order = order;
        }
    }
}
