package tsea.components;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import tsea.core.GameObject;
import tsea.core.Transform;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                
                String name = jsonObject.get("name").getAsString();
                Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
                int zIndex = context.deserialize(jsonObject.get("zIndex"), int.class);

                JsonArray components = jsonObject.getAsJsonArray("components");

                GameObject gameObject =  new GameObject(name, transform, zIndex);

                for(JsonElement jsonElement : components)
                    gameObject.addComponent(context.deserialize(jsonElement, Component.class));

                return gameObject;
    }
    
}
