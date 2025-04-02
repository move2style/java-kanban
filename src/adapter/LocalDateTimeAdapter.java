package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {

       String localDateTimeString = Objects.nonNull(localDateTime) //если не пустое тогда
               ? localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) // изменить формат на DateTimeFormatter.ISO_LOCAL_DATE_TIME
               : LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); //иначе взять текущее время и записать в формате DateTimeFormatter.ISO_LOCAL_DATE_TIME
        jsonWriter.value(localDateTimeString);
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
