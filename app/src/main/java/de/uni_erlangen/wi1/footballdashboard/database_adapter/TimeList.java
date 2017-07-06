package de.uni_erlangen.wi1.footballdashboard.database_adapter;

import java.util.LinkedList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;

/**
 * Created by knukro on 6/16/17.
 */

class TimeList
{

    final List<OPTA_Event> dataList; // Final reference to the whole (cached) data;
    private int currIndex = 0; // Index of the last returned element

    TimeList(List<OPTA_Event> dataList)
    {
        this.dataList = dataList;
    }

    List<OPTA_Event> updateValues(int newTime)
    {
        // Create a new list
        List<OPTA_Event> outList = new LinkedList<>();
        // Update values until the currentElement.time is bigger than @newTime
        while (currIndex < dataList.size() && dataList.get(currIndex).getCRTime() < newTime) {
            outList.add(dataList.get(currIndex));
            currIndex++; // Update current Index
        }

        return outList;
    }

    List<OPTA_Event> getValuesRange(int startTime, int endTime)
    {
        // Get index of first element.time >= startTime
        int i = 0;
        while (dataList.get(i).getCRTime() < startTime)
            i++;

        List<OPTA_Event> outList = new LinkedList<>();
        // Go to rest of list list and return when currentItem.time is bigger than @endTime
        for (; i < dataList.size(); i++) {
            OPTA_Event info = dataList.get(i);

            if (info.getCRTime() > endTime) {
                // Set new currIndex and return list
                if (i != 0)
                    currIndex = i;
                break;
            }

            outList.add(info);
        }

        return outList;
    }


}
