package koloboklesnoi.newspaper.api.gson.converter;

import com.google.gson.*;
import koloboklesnoi.newspaper.model.MediaMetadatum;

import java.lang.reflect.Type;

public class MediaMetadatumGsonConverter implements JsonDeserializer<MediaMetadatum> {
    @Override
    public MediaMetadatum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        MediaMetadatum mediaMetadatum = new MediaMetadatum();
        JsonObject jsonObject = json.getAsJsonObject();

        mediaMetadatum.setUrl(jsonObject.get("url").getAsString());

        return mediaMetadatum;
    }
}
