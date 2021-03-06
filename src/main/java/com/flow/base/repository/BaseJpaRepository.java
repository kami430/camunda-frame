package com.flow.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@NoRepositoryBean
public interface BaseJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    Boolean saveEntity(T entity);

    Boolean deleteEntity(T entity);

    Boolean deleteByKey(ID id);

    Boolean deleteByIds(List<ID> ids);

    Boolean update(T entity, ID id);

    T findByKey(ID id);

    List<T> findByField(String field, Object value);

    T findByExample(T entity);

    Page<T> findByExample(T entity, Pageable pageable);

    List<T> findByHql(String hql, Object... fields);

    List<T> findByHql(String hql, Map<String, Object> fields);

    List<T> findPageByHql(String hql, Map<String, Object> fields, Pageable pageable);

    List<Map> findBySql(String sql, Object... fields);

    List<Map> findBySql(String sql, Map<String, Object> fields);

    List<Map> findPageBySql(String sql, Map<String, Object> fields, Pageable pageable);

    T findOne(Map<String, Object> fields);

    T findOne(Param param);

    List<T> findByMoreField(Map<String, Object> fields);

    List<T> findByMoreField(Param param);

    List<T> findPageByMoreField(Map<String, Object> fields, Pageable pageable);

    List<T> findPageByMoreField(Param param, Pageable pageable);

    Long findCount(Map<String, Object> fields);

    Long findCount(Param param);

    Long findCount(String hql, Map<String, Object> fields, boolean isHql);

    Class<T> getClazz();

    EntityManager getEntityManager();

    String multiParamFormat(String sql, Consumer<BaseJpaRepositoryImpl.SqlFormat> action);

    Param<T> param();
}
