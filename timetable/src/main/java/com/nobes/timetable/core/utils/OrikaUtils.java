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

    /**
     * 缓存实例集合
     */
    private static final Map<String, MapperFacade> CACHE_MAPPER = new ConcurrentHashMap<>();

    private final MapperFacade mapper;

    public OrikaUtils(MapperFacade mapper) {
        this.mapper = mapper;
    }

    /**
     * 转换实体函数
     *
     * @param sourceEntity 源实体
     * @param targetClass  目标类对象
     * @param refMap       配置源类与目标类不同字段名映射
     * @param <S>          源泛型
     * @param <T>          目标泛型
     * @return 目标实体
     */
    public static <S, T> T convert(S sourceEntity, Class<T> targetClass, Map<String, String> refMap) {
        if (sourceEntity == null) {
            return null;
        }
        return classMap(sourceEntity.getClass(), targetClass, refMap).map(sourceEntity, targetClass);
    }

    /**
     * 转换实体函数
     *
     * @param sourceEntity 源实体
     * @param targetClass  目标类对象
     * @param <S>          源泛型
     * @param <T>          目标泛型
     * @return 目标实体
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
     * 属性名称不一致可用
     *
     * @param source 原对象
     * @param target 目标对象
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
     * Orika复制对象
     *
     * @param source 源数据
     * @param target 目标对象
     * @return target
     */
    private <V, P> P map(V source, Class<P> target) {
        return mapper.map(source, target);
    }

}
