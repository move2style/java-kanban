package webserver;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import adapter.LocalDateTimeSerializer;
import com.google.gson.Gson;

import java.time.Duration;
import java.time.LocalDateTime;

public final class GsonBuilders {

    public GsonBuilders() {

    }

    public static Gson getGson() {
        return new com.google.gson.GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .create();
    }
}
