// Language: java
package entities;

public class DialogueEntry {
    public enum Type { JONA, EXPLANATION }
    private final Type type;
    private final String text;

    public DialogueEntry(Type type, String text) {
        this.type = type;
        this.text = text;
    }

    public Type getType() { return type; }
    public String getText() { return text; }
}
