package EverTech1.pingcounter;

import net.minecraft.network.chat.ClickEvent;

import java.lang.reflect.Constructor;
import java.net.URI;

public class ClickEventCompat {
    public static ClickEvent createOpenUrl(String url) {
        try {
            Class<?> openUrlClass = Class.forName("net.minecraft.network.chat.ClickEvent$OpenUrl");
            Constructor<?> ctor = openUrlClass.getConstructor(URI.class);
            return (ClickEvent) ctor.newInstance(URI.create(url));
        } catch (ClassNotFoundException e) {
            try {
                Constructor<ClickEvent> legacyCtor = ClickEvent.class.getConstructor(ClickEvent.Action.class, String.class);
                return legacyCtor.newInstance(ClickEvent.Action.OPEN_URL, url);
            } catch (Exception inner) {
                throw new RuntimeException("Failed to create legacy ClickEvent", inner);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create OpenUrl ClickEvent", e);
        }
    }
}
