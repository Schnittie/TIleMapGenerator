public abstract class RuleList {
    public final static int LANDID = 0;
    public final static int COASTID = 1;
    public final static int SEAID = 2;
    public final static int HILLID = 3;
    public final static int REEFID = 4;
    public final static int MOUNTAINID = 5;
    public final static int OCEANID = 6;
    public abstract boolean canThisBeHere(Direction direction, TileContent tileContent);
}
