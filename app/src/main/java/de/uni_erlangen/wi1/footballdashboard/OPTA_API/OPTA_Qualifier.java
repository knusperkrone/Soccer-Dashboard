package de.uni_erlangen.wi1.footballdashboard.OPTA_API;

/**
 * Created by knukro on 5/22/17.
 */

public abstract class OPTA_Qualifier
{

    protected String value;
    private boolean evaluated;
    private double cost;

    public OPTA_Qualifier(String value) {
        this.value = value;
        evaluated = false;
    }

    public double GetCost() {
        if (!evaluated) {
            //evaluateCost(); // Template Method
            evaluated = true;
        }
        return this.cost;
    }

    public abstract int GetId();

}
