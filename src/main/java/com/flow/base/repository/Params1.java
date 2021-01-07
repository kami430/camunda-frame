package com.flow.base.repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * 旧版params已废弃
 */
@Deprecated
public class Params1 {

    /**-----------------------------外部函数 - 数据库 -------------------------------*/

    /**
     * 输出查询对象
     *
     * @param clazz
     * @param entityManager
     * @return
     */
    public CriteriaQuery build(Class clazz, EntityManager entityManager) {
        return this.buildQuery(clazz, entityManager).newCriteriaQuery();
    }

    /**
     * 输出查询对象条数
     *
     * @param clazz
     * @param entityManager
     * @return
     */
    public CriteriaQuery buildCount(Class clazz, EntityManager entityManager) {
        return this.buildQuery(clazz, entityManager).setCountMode().newCriteriaQuery();
    }

    /**
     * 创建params
     *
     * @return
     */
    public static Params1 create() {
        return new Params1();
    }

    /**
     * ------------------------------ 外部函数 - 条件组装 -----------------------------------
     */

    private Params1() {
    }

    private enum Action {
        EQ, NOTEQ, OR, LIKE, ORLIKE, ISNULL, ISNOTNULL, IN, NOTIN, BETWEENDATE, BETWEENNUM, LE, GE, LT, GT,
        ORDER, SETORDER, GROUP,
        ADDQUERY, JOIN, FETCH
    }

    private List<Action> action = new LinkedList<>();

    private Stack<String> eq = new Stack<>();
    private Stack<Object> eqPara = new Stack<>();

    private Stack<String> noteq = new Stack<>();
    private Stack<Object> noteqPara = new Stack<>();

    private Stack<Map<String, Object>> or = new Stack<>();

    private Stack<String> like = new Stack<>();
    private Stack<String> likePara = new Stack<>();

    private Stack<List<String>> orLike = new Stack<>();
    private Stack<String> orLikePara = new Stack<>();

    private Stack<String> isNull = new Stack<>();

    private Stack<String> isNotNull = new Stack<>();

    private Stack<String> in = new Stack<>();
    private Stack<Collection> inPara = new Stack<>();

    private Stack<String> notIn = new Stack<>();
    private Stack<Collection> notInPara = new Stack<>();

    private Stack<String> betweenDate = new Stack<>();
    private Stack<Date> betweenDatePara1 = new Stack<>();
    private Stack<Date> betweenDatePara2 = new Stack<>();

    private Stack<String> betweenNumber = new Stack<>();
    private Stack<Number> betweenNumberPara1 = new Stack<>();
    private Stack<Number> betweenNumberPara2 = new Stack<>();

    private Stack<String> le = new Stack<>();
    private Stack<Number> lePara = new Stack<>();

    private Stack<String> ge = new Stack<>();
    private Stack<Number> gePara = new Stack<>();

    private Stack<String> lt = new Stack<>();
    private Stack<Number> ltPara = new Stack<>();

    private Stack<String> gt = new Stack<>();
    private Stack<Number> gtPara = new Stack<>();

    private Stack<String> order = new Stack<>();
    private Stack<String> orderPara = new Stack<>();

    private Stack<String> setOrder = new Stack<>();
    private Stack<String> setOrderPara = new Stack<>();

    private Stack<String> group = new Stack<>();

    private Stack<Predicate> query = new Stack<>();

    private Stack<String> joinQuery = new Stack<>();
    private Stack<JoinType> joinTypes = new Stack<>();
    private Stack<Params1> joinQueryPara = new Stack<>();

    private Stack<String> fetchQuery = new Stack<>();
    private Stack<JoinType> fetchTypes = new Stack<>();
    private Stack<Params1> fetchQueryPara = new Stack<>();


    public Params1 eq(String propertyName, Object value) {
        action.add(Action.EQ);
        this.eq.add(propertyName);
        this.eqPara.add(value);
        return this;
    }

    public Params1 notEq(String propertyName, Object value) {
        action.add(Action.NOTEQ);
        this.noteq.add(propertyName);
        this.noteqPara.add(value);
        return this;
    }

    public Params1 or(Map<String, Object> params) {
        action.add(Action.OR);
        this.or.add(params);
        return this;
    }

    public Params1 like(String propertyName, String value) {
        action.add(Action.LIKE);
        this.like.add(propertyName);
        this.likePara.add(value);
        return this;
    }

    public Params1 orLike(List<String> propertyName, String value) {
        action.add(Action.ORLIKE);
        this.orLike.add(propertyName);
        this.orLikePara.add(value);
        return this;
    }

    public Params1 isNull(String propertyName) {
        action.add(Action.ISNULL);
        this.isNull.add(propertyName);
        return this;
    }

    public Params1 isNotNull(String propertyName) {
        action.add(Action.ISNOTNULL);
        this.isNotNull.add(propertyName);
        return this;
    }

    public Params1 in(String propertyName, Collection value) {
        action.add(Action.IN);
        this.in.add(propertyName);
        this.inPara.add(value);
        return this;
    }

    public Params1 notIn(String propertyName, Collection value) {
        action.add(Action.NOTIN);
        this.notIn.add(propertyName);
        this.notInPara.add(value);
        return this;
    }

    public Params1 between(String propertyName, Date lo, Date go) {
        action.add(Action.BETWEENDATE);
        this.betweenDate.add(propertyName);
        this.betweenDatePara1.add(lo);
        this.betweenDatePara2.add(go);
        return this;
    }

    public Params1 between(String propertyName, Number lo, Number go) {
        action.add(Action.BETWEENNUM);
        this.betweenNumber.add(propertyName);
        this.betweenNumberPara1.add(lo);
        this.betweenNumberPara2.add(go);
        return this;
    }

    public Params1 lt(String propertyName, Number value) {
        action.add(Action.LT);
        this.lt.add(propertyName);
        this.ltPara.add(value);
        return this;
    }

    public Params1 gt(String propertyName, Number value) {
        action.add(Action.GT);
        this.gt.add(propertyName);
        this.gtPara.add(value);
        return this;
    }

    public Params1 le(String propertyName, Number value) {
        action.add(Action.LE);
        this.le.add(propertyName);
        this.lePara.add(value);
        return this;
    }

    public Params1 ge(String propertyName, Number value) {
        action.add(Action.GE);
        this.ge.add(propertyName);
        this.gePara.add(value);
        return this;
    }

    public Params1 orderAsc(String propertyName) {
        action.add(Action.ORDER);
        this.order.add(propertyName);
        this.orderPara.add("asc");
        return this;
    }

    public Params1 orderDesc(String propertyName) {
        action.add(Action.ORDER);
        this.order.add(propertyName);
        this.orderPara.add("desc");
        return this;
    }

    public Params1 setOrder(String propertyName, String value) {
        action.add(Action.SETORDER);
        this.setOrder.add(propertyName);
        this.setOrderPara.add(value);
        return this;
    }

    public Params1 group(String propertyName) {
        action.add(Action.GROUP);
        this.group.add(propertyName);
        return this;
    }

    public Params1 addQuery(Predicate predicate) {
        action.add(Action.ADDQUERY);
        this.query.add(predicate);
        return this;
    }

    public Params1 innerJoin(String propertyName, Params1 params1) {
        action.add(Action.JOIN);
        this.joinQuery.add(propertyName);
        this.joinTypes.add(JoinType.INNER);
        if (params1 == null) params1 = Params1.create();
        this.joinQueryPara.add(params1);
        return this;
    }

    public Params1 leftJoin(String propertyName, Params1 params1) {
        action.add(Action.JOIN);
        this.joinQuery.add(propertyName);
        this.joinTypes.add(JoinType.LEFT);
        if (params1 == null) params1 = Params1.create();
        this.joinQueryPara.add(params1);
        return this;
    }

    public Params1 rightJoin(String propertyName, Params1 params1) {
        action.add(Action.JOIN);
        this.joinQuery.add(propertyName);
        this.joinTypes.add(JoinType.RIGHT);
        if (params1 == null) params1 = Params1.create();
        this.joinQueryPara.add(params1);
        return this;
    }

    public Params1 innerFetch(String propertyName) {
        action.add(Action.FETCH);
        this.fetchQuery.add(propertyName);
        this.fetchTypes.add(JoinType.INNER);
        return this;
    }

    public Params1 leftFetch(String propertyName) {
        action.add(Action.FETCH);
        this.fetchQuery.add(propertyName);
        this.fetchTypes.add(JoinType.LEFT);
        return this;
    }

    public Params1 rightFetch(String propertyName) {
        action.add(Action.FETCH);
        this.fetchQuery.add(propertyName);
        this.fetchTypes.add(JoinType.RIGHT);
        return this;
    }

    /**
     * -------------------------外部函数 - 条件组装辅助----------------------------
     */

    public static class OrMap extends LinkedHashMap<String, Object> {
        @Override
        public OrMap put(String param, Object value) {
            super.put(param, value);
            return this;
        }

        public static OrMap creat() {
            return new OrMap();
        }
    }

    /**
     * -------------------------内部函数 - 内部处理-----------------------------
     */

    private Query buildQuery(Class clazz, EntityManager entityManager) {
        Query query = Query.forClass(clazz, entityManager);
        return buildQuery(query);
    }

    private Query buildQuery(Query query) {
        action.forEach(action -> {
            switch (action) {
                case EQ:
                    query.eq(this.eq.pop(), this.eqPara.pop());
                    break;
                case NOTEQ:
                    query.notEq(this.noteq.pop(), this.noteqPara.pop());
                    break;
                case OR:
                    query.or(this.or.pop());
                    break;
                case LIKE:
                    query.like(this.like.pop(), this.likePara.pop());
                    break;
                case ORLIKE:
                    query.orLike(this.orLike.pop(), this.orLikePara.pop());
                    break;
                case ISNULL:
                    query.isNull(this.isNull.pop());
                    break;
                case ISNOTNULL:
                    query.isNotNull(this.isNotNull.pop());
                    break;
                case IN:
                    query.in(this.in.pop(), this.inPara.pop());
                    break;
                case NOTIN:
                    query.notIn(this.notIn.pop(), this.notInPara.pop());
                    break;
                case BETWEENDATE:
                    query.between(this.betweenDate.pop(), this.betweenDatePara1.pop(), this.betweenDatePara2.pop());
                    break;
                case BETWEENNUM:
                    query.between(this.betweenNumber.pop(), this.betweenNumberPara1.pop(), this.betweenNumberPara2.pop());
                    break;
                case LE:
                    query.le(this.le.pop(), this.lePara.pop());
                    break;
                case GE:
                    query.ge(this.ge.pop(), this.gePara.pop());
                    break;
                case LT:
                    query.lt(this.lt.pop(), this.ltPara.pop());
                    break;
                case GT:
                    query.gt(this.gt.pop(), this.gtPara.pop());
                    break;
                case ORDER:
                    query.addOrder(this.order.pop(), this.orderPara.pop());
                    break;
                case SETORDER:
                    query.setOrder(this.setOrder.pop(), this.setOrderPara.pop());
                    break;
                case GROUP:
                    query.setGroupBy(this.group.pop());
                    break;
                case ADDQUERY:
                    query.addCriterions(this.query.pop());
                    break;
                case JOIN:
                    query.addJoinQuery(buildJoinQuery(this.joinQuery.pop(), this.joinTypes.pop(), query, this.joinQueryPara.pop()));
                    break;
                case FETCH:
                    query.fetch(this.fetchQuery.pop(),this.fetchTypes.pop());
            }
        });
        return query;
    }

    private Query buildJoinQuery(String propertyName, JoinType joinType, Query query, Params1 params1) {
        Join join = query.getFrom().join(propertyName, joinType);
        Query joinQuery = Query.forJoinQuery(query, join);
        joinQuery = params1.buildQuery(joinQuery);
        return joinQuery;
    }


    /**--------------------------Param - 核心查询组件 -------------------------*/

    /**
     * 通过类创建查询条件
     */
    private static class Query {
        private EntityManager entityManager;
        /**
         * 要查询的模型对象
         */
        private Class clazz;
        /**
         * 查询条件列表
         */
        private From from;
        private List<Predicate> predicates;
        private CriteriaQuery criteriaQuery;
        private CriteriaBuilder criteriaBuilder;
        /**
         * 排序方式列表
         */
        private List<Order> orders;
        /**
         * 关联模式
         */
        /**
         * 或条件
         */
        private List<Query> orQuery;
        private String groupBy;

        private Query() {
        }

        private Query(Class clazz, EntityManager entityManager) {
            this.clazz = clazz;
            this.entityManager = entityManager;
            this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
            this.criteriaQuery = criteriaBuilder.createQuery(this.clazz);
            this.from = criteriaQuery.from(this.clazz);
            this.predicates = new ArrayList();
            this.orders = new ArrayList();
        }

        public static Query forClass(Class clazz, EntityManager entityManager) {
            return new Query(clazz, entityManager);
        }

        public static Query forJoinQuery(Query sourceQuery, Join join) {
            Query targetQuery = new Query();
            targetQuery.clazz = sourceQuery.getClazz();
            targetQuery.entityManager = sourceQuery.getEntityManager();
            targetQuery.criteriaBuilder = sourceQuery.getCriteriaBuilder();
            targetQuery.criteriaQuery = sourceQuery.getCriteriaQuery();
            targetQuery.from = join;
            targetQuery.predicates = new ArrayList();
            targetQuery.orders = new ArrayList();
            return targetQuery;
        }

        /**
         * 等于
         */
        public Query eq(String propertyName, Object value) {
            if (isNullOrEmpty(value))
                return this;
            this.predicates.add(criteriaBuilder.equal(from.get(propertyName), value));
            return this;
        }

        /**
         * 不相等
         */
        public Query notEq(String propertyName, Object value) {
            if (isNullOrEmpty(value)) {
                return this;
            }
            this.predicates.add(criteriaBuilder.notEqual(from.get(propertyName), value));
            return this;
        }

        /**
         * 或
         */
        public Query or(Map<String, Object> params) {
            if ((params == null) || (params.size() == 0))
                return this;
            Iterator<String> keys = params.keySet().iterator();
            String fKey = keys.next();
            Predicate predicate = criteriaBuilder.or(criteriaBuilder.equal(from.get(fKey), params.get(fKey)));
            while (keys.hasNext()) {
                String key = keys.next();
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(from.get(key), params.get(key)));
            }
            this.predicates.add(predicate);
            return this;
        }

        /**
         * 模糊查询
         */
        public Query like(String propertyName, String value) {
            if (isNullOrEmpty(value))
                return this;
            if (value.indexOf("%") < 0)
                value = "%" + value + "%";
            this.predicates.add(criteriaBuilder.like(from.get(propertyName), value));
            return this;
        }

        /**
         * 或（模糊查询）
         */
        public Query orLike(List<String> propertyName, String value) {
            if (isNullOrEmpty(value) || (propertyName.size() == 0))
                return this;
            if (value.indexOf("%") < 0)
                value = "%" + value + "%";
            Predicate predicate = criteriaBuilder.or(criteriaBuilder.like(from.get(propertyName.get(0)), value.toString()));
            for (int i = 1; i < propertyName.size(); ++i)
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.like(from.get(propertyName.get(i)), value));
            this.predicates.add(predicate);
            return this;
        }

        /**
         * 空
         */
        public Query isNull(String propertyName) {
            this.predicates.add(criteriaBuilder.isNull(from.get(propertyName)));
            return this;
        }

        /**
         * 非空
         */
        public Query isNotNull(String propertyName) {
            this.predicates.add(criteriaBuilder.isNotNull(from.get(propertyName)));
            return this;
        }

        /**
         * 在集合中
         */
        public Query in(String propertyName, Collection value) {
            if ((value == null) || (value.size() == 0)) {
                return this;
            }
            Iterator iterator = value.iterator();
            CriteriaBuilder.In in = criteriaBuilder.in(from.get(propertyName));
            while (iterator.hasNext()) {
                in.value(iterator.next());
            }
            this.predicates.add(in);
            return this;
        }

        /**
         * 不在集合
         */
        public Query notIn(String propertyName, Collection value) {
            if ((value == null) || (value.size() == 0)) {
                return this;
            }
            Iterator iterator = value.iterator();
            CriteriaBuilder.In in = criteriaBuilder.in(from.get(propertyName));
            while (iterator.hasNext()) {
                in.value(iterator.next());
            }
            this.predicates.add(criteriaBuilder.not(in));
            return this;
        }

        /**
         * 两者之间
         */
        public Query between(String propertyName, Date lo, Date go) {
            if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
                this.predicates.add(criteriaBuilder.between(from.get(propertyName), lo, go));
            }
            return this;
        }

        /**
         * 两者之间
         */
        public Query between(String propertyName, Number lo, Number go) {
            if (!(isNullOrEmpty(lo)))
                ge(propertyName, lo);
            if (!(isNullOrEmpty(go)))
                le(propertyName, go);
            return this;
        }

        /**
         * 小于等于
         */
        public Query le(String propertyName, Number value) {
            if (isNullOrEmpty(value)) return this;
            this.predicates.add(criteriaBuilder.le(from.get(propertyName), value));
            return this;
        }

        /**
         * 小于
         */
        public Query lt(String propertyName, Number value) {
            if (isNullOrEmpty(value)) {
                return this;
            }
            this.predicates.add(criteriaBuilder.lt(from.get(propertyName), value));
            return this;
        }

        /**
         * 大于等于
         */
        public Query ge(String propertyName, Number value) {
            if (isNullOrEmpty(value)) {
                return this;
            }
            this.predicates.add(criteriaBuilder.ge(from.get(propertyName), value));
            return this;
        }

        /**
         * 大于
         */
        public Query gt(String propertyName, Number value) {
            if (isNullOrEmpty(value)) return this;
            this.predicates.add(criteriaBuilder.gt(from.get(propertyName), value));
            return this;
        }

        /**
         * 直接添加JPA内部的查询条件,用于应付一些复杂查询的情况,例如或
         */
        public Query addCriterions(Predicate predicate) {
            this.predicates.add(predicate);
            return this;
        }

        /**
         * 添加join查询
         *
         * @param joinQuery
         * @return
         */
        public Query addJoinQuery(Query joinQuery) {
            this.getPredicates().addAll(joinQuery.getPredicates());
            return this;
        }

        /**
         * 添加查询循环策略(hibernate只支持ROOT.fetch,不支持join.fetch)
         *
         * @param propertyName
         * @return
         */
        public Query fetch(String propertyName,JoinType joinType) {
            this.getFrom().fetch(propertyName,joinType);
            return this;
        }

        /**
         * 添加排序
         *
         * @param propertyName 字段名
         * @param order        ("asc","desc")
         * @return
         */
        public Query addOrder(String propertyName, String order) {
            if (order == null || propertyName == null)
                return this;
            if (this.orders == null)
                this.orders = new ArrayList();
            if (order.equalsIgnoreCase("asc"))
                this.orders.add(criteriaBuilder.asc(from.get(propertyName)));
            else if (order.equalsIgnoreCase("desc"))
                this.orders.add(criteriaBuilder.desc(from.get(propertyName)));
            return this;
        }

        /**
         * 重置排序,并用新值排序
         *
         * @param propertyName 字段名
         * @param order        排序值("asc","desc")
         * @return
         */
        public Query setOrder(String propertyName, String order) {
            this.orders = null;
            addOrder(propertyName, order);
            return this;
        }

        public CriteriaQuery newCriteriaQuery() {
            criteriaQuery.where(predicates.toArray(new Predicate[0]));
            if (!isNullOrEmpty(groupBy)) {
                criteriaQuery.groupBy(from.get(groupBy));
            }
            if (this.orders != null) {
                criteriaQuery.orderBy(orders);
            }
            return criteriaQuery;
        }

        public Query setCountMode() {
            this.criteriaQuery.select(this.criteriaBuilder.count(this.from));
            return this;
        }

        public Class getModleClass() {
            return this.clazz;
        }

        public Class getClazz() {
            return this.clazz;
        }

        public List<Order> getOrders() {
            return orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
        }

        public EntityManager getEntityManager() {
            return this.entityManager;
        }

        public void setEntityManager(EntityManager em) {
            this.entityManager = em;
        }

        public From getFrom() {
            return this.from;
        }

        public List<Predicate> getPredicates() {
            return predicates;
        }

        public void setPredicates(List<Predicate> predicates) {
            this.predicates = predicates;
        }

        public CriteriaQuery getCriteriaQuery() {
            return criteriaQuery;
        }

        public CriteriaBuilder getCriteriaBuilder() {
            return criteriaBuilder;
        }

        public void setFetchModes(List<String> fetchField, List<String> fetchMode) {
        }

        public String getGroupBy() {
            return groupBy;
        }

        public void setGroupBy(String groupBy) {
            this.groupBy = groupBy;
        }

        /**
         * 排除空值
         */
        private boolean isNullOrEmpty(Object value) {
            if (value instanceof String) {
                return value == null || "".equals(value);
            }
            return value == null;
        }
    }
}
