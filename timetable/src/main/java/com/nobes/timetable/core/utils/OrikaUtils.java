package com.nobes.timetable.core.utils;


import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class OrikaUtils {

    private static final MapperFactory FACTORY = new DefaultMapperFactory.Builder().build();
    private static final Map<String, MapperFacade> CACHE_MAPPER = new ConcurrentHashMap<>();

    private final MapperFacade mapper;

    public OrikaUtils(MapperFacade mapper) {
        this.mapper = mapper;
    }

    /**
     * convert entity
     *
     * @param sourceEntity
     * @param targetClass
     * @param refMap
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T convert(S sourceEntity, Class<T> targetClass, Map<String, String> refMap) {
        if (sourceEntity == null) {
            return null;
        }
        return classMap(sourceEntity.getClass(), targetClass, refMap).map(sourceEntity, targetClass);
    }

    /**
     * convert entity
     *
     * @param sourceEntity
     * @param targetClass
     * @param <S>
     * @param <T>
     * @return
     */
    public static <S, T> T convert(S sourceEntity, Class<T> targetClass) {
        return convert(sourceEntity, targetClass, null);
    }


    /**
     * register attributes
     *
     * @param source source class
     * @param target target class
     * @param refMap convert
     */
    public static <V, P> void register(Class<V> source, Class<P> target, Map<String, String> refMap) {
        if (CollectionUtils.isEmpty(refMap)) {
            FACTORY.classMap(source, target).byDefault().register();
        } else {
            ClassMapBuilder<V, P> classMapBuilder = FACTORY.classMap(source, target);
            refMap.forEach(classMapBuilder::field);
            classMapBuilder.byDefault().register();
        }
    }

    /**
     * attribute with different names
     *
     * @param source
     * @param target
     * @return OrikaUtils
     */
    private static synchronized <V, P> OrikaUtils classMap(Class<V> source, Class<P> target, Map<String, String> refMap) {
        String key = source.getCanonicalName() + ":" + target.getCanonicalName();
        if (CACHE_MAPPER.containsKey(key)) {
            return new OrikaUtils(CACHE_MAPPER.get(key));
        }
        register(source, target, refMap);
        MapperFacade mapperFacade = FACTORY.getMapperFacade();
        CACHE_MAPPER.put(key, mapperFacade);

        return new OrikaUtils(mapperFacade);
    }


    /**
     * Orika copy
     *
     * @param source
     * @param target
     * @return target
     */
    private <V, P> P map(V source, Class<P> target) {
        return mapper.map(source, target);
    }

}
