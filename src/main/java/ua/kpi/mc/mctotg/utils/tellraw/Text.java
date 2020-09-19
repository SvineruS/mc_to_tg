package ua.kpi.mc.mctotg.utils.tellraw;


import com.google.gson.JsonObject;


public class Text {
    JsonObject json = new JsonObject();

    public Text(String text) {
        json.addProperty("text", text);
    }

    public Text color(String color) {
        json.addProperty("color", color);
        return this;
    }

    public Text suggestCommand(String cmd) {
        onClick(action("suggest_command", cmd));
        return this;
    }

    public Text openUrl(String url) {
        onClick(action("open_url", url));
        return this;
    }

    public Text showText(String text) {
        onHover(action("show_text", text));
        return this;
    }

    public JsonObject getJson() { return json; }

    JsonObject action(String action, String value) {
        JsonObject obj = new JsonObject();
        obj.addProperty("action", action);
        obj.addProperty("value", value);
        return obj;
    }

    void onClick(JsonObject obj) {
        json.add("clickEvent", obj);
    }


    void onHover(JsonObject obj) {
        json.add("hoverEvent", obj);
    }
}