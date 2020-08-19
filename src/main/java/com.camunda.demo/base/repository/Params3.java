package com.camunda.demo.base.repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.Consumer;

@Deprecated
public class Params3 {

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
     * 或条件
     */
    private List<Params3> orParam;
    private String groupBy;

    private Params3() {
    }

    private Params3(Class clazz, EntityManager entityManager) {
        this.clazz = clazz;
        this.entityManager = entityManager;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.criteriaQuery = criteriaBuilder.createQuery(this.clazz);
        this.from = criteriaQuery.from(this.clazz);
        this.predicates = new ArrayList();
        this.orders = new ArrayList();
    }

    /**
     * 创建新查询
     * @param clazz
     * @param entityManager
     * @return
     */
    public static Params3 forClass(Class clazz, EntityManager entityManager) {
        return new Params3(clazz, entityManager);
    }

    /**
     * 创建join查询
     * @param joinPropertyName 该类的join字段
     * @return
     */
    private Params3 initJoinQuery(String joinPropertyName, JoinType joinType) {
        Params3 joinParam = new Params3();
        Join join = this.getFrom().join(joinPropertyName,joinType);
        joinParam.clazz = this.getClazz();
        joinParam.entityManager = this.getEntityManager();
        joinParam.criteriaBuilder = this.getCriteriaBuilder();
        joinParam.criteriaQuery = this.getCriteriaQuery();
        joinParam.from = join;
        joinParam.predicates = new ArrayList();
        joinParam.orders = new ArrayList();
        return joinParam;
    }

    public Params3 innerJoin(String joinPropertyName, Consumer<Params3> action){
        Params3 joinParam = initJoinQuery(joinPropertyName,JoinType.INNER);
        action.accept(joinParam);
        this.getPredicates().addAll(joinParam.predicates);
        return this;
    }

    public Params3 leftJoin(String joinPropertyName, Consumer<Params3> action){
        Params3 joinParam = initJoinQuery(joinPropertyName,JoinType.LEFT);
        action.accept(joinParam);
        this.getPredicates().addAll(joinParam.predicates);
        return this;
    }

    public Params3 rightJoin(String joinPropertyName, Consumer<Params3> action){
        Params3 joinParam = initJoinQuery(joinPropertyName,JoinType.RIGHT);
        action.accept(joinParam);
        this.getPredicates().addAll(joinParam.predicates);
        return this;
    }

    /**
     * 等于
     */
    public Params3 eq(String propertyName, Object value) {
        if (isNullOrEmpty(value))
            return this;
        this.predicates.add(criteriaBuilder.equal(from.get(propertyName), value));
        return this;
    }

    /**
     * 不相等
     */
    public Params3 notEq(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return this;
        }
        this.predicates.add(criteriaBuilder.notEqual(from.get(propertyName), value));
        return this;
    }

    /**
     * 或
     */
    public Params3 or(Map<String, Object> params) {
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
    public Params3 like(String propertyName, String value) {
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
    public Params3 orLike(List<String> propertyName, String value) {
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
    public Params3 isNull(String propertyName) {
        this.predicates.add(criteriaBuilder.isNull(from.get(propertyName)));
        return this;
    }

    /**
     * 非空
     */
    public Params3 isNotNull(String propertyName) {
        this.predicates.add(criteriaBuilder.isNotNull(from.get(propertyName)));
        return this;
    }

    /**
     * 在集合中
     */
    public Params3 in(String propertyName, Collection value) {
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
    public Params3 notIn(String propertyName, Collection value) {
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
    public Params3 between(String propertyName, Date lo, Date go) {
        if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
            this.predicates.add(criteriaBuilder.between(from.get(propertyName), lo, go));
        }
        return this;
    }

    /**
     * 两者之间
     */
    public Params3 between(String propertyName, Number lo, Number go) {
        if (!(isNullOrEmpty(lo)))
            ge(propertyName, lo);
        if (!(isNullOrEmpty(go)))
            le(propertyName, go);
        return this;
    }

    /**
     * 小于等于
     */
    public Params3 le(String propertyName, Number value) {
        if (isNullOrEmpty(value)) return this;
        this.predicates.add(criteriaBuilder.le(from.get(propertyName), value));
        return this;
    }

    /**
     * 小于
     */
    public Params3 lt(String propertyName, Number value) {
        if (isNullOrEmpty(value)) {
            return this;
        }
        this.predicates.add(criteriaBuilder.lt(from.get(propertyName), value));
        return this;
    }

    /**
     * 大于等于
     */
    public Params3 ge(String propertyName, Number value) {
        if (isNullOrEmpty(value)) {
            return this;
        }
        this.predicates.add(criteriaBuilder.ge(from.get(propertyName), value));
        return this;
    }

    /**
     * 大于
     */
    public Params3 gt(String propertyName, Number value) {
        if (isNullOrEmpty(value)) return this;
        this.predicates.add(criteriaBuilder.gt(from.get(propertyName), value));
        return this;
    }

    /**
     * 添加join查询
     *
     * @param joinParam
     * @return
     */
    private Params3 join(Params3 joinParam) {
        this.getPredicates().addAll(joinParam.getPredicates());
        return this;
    }

    /**
     * 添加查询循环策略(hibernate只支持ROOT.fetch,不支持join.fetch)
     *
     * @param propertyName
     * @return
     */
    public Params3 fetch(String propertyName, JoinType joinType) {
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
    public Params3 addOrder(String propertyName, String order) {
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
    public Params3 setOrder(String propertyName, String order) {
        this.orders = null;
        addOrder(propertyName, order);
        return this;
    }

    /**
     * 直接添加JPA内部的查询条件,用于应付一些复杂查询的情况,例如或
     */
    public Params3 addCriterions(Predicate predicate) {
        this.predicates.add(predicate);
        return this;
    }

    /**
     * 生成查询
     * @return
     */
    public CriteriaQuery build() {
        this.criteriaQuery.select(this.from);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        if (!isNullOrEmpty(groupBy)) {
            criteriaQuery.groupBy(from.get(groupBy));
        }
        if (this.orders != null) {
            criteriaQuery.orderBy(orders);
        }
        return criteriaQuery;
    }

    /**
     * 生成记录总数查询
     * @return
     */
    public CriteriaQuery buildCount() {
        this.setCountMode();
        this.criteriaQuery.where(predicates.toArray(new Predicate[0]));
        if (!isNullOrEmpty(groupBy)) {
            criteriaQuery.groupBy(from.get(groupBy));
        }
        if (this.orders != null) {
            criteriaQuery.orderBy(orders);
        }
        return criteriaQuery;
    }

    /**
     * 设置查询记录总数模式
     * @return
     */
    private Params3 setCountMode() {
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