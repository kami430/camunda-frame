package com.camunda.demo.base.repository;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * 旧版params已废弃
 */
@Deprecated
public class Params2 {
    private final List<String> and;
    private final LinkedHashMap<String, Object> andPara;

    private final List<String> or;
    private final LinkedHashMap<String, Object> orPara;

    private final List<String> between;
    private final LinkedHashMap<String, List> betweenPara;

    private final List<String> lessThan;
    private final LinkedHashMap<String, Object> lessThanPara;

    private final List<String> moreThan;
    private final LinkedHashMap<String, Object> moreThanPara;

    /* private final List<String> isNull;
    private final LinkedHashMap<String ,Object> isNullPara;

    private final List<String> isNotNull;
    private final LinkedHashMap<String ,Object> isNotNullPara;*/

    private final List<String> like;
    private final LinkedHashMap<String, Object> likePara;

    private final List<String> notLike;
    private final LinkedHashMap<String, Object> notLikePara;

    private final List<String> orderBy;
    private final LinkedHashMap<String, Object> orderByPara;

    private final List<String> not;
    private final LinkedHashMap<String, Object> notPara;

    private final List<String> in;
    private final LinkedHashMap<String, List> inPara;

    private final List<String> notIn;
    private final LinkedHashMap<String, List> notInPara;

    private final List<String> limit;
    private final LinkedHashMap<String, List> limitPara;

    private Params2(Bulider bulider) {
        this.and = bulider.and;
        this.andPara = bulider.andPara;
        this.or = bulider.or;
        this.orPara = bulider.orPara;
        this.between = bulider.between;
        this.betweenPara = bulider.betweenPara;
        this.lessThan = bulider.lessThan;
        this.lessThanPara = bulider.lessThanPara;
        this.moreThan = bulider.moreThan;
        this.moreThanPara = bulider.moreThanPara;
        /*this.isNull = bulider.isNull;
        this.isNullPara = bulider.isNullPara;
        this.isNotNull = bulider.isNotNull;
        this.isNotNullPara = bulider.isNotNullPara;*/
        this.like = bulider.like;
        this.likePara = bulider.likePara;
        this.notLike = bulider.notLike;
        this.notLikePara = bulider.notLikePara;
        this.orderBy = bulider.orderBy;
        this.orderByPara = bulider.orderByPara;
        this.not = bulider.not;
        this.notPara = bulider.notPara;
        this.in = bulider.in;
        this.inPara = bulider.inPara;
        this.notIn = bulider.notIn;
        this.notInPara = bulider.notInPara;
        this.limit = bulider.limit;
        this.limitPara = bulider.limitPara;
    }

    public static Params2.Bulider builder() {
        return new Params2.Bulider();
    }

    public static class Bulider {
        private List<String> and;
        private LinkedHashMap<String, Object> andPara;

        private List<String> or;
        private LinkedHashMap<String, Object> orPara;

        private List<String> between;
        private LinkedHashMap<String, List> betweenPara;

        private List<String> lessThan;
        private LinkedHashMap<String, Object> lessThanPara;

        private List<String> moreThan;
        private LinkedHashMap<String, Object> moreThanPara;

       /* private List<String> isNull;
        private LinkedHashMap<String ,Object> isNullPara;

        private List<String> isNotNull;
        private LinkedHashMap<String ,Object> isNotNullPara;*/

        private List<String> like;
        private LinkedHashMap<String, Object> likePara;

        private List<String> notLike;
        private LinkedHashMap<String, Object> notLikePara;

        private List<String> orderBy;
        private LinkedHashMap<String, Object> orderByPara;

        private List<String> not;
        private LinkedHashMap<String, Object> notPara;

        private List<String> in;
        private LinkedHashMap<String, List> inPara;

        private List<String> notIn;
        private LinkedHashMap<String, List> notInPara;

        private List<String> limit;
        private LinkedHashMap<String, List> limitPara;

        public Bulider Euqal(String key, Object value) {
            this.and = new ArrayList<>();
            this.andPara = new LinkedHashMap<>();
            this.and.add("=");
            this.andPara.put(key, value);
            return this;
        }

        public Bulider Or(String key, Object value) {
            this.or = new ArrayList<>();
            this.orPara = new LinkedHashMap<>();
            this.or.add("or");
            this.orPara.put(key, value);
            return this;
        }

        public Bulider Between(String key, Object value1, Object value2) {
            this.between = new ArrayList<>();
            this.betweenPara = new LinkedHashMap<>();
            List value = new ArrayList();
            value.add(value1);
            value.add(value2);
            this.between.add("between");
            this.betweenPara.put(key, value);
            return this;
        }

        public Bulider LessThan(String key, Object value) {
            this.lessThan = new ArrayList<>();
            this.lessThanPara = new LinkedHashMap<>();
            this.lessThan.add("<");
            this.lessThanPara.put(key, value);
            return this;
        }

        public Bulider MoreThan(String key, Object value) {
            this.moreThan = new ArrayList<>();
            this.moreThanPara = new LinkedHashMap<>();
            this.moreThan.add(">");
            this.moreThanPara.put(key, value);
            return this;
        }

        /*public Bulider IsNull(String key , Object value){
            this.isNull = new ArrayList<>();
            this.isNullPara = new LinkedHashMap<>();
            this.isNull.add("isnull");
            this.isNullPara.put(key,value);
            return this;
        }

        public Bulider IsNotNull(String key , Object value){
            this.isNotNull = new ArrayList<>();
            this.isNotNullPara = new LinkedHashMap<>();
            this.isNotNull.add("isnotnull");
            this.isNotNullPara.put(key,value);
            return this;
        }*/

        public Bulider Like(String key, Object value) {
            this.like = new ArrayList<>();
            this.likePara = new LinkedHashMap<>();
            this.like.add("like");
            this.likePara.put(key, value);
            return this;
        }

        public Bulider NotLike(String key, Object value) {
            this.notLike = new ArrayList<>();
            this.notLikePara = new LinkedHashMap<>();
            this.notLike.add("notlike");
            this.notLikePara.put(key, value);
            return this;
        }

        public Bulider OrderBy(String key, Object value) {
            this.orderBy = new ArrayList<>();
            this.orderByPara = new LinkedHashMap<>();
            this.orderBy.add("orderby");
            this.orderByPara.put(key, value);
            return this;
        }

        public Bulider NotEqual(String key, Object value) {
            this.not = new ArrayList<>();
            this.notPara = new LinkedHashMap<>();
            this.not.add("!=");
            this.notPara.put(key, value);
            return this;
        }

        public Bulider In(String key, Object... values) {
            this.in = new ArrayList<>();
            this.inPara = new LinkedHashMap<>();
            List list = new ArrayList();
            for (Object value : values) {
                list.add(value);
            }
            this.in.add("in");
            this.inPara.put(key, list);
            return this;
        }

        public Bulider NotIn(String key, Object... values) {
            this.notIn = new ArrayList<>();
            this.notInPara = new LinkedHashMap<>();
            List list = new ArrayList();
            for (Object value : values) {
                list.add(value);
            }
            this.notIn.add("notin");
            this.notInPara.put(key, list);
            return this;
        }

        public Bulider Limit(Integer value1, Integer value2) {
            this.limit = new ArrayList<>();
            this.limitPara = new LinkedHashMap<>();
            List<Integer> list = new ArrayList();
            list.add(value1);
            list.add(value2);
            this.limit.add("limit");
            this.limitPara.put("limit", list);
            return this;
        }

        public Params2 build() {
            return new Params2(this);
        }

    }

    public class PResult {
        private StringBuilder sql;
        private Map<String, Object> params;
        private List limit;

        public PResult(StringBuilder sql, Map<String, Object> params, List limit) {
            this.sql = sql;
            this.params = params;
            this.limit = limit;
        }

        public StringBuilder getSql() {
            return sql;
        }

        public void setSql(StringBuilder sql) {
            this.sql = sql;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        public List getLimit() {
            return limit;
        }

        public void setLimit(List limit) {
            this.limit = limit;
        }
    }

    public PResult getPResult() {
        Map<String, Object> param = new HashMap<>();
        List limits = null;
        StringBuilder sql = concatSqlForObj(param, this.and, this.andPara)
                .append(concatSqlForObj(param, this.not, this.notPara))
                .append(concatSqlForObj(param, this.or, this.orPara))
                .append(concatSqlForList(param, this.between, this.betweenPara))
                .append(concatSqlForObj(param, this.lessThan, this.lessThanPara))
                .append(concatSqlForObj(param, this.moreThan, this.moreThanPara))
                .append(concatSqlForObj(param, this.like, this.likePara))
                .append(concatSqlForObj(param, this.notLike, this.notLikePara))
                .append(concatSqlForList(param, this.in, this.inPara))
                .append(concatSqlForList(param, this.notIn, this.notInPara))
                .append(concatSqlForObj(param, this.orderBy, this.orderByPara));

        if (this.limitPara != null){
            limits = this.limitPara.get("limit");
        }
        return new PResult(sql,param,limits);
    }

    public StringBuilder concatSqlForObj(Map<String, Object> targetParam, List<String> symbols, Map<String, Object> sourceParam) {
        StringBuilder sql = new StringBuilder();
        if (symbols != null && symbols.size() != 0) {
            List<Map.Entry<String, Object>> indexedList = new ArrayList<>(sourceParam.entrySet());
            forEach(0, symbols, (i, symbol) -> {
                Map.Entry<String, Object> entry = indexedList.get(i);
                String key = entry.getKey();
                Object value = entry.getValue();
                if (symbol.equals("or")) {
                    sql.append(" or u.").append(key).append("=:").append(key);
                } else if (symbol.equals("<")) {
                    sql.append(" and u.").append(key).append("<:").append(key);
                } else if (symbol.equals(">")) {
                    sql.append(" and u.").append(key).append(">:").append(key);
                } else if (symbol.equals("like")) {
                    sql.append(" and u.").append(key).append(" like :").append(key);
                } else if (symbol.equals("notlike")) {
                    sql.append(" and u.").append(key).append(" not like :").append(key);
                } else if (symbol.equals("!=")) {
                    sql.append(" and u.").append(key).append(" != :").append(key);
                } else if (symbol.equals("orderby")) {
                    sql.append(" order by ").append(key).append(" ").append(value.toString());
                    return;
                } else {
                    sql.append(" and u." + key + " " + symbol + " :" + key);
                }
                targetParam.put(key, value);
            });
        }
        return sql;
    }

    public StringBuilder concatSqlForList(Map<String, Object> targetParam, List<String> symbols, Map<String, List> sourceParam) {
        StringBuilder sql = new StringBuilder();
        if (symbols != null && symbols.size() != 0) {
            List<Map.Entry<String, List>> indexedList = new ArrayList<>(sourceParam.entrySet());
            forEach(0, symbols, (i, symbol) -> {
                Map.Entry<String, List> entry = indexedList.get(i);
                String key = entry.getKey();
                List value = entry.getValue();
                if (symbol.equals("between")) {
                    sql.append(" and u.").append(key).append(" between :").append(key + "1").append(" and :").append(key + "2");
                } else if (symbol.equals("in") || symbol.equals("notin")) {
                    sql.append(" and u.").append(key);
                    sql.append(symbol.equals("in") ? " in (" : " not in (");
                    forEach(0, value, (idx, val) -> {
                        sql.append(":").append(key).append(",");
                        targetParam.put("key" + idx, value.get(i));
                    });
                    sql.setLength(sql.length() - 1);
                    sql.append(")");
                    return;
                }
                targetParam.put(key + "1", value.get(0));
                targetParam.put(key + "2", value.get(1));
            });
        }
        return sql;
    }

    /**
     * 遍历列表，可获取列表的index
     *
     * @param startIndex index初始值，一般为0(方便i+1取值)
     * @param elements   列表元素
     * @param action     处理方法
     * @param <T>
     */
    private <T> void forEach(Integer startIndex, Iterable<? extends T> elements, BiConsumer<Integer, ? super T> action) {
        Objects.requireNonNull(elements);
        Objects.requireNonNull(action);
        for (T element : elements) {
            action.accept(startIndex++, element);
        }
    }

}