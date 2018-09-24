package com.washingtonpost.arc.sub.cache;

import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class HazelCastModule extends Module {

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {
        return seq( bind(HazelCastStarter.class).toSelf().eagerly(),
                    bind(HazelCache.class).toSelf().eagerly());
    }
}
