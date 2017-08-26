package de.uni_erlangen.wi1.footballdashboard.helper;

/**
 * Created by knukro on 18.07.17.
 * Helper struct
 */

public class TeamFormationChange
{

    private final ChangeInfo[] changeInfos;
    private boolean hasJerseyNumbers, hasPosition, hasLayoutPosition, hasPlayerIds, hasCaptain, hasLayoutId;

    private int captainId;
    private int layoutId;

    public TeamFormationChange(int size)
    {
        changeInfos = new ChangeInfo[size];
        for (int i = 0; i < size; i++)
            changeInfos[i] = new ChangeInfo();
    }


    private static class ChangeInfo
    {
        int jerseyNumber;
        int position;
        int layoutPosition;
        int playerId;
    }

    public int size()
    {
        return changeInfos.length;
    }

    public void setJerseyNumber(int index, int jerseyNumber)
    {
        hasJerseyNumbers = true;
        changeInfos[index].jerseyNumber = jerseyNumber;
    }

    public void setPlayerPosition(int index, int position)
    {
        hasPosition = true;
        changeInfos[index].position = position;
    }

    public void setPlayerLayoutPosition(int index, int layoutPosition)
    {
        hasLayoutPosition = true;
        changeInfos[index].layoutPosition = layoutPosition;
    }

    public void setPlayerId(int index, int playerId)
    {
        hasPlayerIds = true;
        changeInfos[index].playerId = playerId;
    }

    public void setCaptainId(int captainId)
    {
        hasCaptain = true;
        this.captainId = captainId;
    }

    public void setLayoutId(int layoutId)
    {
        hasLayoutId = true;
        this.layoutId = layoutId;
    }

    public int getJerseyNumber(int index)
    {
        return changeInfos[index].jerseyNumber;
    }

    public int getPosition(int index)
    {
        return changeInfos[index].position;
    }

    public int getLayoutPosition(int index)
    {
        return changeInfos[index].layoutPosition;
    }

    public int getPlayerId(int index)
    {
        return changeInfos[index].playerId;
    }

    public int getCaptainId()
    {
        return captainId;
    }

    public int getLayoutId()
    {
        return layoutId;
    }


    public boolean hasJerseyNumbers()
    {
        return hasJerseyNumbers;
    }

    public boolean hasPosition()
    {
        return hasPosition;
    }

    public boolean hasLayoutPosition()
    {
        return hasLayoutPosition;
    }

    public boolean hasPlayerIds()
    {
        return hasPlayerIds;
    }

    public boolean hasCaptain()
    {
        return hasCaptain;
    }

    public boolean hasLayoutId()
    {
        return hasLayoutId;
    }

}
