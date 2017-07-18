package de.uni_erlangen.wi1.footballdashboard.opta_api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views.PlayerView;


public class OPTA_Player implements Comparable
{

    private enum Position
    {
        GOALKEEPER, DEFENDER, MIDFIELDER, STRIKER, SUBSTITUTE
    }

    // OPTA values
    private final int id;
    private String name;
    private int shirtNumber;
    private int layoutPosition;
    private Position position;

    // Own values
    private boolean captain = false;
    private int rankingPoints = 60;
    private short cardIndicator = 0;

    public final List<OPTA_Event> actions = new ArrayList<>();
    public final List<Short> playerRankings = new ArrayList<>(90);

    PlayerView mappedView;


    public OPTA_Player(int id)
    {
        this.id = id;
    }

    public void setName(String firstName, @Nullable String knownName, String lastName)
    {
        // Shows KnowName, if it's there
        if (knownName != null && !knownName.equals("null")) {
            this.name = knownName;
        } else {
            // Or fullName, if Name if short enough
            if (lastName.length() < 8)
                this.name = firstName.charAt(0) + ". " + lastName;
            else
                this.name = lastName;
        }
    }

    public void setPosition(int position)
    {
        switch (position) {
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

    public void setPosition(String position)
    {
        setPosition(Integer.valueOf(position));
    }

    public void changeRankingPoints(int value)
    {
        rankingPoints += value;
        if (rankingPoints > 100) rankingPoints = 100;
        else if (rankingPoints < 0) rankingPoints = 0;
    }

    void mapView(PlayerView view)
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

    public void setCaptain(boolean isCaptain)
    {
        this.captain = isCaptain;
    }

    void setCardYellow()
    {
        cardIndicator = 1;
    }

    void setCardRed()
    {
        cardIndicator = 2;
    }

    public void removeAllCards()
    {
        cardIndicator = 0;
    }

    public boolean hasYellowCard()
    {
        return cardIndicator == 1;
    }

    public boolean hasRedCard()
    {
        return cardIndicator == 2;
    }

    boolean isActive()
    {
        return layoutPosition != 0;
    }

    public boolean isCaptain()
    {
        return captain;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    void setShirtNumber(int shirtNumber)
    {
        this.shirtNumber = shirtNumber;
    }

    void setLayoutPosition(int layoutPosition)
    {
        this.layoutPosition = layoutPosition;
    }

    public void setPosition(Position position)
    {
        this.position = position;
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

    int getLayoutPosition()
    {
        return layoutPosition;
    }

    public Position getPosition()
    {
        return position;
    }

    public int getRankingPoints()
    {
        return rankingPoints;
    }

    public List<OPTA_Event> getActions()
    {
        return actions;
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        // Prefer active over inactive and ranking descending
        OPTA_Player t1 = (OPTA_Player) o;
        if (isActive() && !t1.isActive())
            return -1;
        else if (!isActive() && t1.isActive())
            return 1;
        else if (!isActive() && !t1.isActive())
            return 0;
        return t1.getRankingPoints() - rankingPoints;
    }

    @Override
    public String toString()
    {
        return name + " (Points: " + rankingPoints + ", currRang:" + playerRankings.get(playerRankings.size() - 1) + ")";
    }
}
