package de.schnittie.model.database.configurations;

public enum RuleCreationInstruction {
    ADJACENCY_RULES, ROTATE_ONCE, ROTATE_TWICE, ROTATE_THRICE, ROTATE_NONE;

    public int getRotationInteger(){
        switch (this){
            case ADJACENCY_RULES -> {
                return -1;
            }
            case ROTATE_ONCE -> {
                return 1;
            }
            case ROTATE_NONE -> {
                return 0;
            }
            case ROTATE_TWICE ->
            {
                return 2;
            }
            case ROTATE_THRICE -> {
                return 3;
            }
        }
        return -1;
    }
}
