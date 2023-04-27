import lombok.Getter;

@Getter
public enum TileContent {
    LAND(0, 'L', new LandRules()),
    COAST(1, 'C', new CoastRules()),
    SEA(2, 'S', new SeaRules()),
    HILL(3, 'H', new HillRules()),
    REEF(4, 'R', new ReefRules()),
    MOUNTAIN(5, 'M', new MountainRules()),
    OCEAN(6, 'O', new OceanRules())
    ;

    private final int id;
    private final char character;
    public final RuleList ruleList;

    TileContent(int id, char character, RuleList ruleList) {
        this.id = id;
        this.character = character;
        this.ruleList = ruleList;
    }

    public static TileContent findById(int id) {
        for (TileContent value : TileContent.values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }
}
