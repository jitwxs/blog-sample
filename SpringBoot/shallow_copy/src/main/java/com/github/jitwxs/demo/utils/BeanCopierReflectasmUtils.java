package com.github.jitwxs.demo.utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanCopierReflectasmUtils {
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, ConstructorAccess> CONSTRUCTOR_ACCESS_CACHE = new ConcurrentHashMap<>();

    private static final int MAX_CACHE_SIZE = 512;

    public static void copyProperties(Object source, Object target) {
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass());
        copier.copy(source, target, null);
    }

    public static <T> T copyProperties(T source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }

        T target;
        try {
            ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
            target = constructorAccess.newInstance();
        } catch (RuntimeException e) {
            try {
                target = targetClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                throw new RuntimeException(String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
            }
        }
        copyProperties(source, target);
        return target;
    }

    public static <T> List<T> copyProperties(List<?> sourceList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return Collections.emptyList();
        }

        ConstructorAccess<T> constructorAccess = getConstructorAccess(targetClass);
        List<T> resultList = new ArrayList<>(sourceList.size());
        for (Object source : sourceList) {
            T target;
            try {
                target = constructorAccess.newInstance();
            } catch (RuntimeException e) {
                try {
                    target = targetClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e1) {
                    throw new RuntimeException(String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
                }
            }

            copyProperties(source, target);
            resultList.add(target);
        }
        return resultList;
    }

    private static <T> ConstructorAccess<T> getConstructorAccess(Class<T> targetClass) {
        ConstructorAccess<T> constructorAccess = CONSTRUCTOR_ACCESS_CACHE.get(targetClass.getName());
        if(constructorAccess != null) {
            return constructorAccess;
        }
        try {
            constructorAccess = ConstructorAccess.get(targetClass);
            if (CONSTRUCTOR_ACCESS_CACHE.size() > MAX_CACHE_SIZE) {
                CONSTRUCTOR_ACCESS_CACHE.clear();
            }
            CONSTRUCTOR_ACCESS_CACHE.put(targetClass.getName(),constructorAccess);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Create new instance of %s failed: %s", targetClass, e.getMessage()));
        }
        return constructorAccess;
    }

    private static BeanCopier getBeanCopier(Class<?> sourceClazz, Class<?> targetClazz) {
        String key = generatorKey(sourceClazz, targetClazz);
        BeanCopier copier;
        if(BEAN_COPIER_MAP.containsKey(key)) {
            copier = BEAN_COPIER_MAP.get(key);
        } else {
            copier = BeanCopier.create(sourceClazz, targetClazz, false);
            BEAN_COPIER_MAP.put(key, copier);
        }
        return copier;
    }

    private static String generatorKey(Class<?> sourceClazz, Class<?> targetClazz) {
        return sourceClazz + "_" + targetClazz;
    }
}