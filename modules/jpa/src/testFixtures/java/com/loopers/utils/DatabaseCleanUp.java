package com.loopers.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseCleanUp implements InitializingBean {

    private final List<String> tableNames = new ArrayList<>();
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void afterPropertiesSet() {
        entityManager.getMetamodel().getEntities().stream()
                .map(Type::getJavaType)
                .map(this::resolveTableNameSafely)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .forEach(tableNames::add);
    }

    private String resolveTableNameSafely(Class<?> type) {
        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            Table t = c.getAnnotation(Table.class);
            if (t != null && t.name() != null && !t.name().isBlank()) {
                return t.name();
            }
        }
        return null;
    }

    @Transactional
    public void truncateAllTables() {
        entityManager.flush();
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

        for (String table : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE `" + table + "`").executeUpdate();
        }

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
