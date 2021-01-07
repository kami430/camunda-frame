package com.flow.base.repository;

import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BaseJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseJpaRepository<T, ID> {

    private Logger logger = LoggerFactory.getLogger(BaseJpaRepository.class);

    private final EntityManager entityManager;
    private final JpaEntityInformation jpaEntityInformation;
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public BaseJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.jpaEntityInformation = entityInformation;
        this.clazz = entityInformation.getJavaType();
    }

    @Override
    public Param<T> param() {
        return Param.forClass(this.clazz, this.entityManager);
    }

    @Override
    @Transactional
    public Boolean saveEntity(T entity) {
        try {
            entityManager.persist(entity);
            return true;
        } catch (Exception e) {
            logger.error("----------保存出错----------", e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean deleteEntity(T entity) {
        try {
            entityManager.remove(entity);
            return true;
        } catch (Exception e) {
            logger.error("----------删除出错----------", e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean deleteByKey(ID id) {
        try {
            String tablename = clazz.getName();
            String idField = getIdField(clazz);
            if (null == idField) return false;
            String sql = "DELETE FROM " + tablename + " u WHERE u." + idField + "=?1";
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, id);
            int i = query.executeUpdate();
            entityManager.close();
            return i != 0;
        } catch (Exception e) {
            logger.error("----------删除出错----------", e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<ID> ids) {
        try {
            String tablename = clazz.getName();
            String idField = getIdField(clazz);
            String sql = "DELETE FROM " + tablename + " u WHERE u." + idField + "in(";
            StringBuilder fieldBuilder = new StringBuilder();
            forEach(1, ids, (index, id) -> fieldBuilder.append("?").append(index).append(","));
            fieldBuilder.setLength(fieldBuilder.length() - 1);
            fieldBuilder.append(")");
            Query query = entityManager.createQuery(sql + fieldBuilder.toString());
            forEach(1, ids, (index, id) -> query.setParameter(index, id));
            int i = query.executeUpdate();
            entityManager.close();
            return i != 0;
        } catch (Exception e) {
            logger.error("----------批量删除出错----------", e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean update(T entity, ID id) {
        try {
            Object o = entityManager.find(entity.getClass(), id);
            copyPropertiesExcludeNull(entity, o);
            entityManager.merge(o);
            return true;
        } catch (Exception e) {
            logger.error("----------更新出错----------", e);
            return false;
        }
    }

    @Override
    public T findByKey(ID id) {
        try {
            T t = entityManager.find(clazz, id);
            return t;
        } catch (Exception e) {
            logger.error("----------查询出错----------", e);
            return null;
        }
    }

    @Override
    public List<T> findByField(String field, Object value) {
        String tablename = clazz.getName();
        List<T> list = new ArrayList<>();
        try {
            String sql = "from " + tablename + " u WHERE u." + field + "=?1";
            Query query = entityManager.createQuery(sql);
            query.setParameter(1, value);
            list = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public T findByExample(T entity) {
        Example<T> example = Example.of(entity);
        return findOne(example).orElse(null);
    }

    @Override
    public Page<T> findByExample(T entity, Pageable pageable) {
        Example example = Example.of(entity);
        return findAll(example, pageable);
    }

    @Override
    public List<T> findByHql(String hql, Object... fields) {
        List<T> list = new ArrayList<>();
        try {
            Query query = entityManager.createQuery(hql);
            for (int i = 0; i < fields.length; i++) query.setParameter(i + 1, fields[i]);
            list = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public List<T> findByHql(String hql, Map<String, Object> fields) {
        return findPageByHql(hql, fields, null);
    }

    @Override
    public List<T> findPageByHql(String hql, Map<String, Object> fields, Pageable pageable) {
        List<T> list = new ArrayList<>();
        try {
            hql += isNotNullOrder(pageable) ? orderString(pageable) : "";
            Query query = entityManager.createQuery(hql);
            if (fields != null) fields.forEach((key, field) -> query.setParameter(key, field));
            if (pageable != null) {
                query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public List<Map> findBySql(String sql, Object... fields) {
        List<Map> list = new ArrayList<>();
        try {
            Query query = entityManager.createNativeQuery(sql);
            for (int i = 0; i < fields.length; i++) query.setParameter(i + 1, fields[i]);
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            list = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public List<Map> findBySql(String sql, Map<String, Object> fields) {
        return findPageBySql(sql, fields, null);
    }

    @Override
    public List<Map> findPageBySql(String sql, Map<String, Object> fields, Pageable pageable) {
        List<Map> list = new ArrayList<>();
        try {
            sql += isNotNullOrder(pageable) ? orderString(pageable) : "";
            Query query = entityManager.createNativeQuery(sql);
            if (fields != null) fields.forEach((key, field) -> query.setParameter(key, field));
            query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            if (pageable != null) {
                query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public T findOne(Map<String, Object> fields) {
        T t = null;
        try {
            String sql = "from " + clazz.getName() + " u WHERE 1=1";
            StringBuilder fieldBuilder = new StringBuilder();
            if (fields != null) fields.forEach((field, value) -> fieldBuilder.append(" AND u." + field + "=:" + field));
            Query query = entityManager.createQuery(sql + fieldBuilder.toString());
            if (fields != null) fields.forEach((field, value) -> query.setParameter(field, value));
            t = (T) query.getSingleResult();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return t;
    }

    @Override
    public T findOne(Param param) {
        T t = null;
        try {
            Query query = entityManager.createQuery(param.build());
            t = (T) query.getSingleResult();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return t;
    }

    @Override
    public List<T> findByMoreField(Map<String, Object> fields) {
        return findPageByMoreField(fields, null);
    }

    @Override
    public List<T> findByMoreField(Param param) {
        return findPageByMoreField(param, null);
    }

    @Override
    public List<T> findPageByMoreField(Map<String, Object> fields, Pageable pageable) {
        List<T> list = new ArrayList<>();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery(clazz);
            Root iRoot = cq.from(clazz);
            List<Predicate> predicates = new ArrayList<>();
            fields.forEach((key, val) -> {
                predicates.add(cb.equal(iRoot.get(key), val));
            });
            if (isNotNullOrder(pageable)) {
                List<Order> orders = pageable.getSort().get().map(order -> {
                    if (order.getDirection().equals(Sort.Direction.DESC))
                        return cb.desc(iRoot.get(order.getProperty()));
                    else return cb.asc(iRoot.get(order.getProperty()));
                }).collect(Collectors.toList());
                cq.orderBy(orders);
            }
            cq.where(predicates.toArray(new Predicate[0]));
            Query query = entityManager.createQuery(cq);
            if (pageable != null) {
                query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public List<T> findPageByMoreField(Param param, Pageable pageable) {
        List<T> list = new ArrayList<>();
        try {
            if (isNotNullOrder(pageable)) {
                pageable.getSort().get().forEach(order -> param.setOrder(null, null).addOrder(order.getProperty(), order.getDirection().name()));
            }
            Query query = entityManager.createQuery(param.build());
            if (pageable != null) {
                query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
                query.setMaxResults(pageable.getPageSize());
            }
            list = query.getResultList();
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return list;
    }

    @Override
    public Long findCount(Map<String, Object> fields) {
        String tablename = clazz.getName();
        Long count = 0L;
        try {
            String sql = "SELECT COUNT(1) FROM " + tablename + " u WHERE 1=1";
            StringBuilder fieldBuilder = new StringBuilder();
            fields.forEach((field, value) -> fieldBuilder.append(" AND u." + field + "=:" + field));
            Query query = entityManager.createQuery(sql + fieldBuilder.toString());
            fields.forEach((field, value) -> query.setParameter(field, value));
            count = Long.valueOf(query.getSingleResult().toString());
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return count;
    }

    @Override
    public Long findCount(Param param) {
        Long count = 0L;
        try {
            Query query = entityManager.createQuery(param.buildCount());
            count = Long.valueOf(query.getSingleResult().toString());
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询列表出错----------", e);
        }
        return count;
    }

    @Override
    public Long findCount(String hql, Map<String, Object> fields, boolean isHql) {
        Long count = 0L;
        Query query;
        try {
            String countSql = getCountSql(hql, isHql);
            query = isHql ? entityManager.createQuery(countSql) : entityManager.createNativeQuery(countSql);
            for (String field : fields.keySet()) query.setParameter(field, fields.get(field));
            count = Long.valueOf(query.getSingleResult().toString());
            entityManager.close();
        } catch (Exception e) {
            logger.error("----------查询数量出错----------", e);
        }
        return count;
    }

    @Override
    public Class<T> getClazz() {
        return this.clazz;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * 获取类的@Id字段名
     *
     * @param clazz 类
     * @return
     */
    private String getIdField(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Id.class)) {
                    return field.getName();
                }
            }
        }
        return null;
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

    /**
     * 获取源对象空值字段
     *
     * @param source 源对象
     * @return
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
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

    /**
     * 获取排序字符串
     *
     * @param pageable
     * @return
     */
    private String orderString(Pageable pageable) {
        if (pageable == null || pageable.getSort() == null || pageable.getSort().get().count() == 0L) return "";
        StringBuilder builder = new StringBuilder(" ORDER BY ");
        List<String> orders = new ArrayList<>();
        pageable.getSort().get().forEach(order -> {
            if (order.getDirection().equals(Sort.Direction.DESC)) {
                orders.add(order.getProperty() + " DESC");
            } else {
                orders.add(order.getProperty());
            }
        });
        builder.append(String.join(",", orders));
        return builder.toString();
    }

    /**
     * 获取sql/hql语句的count语句
     *
     * @param sql   传入的sql
     * @param isHql 是否hql
     * @return
     */
    public static String getCountSql(String sql, boolean isHql) {
        if (isHql) {
            String regexStart = "^(?i)from\\s{1}| (?i)from ";
            String[] rs = sql.split(regexStart, 2);
            if (rs.length == 2) return "SELECT COUNT(*) FROM " + rs[1];
        }
        return "SELECT COUNT(*) FROM (" + sql + ") t";
    }

    /**
     * 复制源对象到目标对象中（排除空值）
     *
     * @param source 源对象
     * @param target 目标对象
     * @throws BeansException
     */
    private void copyPropertiesExcludeNull(Object source, Object target) throws BeansException {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
}
