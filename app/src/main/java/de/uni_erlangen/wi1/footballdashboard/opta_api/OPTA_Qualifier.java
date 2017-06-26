package de.uni_erlangen.wi1.footballdashboard.opta_api;

import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Cross;

/**
 * Created by knukro on 5/22/17.
 */

public abstract class OPTA_Qualifier
{

    protected final String value;
    private boolean evaluated;
    private double cost;

    public OPTA_Qualifier(String value)
    {
        this.value = value;
        evaluated = false;
    }

    public static OPTA_Qualifier newInstance(int id, String value)
    {
        OPTA_Qualifier qualifier = new Cross(value);
        switch (id) {
            default:
        }
        return qualifier;
    }


    public double GetCost()
    {
        if (!evaluated) {
            //evaluateCost(); // Template Method
            evaluated = true;
        }
        return this.cost;
    }

    public String getValue()
    {
        return value;
    }

    public abstract int getId();

    public String descripeContent()
    {
        return "Qualifier";
    }

}
