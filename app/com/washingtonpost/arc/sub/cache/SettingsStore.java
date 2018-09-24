package com.washingtonpost.arc.sub.cache;

import com.hazelcast.core.MapStore;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * This class can act as a write-through persistence with asynchronous commit to underlying permanent store
 */
public class SettingsStore implements MapStore<Long, TenantSettings> {
    @Override
    public void store(Long key, TenantSettings value) {

    }

    @Override
    public void storeAll(Map<Long, TenantSettings> map) {

    }

    @Override
    public void delete(Long key) {

    }

    @Override
    public void deleteAll(Collection<Long> keys) {

    }

    @Override
    public TenantSettings load(Long key) {
        return new TenantSettings();
    }

    @Override
    public Map<Long, TenantSettings> loadAll(Collection<Long> keys) {
        return Collections.EMPTY_MAP;
    }

    @Override
    public Iterable<Long> loadAllKeys() {
        return Collections.EMPTY_LIST;
    }
}

