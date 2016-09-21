package com.mediaiq.graphOfUsers;

/**
 * @author rmandada
 */
public enum RelationType {
    KNOWS,
    FRIEND,
    RELATIVE;

    /**
     * Obtains the numeric value of the relation.
     */
    public static int getIntValue(RelationType relationType) {
        int res = 0;
        switch (relationType) {
            case RELATIVE:
                res = 0;
                break;
            case FRIEND:
                res = 1;
                break;
            case KNOWS:
                res = 2;
                break;

        }
        return res;
    }
}
