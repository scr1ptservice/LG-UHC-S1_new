package scriptservice.uhc.loupgarou.classes;

public class commandResult {
    private final boolean right;
    private final String prefix;

    // public "set"
    public commandResult(boolean right, String prefix) {
        this.right = right;
        this.prefix = prefix;
    }

    // "get"
    public String getPrefix() {
        return prefix;
    }

    // "has"
    public boolean hasRight() {
        return right;
    }
    public boolean hasPermission() {
        return right;
    }
}
