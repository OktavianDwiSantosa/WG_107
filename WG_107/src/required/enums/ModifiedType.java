package required.enums;

public enum ModifiedType {
    UPGRADE("Upgrade"), CRAFT("Craft"), BUY("Buy"), SELL("Sell");

    private final String name;

    ModifiedType(String vName) {
        name = vName;
    }

    @Override
    public String toString() { return name; }
}
