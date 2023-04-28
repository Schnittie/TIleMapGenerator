import lombok.Getter;

@Getter
public enum ETileContent {
    LAND(0, 'L', new RulesLand()),
    COAST(1, 'C', new RulesCoast()),
    SEA(2, 'S', new RulesSea()),
    HILL(3, 'H', new RulesHill()),
    REEF(4, 'R', new RulesReef()),
    MOUNTAIN(5, 'M', new RulesMountain()),
    OCEAN(6, 'O', new RulesOcean())
    ;

    private final int id;
    private final char character;
    public final RuleList ruleList;

    ETileContent(int id, char character, RuleList ruleList) {
        this.id = id;
        this.character = character;
        this.ruleList = ruleList;
    }

    public static ETileContent findById(int id) {
        for (ETileContent value : ETileContent.values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }
}
