public enum EDirection {
    ABOVE, BELOW, RIGHT, LEFT, TOPLEFT, TOPRIGHT, DOWNLEFT, DOWNRIGHT;

    public EDirection getReverse() {
        switch (this) {
            case ABOVE -> {
                return BELOW;
            }
            case BELOW -> {
                return ABOVE;
            }
            case RIGHT -> {
                return LEFT;
            }
            case LEFT -> {
                return RIGHT;
            }
            case TOPLEFT -> {
                return DOWNRIGHT;
            }
            case TOPRIGHT -> {
                return DOWNLEFT;
            }
            case DOWNLEFT -> {
                return TOPRIGHT;
            }
            case DOWNRIGHT -> {
                return TOPLEFT;
            }
        }
        return null;
    }
}
