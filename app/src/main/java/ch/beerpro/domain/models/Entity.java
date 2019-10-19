package ch.beerpro.domain.models;

import java.util.HashMap;
import java.util.List;

public interface Entity {

    static <T extends Entity> HashMap<String, T> entitiesById(List<T> entries) {
        HashMap<String, T> byId = new HashMap<>();
        for (T entry : entries) {
            byId.put(entry.getId(), entry);
        }
        return byId;
    }

    String getId();

    void setId(String id);
}
