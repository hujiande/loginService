package com.hjd.net.managerservice.config.shiro.cache;

import com.hjd.net.managerservice.config.shiro.security.JwtProperties;
import com.hjd.net.managerservice.utils.JedisUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ShiroCacheManager implements CacheManager {

    @Autowired
    JedisUtils jedisUtils;

    @Resource
    JwtProperties jwtProperties;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new ShiroCache<K,V>(jedisUtils,jwtProperties);
    }
}
