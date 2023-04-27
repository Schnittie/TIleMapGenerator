public enum Direction {
    ABOVE,BELOW,TORIGHT,TOLEFT;
    public Direction getReverse(){
        switch (this){
            case ABOVE -> {
                return BELOW;
            }
            case BELOW -> {
                return ABOVE;
            }
            case TORIGHT -> {
                return TOLEFT;
            }
            case TOLEFT -> {
                return TORIGHT;
            }
        }
        return null;
    }
}
