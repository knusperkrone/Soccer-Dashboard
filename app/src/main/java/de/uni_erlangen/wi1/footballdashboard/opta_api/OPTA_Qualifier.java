package de.uni_erlangen.wi1.footballdashboard.opta_api;

import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Cross;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Hand;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Leading_To_Attempt;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Leading_To_Goal;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Overrun;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Penalty;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Red_Card;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Second_Yellow;
import de.uni_erlangen.wi1.footballdashboard.opta_api.QUALIFIERS.Yellow_Card;

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
            case API_QUALIFIER_IDS.OVERRUN:
                qualifier = new Overrun(value);
            case API_QUALIFIER_IDS.HAND:
                qualifier = new Hand(value);
            case API_QUALIFIER_IDS.LEADING_TO_ATTEMPT:
                qualifier = new Leading_To_Attempt(value);
            case API_QUALIFIER_IDS.LEADING_TO_GOAL:
                qualifier = new Leading_To_Goal(value);
            case API_QUALIFIER_IDS.PENALTY:
                qualifier = new Penalty(value);
            case API_QUALIFIER_IDS.RED_CARD:
                qualifier = new Red_Card(value);
            case API_QUALIFIER_IDS.SECOND_YELLOW:
                qualifier = new Second_Yellow(value);
            case API_QUALIFIER_IDS.YELLOW_CARD:
                qualifier = new Yellow_Card(value);
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

    public abstract String describeContent();

   /* public String descripeContent()
    {
        return "Qualifier";
    }
    */

}
