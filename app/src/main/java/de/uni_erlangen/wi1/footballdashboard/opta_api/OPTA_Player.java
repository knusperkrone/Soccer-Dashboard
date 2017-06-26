package de.uni_erlangen.wi1.footballdashboard.opta_api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;


public class OPTA_Player implements Comparable
{

    private enum Position
    {
        GOALKEEPER, DEFENDER, MIDFIELDER, STRIKER, SUBSTITUTE
    }

    // OPTA Data
    private final int id;
    private String name;
    private int shirtNumber;
    private int layoutPosition;
    private Position position;

    // Own values
    private boolean captain = false;
    private int rankingPoints = 60;
    private short card = 0;

    public final List<OPTA_Event> actions = new ArrayList<>();

    public final List<Integer> teamRankings = new LinkedList<>();
    public PlayerView mappedView;

    public OPTA_Player(int id)
    {
        this.id = id;
    }

    public void setCaptain(boolean isCaptain)
    {
        this.captain = isCaptain;
    }

    public void setName(String firstName, @Nullable String knownName, String lastName)
    {
        if (knownName != null && !knownName.equals("null")) {
            this.name = knownName;
        } else {
            if (lastName.length() < 8)
                this.name = firstName.charAt(0) + ". " + lastName;
            else
                this.name = lastName;
        }
    }

    public void mapView(PlayerView view)
    {
        this.mappedView = view;
    }

    public void setShirtNumber(String shirtNumber)
    {
        this.shirtNumber = Integer.valueOf(shirtNumber);
    }

    public void setLayoutPosition(String layoutPos)
    {
        this.layoutPosition = Integer.valueOf(layoutPos);
    }

    public void setPosition(String position)
    {
        //TODO: Double check!
        switch (Integer.valueOf(position)) {
            case 1:
                this.position = Position.GOALKEEPER;
                break;
            case 2:
                this.position = Position.DEFENDER;
                break;
            case 3:
                this.position = Position.MIDFIELDER;
                break;
            case 4:
                this.position = Position.STRIKER;
                break;
            case 5:
                this.position = Position.SUBSTITUTE;
                break;
        }
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getShirtNumber()
    {
        return shirtNumber;
    }

    public int getLayoutPosition()
    {
        return layoutPosition;
    }

    public Position getPosition()
    {
        return position;
    }

    public boolean isCaptain()
    {
        return captain;
    }

    public int getRankingPoints()
    {
        return rankingPoints;
    }

    public List<OPTA_Event> getActions()
    {
        return actions;
    }

    public boolean hasYellowCard()
    {
        return card == 1;
    }

    public boolean hasRedCard()
    {
        return card == 2;
    }

    public boolean isActive()
    {
        return layoutPosition != 0;
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        OPTA_Player t1 = (OPTA_Player) o;
        if (isActive() && !t1.isActive())
            return -1;
        else if (!isActive() && t1.isActive())
            return 1;
        else if (!isActive() && !t1.isActive())
            return 0;
        return getRankingPoints() - t1.getRankingPoints();
    }

}
