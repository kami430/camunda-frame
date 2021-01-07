package com.flow.base.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.Consumer;

public class Param<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(Param.class);

    private EntityManager entityManager;
    /**
     * 要查询的模型对象
     */
    private Class<T> clazz;
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
    private List<Param> orParam;
    private String groupBy;

    private Map<String, Param> joinMap = new HashMap<>();
    private boolean isCountModel = false;

    private Param() {
    }

    private Param(Class clazz, EntityManager entityManager) {
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
     *
     * @param clazz
     * @param entityManager
     * @return
     */
    public static Param forClass(Class clazz, EntityManager entityManager) {
        return new Param(clazz, entityManager);
    }

    /**
     * 获取单个数据
     *
     * @return
     */
    public T findOne() {
        T t = null;
        try {
            Query query = entityManager.createQuery(build());
            t = (T) query.getSingleResult();
            entityManager.close();
        } catch (Exception e) {
            LOGGER.error("----------查询对象出错----------", e);
        }
        return t;
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<T> findList() {
        return findPage(null);
    }

    /**
     * 获取分页数据
     *
     * @param pageable
     * @return
     */
    public List<T> findPage(Pageable pageable) {
        List<T> t = new ArrayList<>();
        try {
            if (isNotNullOrder(pageable)) {
                pageable.getSort().get().forEach(order -> setOrder(null, null).addOrder(order.getProperty(), order.getDirection().name()));
            }
            Query query = entityManager.createQuery(build());
            if (pageable != null) {
                query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }
            t = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            LOGGER.error("----------查询列表出错----------", e);
        }
        return t;
    }

    public Long findCount() {
        Long count = 0L;
        try {
            Query query = entityManager.createQuery(buildCount());
            count = Long.valueOf(query.getSingleResult().toString());
            entityManager.close();
        } catch (Exception e) {
            LOGGER.error("----------查询数据总数出错----------", e);
        }
        return count;
    }

    /**
     * 创建join查询
     *
     * @param joinPropertyName 该类的join字段
     * @return
     */
    private Param<T> initJoinQuery(String joinPropertyName, JoinType joinType) {
        Param joinParam = new Param();
        Join join = this.getFrom().join(joinPropertyName, joinType);
        joinParam.clazz = join.getAttribute().getJavaType();
        joinParam.entityManager = this.getEntityManager();
        joinParam.criteriaBuilder = this.getCriteriaBuilder();
        joinParam.criteriaQuery = this.getCriteriaQuery();
        joinParam.from = join;
        joinParam.predicates = new ArrayList();
        joinParam.orders = new ArrayList();
        joinMap.put(joinPropertyName, joinParam);
        return joinParam;
    }

    public Param<T> innerJoin(String joinPropertyName, Consumer<Param> action) {
        Param joinParam = initJoinQuery(joinPropertyName, JoinType.INNER);
        action.accept(joinParam);
        this.getPredicates().addAll(joinParam.predicates);
        return this;
    }

    public Param<T> leftJoin(String joinPropertyName, Consumer<Param> action) {
        Param joinParam = initJoinQuery(joinPropertyName, JoinType.LEFT);
        action.accept(joinParam);
        this.getPredicates().addAll(joinParam.predicates);
        return this;
    }

    public Param<T> rightJoin(String joinPropertyName, Consumer<Param> action) {
        Param joinParam = initJoinQuery(joinPropertyName, JoinType.RIGHT);
        action.accept(joinParam);
        this.getPredicates().addAll(joinParam.predicates);
        return this;
    }

    /**
     * 等于
     */
    public Param<T> eq(String propertyName, Object value) {
        if (isNullOrEmpty(value))
            return this;
        this.predicates.add(criteriaBuilder.equal(from.get(propertyName), value));
        return this;
    }

    /**
     * 不相等
     */
    public Param<T> notEq(String propertyName, Object value) {
        if (isNullOrEmpty(value)) {
            return this;
        }
        this.predicates.add(criteriaBuilder.notEqual(from.get(propertyName), value));
        return this;
    }

    /**
     * 或
     */
    public Param<T> or(Map<String, Object> params) {
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
    public Param<T> like(String propertyName, String value) {
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
    public Param<T> orLike(List<String> propertyName, String value) {
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
    public Param<T> isNull(String propertyName) {
        this.predicates.add(criteriaBuilder.isNull(from.get(propertyName)));
        return this;
    }

    /**
     * 非空
     */
    public Param<T> isNotNull(String propertyName) {
        this.predicates.add(criteriaBuilder.isNotNull(from.get(propertyName)));
        return this;
    }

    /**
     * 在集合中
     */
    public Param<T> in(String propertyName, Collection value) {
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
    public Param<T> notIn(String propertyName, Collection value) {
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
    public Param<T> between(String propertyName, Date lo, Date go) {
        if (!isNullOrEmpty(lo) && !isNullOrEmpty(go)) {
            this.predicates.add(criteriaBuilder.between(from.get(propertyName), lo, go));
        }
        return this;
    }

    /**
     * 两者之间
     */
    public Param<T> between(String propertyName, Number lo, Number go) {
        if (!(isNullOrEmpty(lo)))
            ge(propertyName, lo);
        if (!(isNullOrEmpty(go)))
            le(propertyName, go);
        return this;
    }

    /**
     * 小于等于
     */
    public Param<T> le(String propertyName, Number value) {
        if (isNullOrEmpty(value)) return this;
        this.predicates.add(criteriaBuilder.le(from.get(propertyName), value));
        return this;
    }

    /**
     * 小于
     */
    public Param<T> lt(String propertyName, Number value) {
        if (isNullOrEmpty(value)) {
            return this;
        }
        this.predicates.add(criteriaBuilder.lt(from.get(propertyName), value));
        return this;
    }

    /**
     * 大于等于
     */
    public Param<T> ge(String propertyName, Number value) {
        if (isNullOrEmpty(value)) {
            return this;
        }
        this.predicates.add(criteriaBuilder.ge(from.get(propertyName), value));
        return this;
    }

    /**
     * 大于
     */
    public Param<T> gt(String propertyName, Number value) {
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
    private Param<T> join(Param joinParam) {
        this.getPredicates().addAll(joinParam.getPredicates());
        return this;
    }

    /**
     * 添加查询循环策略(hibernate只支持ROOT.fetch,不支持join.fetch)
     *
     * @param propertyName
     * @return
     */
    public Param<T> fetch(String propertyName, JoinType joinType) {
        this.getFrom().fetch(propertyName, joinType);
        return this;
    }

    /**
     * 添加排序
     *
     * @param propertyName 字段名
     * @param order        ("asc","desc")
     * @return
     */
    public Param<T> addOrder(String propertyName, String order) {
        if (order == null || propertyName == null)
            return this;
        String realProp = propertyName;
        Param joinParam = this;
        if (propertyName.contains(".")) {
            String[] orderParam = realProp.split("\\.");
            realProp = orderParam[orderParam.length - 1];
            for (int i = 0; i < orderParam.length - 1; i++) {
                joinParam = (Param) joinParam.joinMap.get(orderParam[i]);
            }
        }
        if (this.orders == null)
            this.orders = new ArrayList();
        if (order.equalsIgnoreCase("asc"))
            this.orders.add(joinParam.criteriaBuilder.asc(joinParam.from.get(realProp)));
        else if (order.equalsIgnoreCase("desc"))
            this.orders.add(joinParam.criteriaBuilder.desc(joinParam.from.get(realProp)));
        return this;
    }

    /**
     * 重置排序,并用新值排序
     *
     * @param propertyName 字段名
     * @param order        排序值("asc","desc")
     * @return
     */
    public Param<T> setOrder(String propertyName, String order) {
        this.orders = null;
        addOrder(propertyName, order);
        return this;
    }

    /**
     * 直接添加JPA内部的查询条件,用于应付一些复杂查询的情况,例如或
     */
    public Param<T> addCriterions(Predicate predicate) {
        this.predicates.add(predicate);
        return this;
    }

    /**
     * 生成查询
     *
     * @return
     */
    public CriteriaQuery<T> build() {
        this.criteriaQuery.select(this.from);
        this.isCountModel = false;
        return buildQuery();
    }

    /**
     * 生成记录总数查询
     *
     * @return
     */
    public CriteriaQuery buildCount() {
        this.setCountMode();
        return buildQuery();
    }

    /**
     * 生成查询
     *
     * @return
     */
    private CriteriaQuery buildQuery() {
        this.criteriaQuery.where(predicates.toArray(new Predicate[0]));
        if (!isNullOrEmpty(groupBy)) {
            criteriaQuery.groupBy(from.get(groupBy));
        }
        if (this.orders != null && !this.isCountModel) {
            criteriaQuery.orderBy(orders);
        } else {
            criteriaQuery.orderBy(new ArrayList<>());
        }
        return criteriaQuery;
    }

    /**
     * 设置查询记录总数模式
     *
     * @return
     */
    private Param setCountMode() {
        this.criteriaQuery.select(this.criteriaBuilder.count(this.from));
        this.isCountModel = true;
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

    public Param getJoinParam(String joinName) {
        return this.joinMap.get(joinName);
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

    /**
     * 判断分页排序是否为空
     *
     * @param pageable
     * @return
     */
    private boolean isNotNullOrder(Pageable pageable) {
        return pageable != null && pageable.getSort() != null && pageable.getSort().get().count() != 0;
    }
}