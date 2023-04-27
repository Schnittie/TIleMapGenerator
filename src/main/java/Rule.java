public record Rule(int toCheck, Direction direction) {
    @Override
    public boolean equals(Object obj) {
       return (obj.getClass().equals(this.getClass()) && ((Rule) obj).direction == this.direction && ((Rule) obj).toCheck == this.toCheck);
    }
}
