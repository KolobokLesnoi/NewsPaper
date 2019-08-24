package koloboklesnoi.newspaper.api.gson.converter;

import com.google.gson.*;
import koloboklesnoi.newspaper.model.MediaMetadatum;
import koloboklesnoi.newspaper.model.Medium;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class MediumGsonConverter implements JsonDeserializer<Medium> {
    @Override
    public Medium deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Medium medium = new Medium();
        JsonObject jsonObject = json.getAsJsonObject();

        MediaMetadatum[] mediaMetadata = context
                .deserialize(jsonObject.get("media-metadata"),MediaMetadatum[].class);
        medium.setMediaMetadata(new ArrayList<MediaMetadatum>(Arrays.asList(mediaMetadata)));

        return medium;
    }
}
