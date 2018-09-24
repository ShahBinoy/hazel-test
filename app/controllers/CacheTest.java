package controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

import com.washingtonpost.arc.sub.cache.HazelCache;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static play.libs.Json.*;

public class CacheTest  extends Controller {

    @Inject
    private HazelCache cache;

    public Result put() {
        long counter = System.currentTimeMillis();
        cache.put("tenant-settings","counter:"+counter,counter);
        return ok(Json.toJson(counter));
    }

    public Result get() {
        Long counter = cache.count("tenant-settings");
        return ok(Json.toJson(counter));
    }

}