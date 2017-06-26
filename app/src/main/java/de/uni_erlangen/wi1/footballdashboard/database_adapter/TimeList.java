package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 6/16/17.
 */

class TimeList
{

    final List<OPTA_Event> dataList;
    private int currIndex = 0;

    TimeList(List<OPTA_Event> dataList)
    {
        this.dataList = dataList;
    }

    List<OPTA_Event> updateValues(int newTime)
    {
        List<OPTA_Event> outList = new LinkedList<>();
        //int startIndex = currIndex;
        while (currIndex < dataList.size() && dataList.get(currIndex).getCRTime() < newTime) {
            outList.add(dataList.get(currIndex));
            currIndex++;
        }
        //Log.d("[TIME_LIST]", "New Values from Index[" + startIndex + "," + currIndex + "]");
        return outList;
    }

    List<OPTA_Event> getValuesUntil(int time)
    {
        List<OPTA_Event> outList = new LinkedList<>();
        for (int i = 0; i < dataList.size(); i++) {
            OPTA_Event info = dataList.get(i);
            if (info.getCRTime() > time) {
                //Log.d("[TIME_LIST]", "All Values until Index[0, " + (i - 1) + "]");
                if (i != 0)
                    currIndex = i;
                break;
            }
            outList.add(info);
        }

        return outList;
    }


}
