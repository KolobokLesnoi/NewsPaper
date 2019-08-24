package koloboklesnoi.newspaper.api.gson.converter;

import com.google.gson.*;
import koloboklesnoi.newspaper.model.Medium;
import koloboklesnoi.newspaper.model.Result;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class ResultGsonConverter implements JsonDeserializer<Result> {
    @Override
    public Result deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Result result = new Result();
        JsonObject jsonObject = json.getAsJsonObject();

        result.setSection(jsonObject.get("section").getAsString());
        result.setId(jsonObject.get("id").getAsLong());
        result.setTitle(jsonObject.get("title").getAsString());
        result.setAbstract(jsonObject.get("abstract").getAsString());
        result.setPublishedDate(jsonObject.get("published_date").getAsString());
        result.setSource(jsonObject.get("source").getAsString());

        Medium[] media = context.deserialize(jsonObject.get("media"),Medium[].class);
        result.setMedia(new ArrayList<Medium>(Arrays.asList(media)));

        return result;
    }


}
