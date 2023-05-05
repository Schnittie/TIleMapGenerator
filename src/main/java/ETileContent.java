import lombok.Getter;

@Getter
public enum ETileContent {
    LAND(0, 'L', new RulesLand(), 10),
    COAST(1, 'C', new RulesCoast(), 1),
    SEA(2, 'S', new RulesSea(), 5),
    HILL(3, 'H', new RulesHill(), 2),
    REEF(4, 'R', new RulesReef(), 3),
    MOUNTAIN(5, 'M', new RulesMountain(), 1),
    OCEAN(6, 'O', new RulesOcean(), 2)
    ;

    private final int id;
    private final char character;
    public final RuleList ruleList;
    public final int probability;

    ETileContent(int id, char character, RuleList ruleList, int probability) {
        this.id = id;
        this.character = character;
        this.ruleList = ruleList;
        this.probability = probability;
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
