package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        String durationString = String.valueOf(duration.toMillis());
        jsonWriter.value(durationString);
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        return Duration.ofMillis(Long.parseLong(jsonReader.nextString()));
    }
}
