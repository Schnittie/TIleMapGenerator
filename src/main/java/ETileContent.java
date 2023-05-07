import lombok.Getter;

@Getter
public enum ETileContent {
    LAND(0, 'L',  RulesLand.getInstance(), 20,"src/main/images/land.png"),
    COAST(1, 'C',  RulesCoast.getInstance(), 2, "src/main/images/coast.png"),
    SEA(2, 'S',  RulesSea.getInstance(), 10, "src/main/images/sea.png"),
    HILL(3, 'H',  RulesHill.getInstance(), 2, "src/main/images/hill.png"),
    REEF(4, 'R',  RulesReef.getInstance(), 3, "src/main/images/reef.png"),
    MOUNTAIN(5, 'M', RulesMountain.getInstance(), 10,"src/main/images/mountain.png" ),
    OCEAN(6, 'O',  RulesOcean.getInstance(), 1,"src/main/images/ocean.png" )
    ;

    private final int id;
    private final char character;
    public final RuleList ruleList;
    public final int probability;
    public final String imagePath;

    ETileContent(int id, char character, RuleList ruleList, int probability, String imagePath) {
        this.id = id;
        this.character = character;
        this.ruleList = ruleList;
        this.probability = probability;
        this.imagePath = imagePath;
    }

    public static ETileContent findById(int id) {
        for (ETileContent value : ETileContent.values()) {
            if (value.id == id) {
                return value;
            }
        }
        throw new RuntimeException("no TileContentFound");
    }
}
