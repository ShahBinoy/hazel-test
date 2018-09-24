package com.washingtonpost.arc.sub.cache;

import com.hazelcast.aws.AwsDiscoveryStrategyFactory;
import com.hazelcast.config.*;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.instance.HazelcastInstanceFactory;
import com.hazelcast.spi.properties.GroupProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class HazelCache {
    private static      Logger          log                                    = LoggerFactory.getLogger(HazelCache.class);
    public static final EvictionConfig  NEVER_EVICT_CONFIG                     = new EvictionConfig().setEvictionPolicy(EvictionPolicy.NONE);
    private static      NearCacheConfig LONG_TERM_NEAR_CACHE_CONFIG            = new NearCacheConfig().setName("as-long-term-near-cache")
                                                                                                      .setInMemoryFormat(InMemoryFormat.OBJECT)
                                                                                                      .setCacheLocalEntries(true)
                                                                                                      .setTimeToLiveSeconds(0)
                                                                                                      .setInvalidateOnChange(true)
                                                                                                      .setEvictionConfig(NEVER_EVICT_CONFIG);
    private static      MapStoreConfig  DB_BACKED_MAP_CONFIG                   = new MapStoreConfig()
                                                                                         .setClassName("com.washingtonpost.arc.sub.cache.SettingsStore")
                                                                                         .setEnabled(true)
                                                                                         .setWriteDelaySeconds(1)
                                                                                         .setInitialLoadMode(MapStoreConfig.InitialLoadMode.EAGER)
                                                                                         .setWriteCoalescing(false);
    /**
     * Settings Map Config with Near-Cache And DB Store
     */
    private static      MapConfig       MAPCONFIG_WITH_NEAR_CACHE_AND_MAPSTORE = ( new MapConfig() ).setName("tenant-settings")
                                                                                                    .setBackupCount(2)
                                                                                                    .setTimeToLiveSeconds(300)
                                                                                                    .setNearCacheConfig(LONG_TERM_NEAR_CACHE_CONFIG)
                                                                                                    .setMapStoreConfig(DB_BACKED_MAP_CONFIG);
    private static      Config          MAIN_HAZEL_CONFIG                      = new Config();

    HazelcastInstance hazelcastInstance;

    @Inject
    public HazelCache(com.typesafe.config.Config config) {
        log.info("Starting HazelCast Cache configuration");

        AwsDiscoveryStrategyFactory awsDiscoveryStrategyFactory = new AwsDiscoveryStrategyFactory();
        Map<String, Comparable>     properties                  = new HashMap<String, Comparable>();
        properties.put("hz-port", config.getInt("hazel.port"));
        properties.put("iam-role","ecsInstanceRole");
        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(awsDiscoveryStrategyFactory, properties);

        //DiscoveryConfig dc = new DiscoveryConfig().addDiscoveryStrategyConfig(com.hazelcast
        MAIN_HAZEL_CONFIG.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED.getName(), String.valueOf(true));

        NetworkConfig networkConfig = MAIN_HAZEL_CONFIG.getNetworkConfig();
        networkConfig.getInterfaces()
                     .addInterface(config.getString("hazel.interfaces"));
        JoinConfig njc = networkConfig.getJoin();
        njc.getTcpIpConfig()
           .setEnabled(false);
        njc.getAwsConfig()
           .setEnabled(false);
        njc.getMulticastConfig()
           .setEnabled(false);
        njc.getDiscoveryConfig()
           .addDiscoveryStrategyConfig(discoveryStrategyConfig);
        
        networkConfig
                .setPort(config.getInt("hazel.port"))
                .setPortAutoIncrement(false);

        hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(MAIN_HAZEL_CONFIG);


        IMap<String, Object> map = hazelcastInstance
                                           .getMap("tenant-settings");

    }

    public void put(String s, String counter, long counter1) {
        hazelcastInstance.getMap(s)
                         .put(counter, counter1);
    }

    public <T> T get(String s, String counter) {
        return (T) hazelcastInstance.getMap(s)
                                    .get(counter);
    }

    public long count(String s) {
        return hazelcastInstance.getMap(s)
                                .size();
    }
}

