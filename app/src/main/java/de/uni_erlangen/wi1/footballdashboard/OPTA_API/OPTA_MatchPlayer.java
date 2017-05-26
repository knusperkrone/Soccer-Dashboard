package de.uni_erlangen.wi1.footballdashboard.OPTA_API;

import android.support.annotation.NonNull;

/**
 * Created by knukro on 5/22/17.
 */

public class OPTA_MatchPlayer
{

    public final int id;
    public int jerseyNumber;
    public final String name;
    public boolean captain;
    public boolean active;
    public int position;


    public OPTA_MatchPlayer(int id, int jerseyNumber, @NonNull String name, boolean captain,
                            boolean active, int position)
    {
        this.id = id;
        this.jerseyNumber = jerseyNumber;
        this.name = name;
        this.captain = captain;
        this.active = active;
        this.position = position;
    }

}
