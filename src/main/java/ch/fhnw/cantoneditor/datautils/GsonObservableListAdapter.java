package ch.fhnw.cantoneditor.datautils;

import java.io.IOException;

import ch.fhnw.observation.ObservableList;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class GsonObservableListAdapter extends TypeAdapter<ObservableList<?>> {

    private Class<T> adapterclass;

    public ArrayAdapter(Class<T> adapterclass) {

        this.adapterclass = adapterclass;
    }

    @Override
    public ObservableList<?> read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else if (reader.peek() == JsonToken.BEGIN_ARRAY) {

            Gson gson = new Gson();
            reader.beginArray();
            while (reader.hasNext()) {
                T inning = (T) gson.fromJson(reader, adapterclass);
                list.add(inning);
            }
            reader.endArray();

        }
    }

    @Override
    public void write(JsonWriter arg0, ObservableList<?> arg1) throws IOException {
        // TODO Auto-generated method stub

    }

}
