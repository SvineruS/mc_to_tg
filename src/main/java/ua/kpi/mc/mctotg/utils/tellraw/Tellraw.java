package ua.kpi.mc.mctotg.utils.tellraw;

import com.google.gson.JsonArray;

import static ua.kpi.mc.mctotg.utils.Utils.runCommand;

public class Tellraw {
    JsonArray json = new JsonArray();

    public Tellraw(Text... texts) {
        for (Text t : texts)
            json.add(t.getJson());
    }

    public Tellraw add(Text text) {json.add(text.getJson()); return this;}

    public String toJson() {
        return json.toString();
    }

    public void send() { runCommand("tellraw @a "+ toJson()); }

}
