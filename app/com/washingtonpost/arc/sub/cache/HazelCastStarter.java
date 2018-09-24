package com.washingtonpost.arc.sub.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.CompleteConfiguration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HazelCastStarter {
    private static Logger log = LoggerFactory.getLogger(HazelCastStarter.class);

    @Inject
    public HazelCastStarter(com.typesafe.config.Config config){
        log.info("Starting Hazelcast HazelCache..");
        //HazelCache     hc               = new HazelCache(config);


        //c.IMPL = cacheManager.createCache()
        
    }
}
