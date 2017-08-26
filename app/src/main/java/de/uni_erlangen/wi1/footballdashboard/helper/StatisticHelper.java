package de.uni_erlangen.wi1.footballdashboard.helper;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Attempt_Saved;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Foul;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Goal;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Miss;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Pass;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Post;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Tackle;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;

/**
 * Created by knukro on 23.08.17
 * .
 */

@SuppressWarnings("unchecked")
public class StatisticHelper
{

    public static void countPlayerEvents(OPTA_Player player, int[][] counter, int minTime, int maxTime)
    {
        // TODO: Don't parse whole list every second
        List<OPTA_Event> events = player.getActions();
        int i = 0;

        // Get first index in time-range
        while (i < events.size() && events.get(i).getCRTime() < minTime) i++;

        // Iterate over end of list or time-range
        for (; i < events.size() && events.get(i).getCRTime() < maxTime; i++) {

            OPTA_Event event = events.get(i);
            int index = -1;

            // Check if current event is interesting
            if (event instanceof Pass && !event.hasQualifier(2, 5, 6, 107, 123, 124)) {
                index = 0;
            } else if (event instanceof Attempt_Saved || event instanceof Goal) { // Shoots on
                index = 1;
            } else if (event instanceof Miss || event instanceof Post) { // Shoots off
                index = 1;
                counter[1][1]--; // Missed shots
            } else if (event instanceof Tackle) {
                index = 2;
            } else if (event instanceof Foul) {
                index = 3;
            }

            // Count up
            if (index != -1) {
                counter[4][0]++; // Overall count
                counter[index][0]++; // Count total of this event
                if (event.isSuccess()) {
                    counter[4][1]++; // Overall success count
                    counter[index][1]++; // count success of this event

                }

            }
        }
    }

    public static void setText(@NonNull TextView view, int[] values)
    {
        int quote = 0;
        if (values[1] != 0) { // Strong typed languages for the win.
            float percent = (float) values[0] / 100f;
            quote = (int) ((float) values[1] / percent);
        }
        view.setText("Done: " + values[0] + "\nSuccess: " + values[1] + "\nQuote: " + quote + " %");
    }

    public static SparseArray getPassesFor(SparseArray ret, List<OPTA_Event> events, SparseArray<OPTA_Player> playerSparseArray,
                                           OPTA_Player receiver, int minTime, int maxTime)
    {
        OPTA_Event lastPass = null;
        for (OPTA_Event currEvent : events) {
            if (currEvent.getCRTime() < minTime)
                continue; // Not quite there yet

            // The last event was a successful pass and the next event.Id is @receiver id
            if (lastPass != null && currEvent.getPlayerId() == receiver.getId()) {
                int giverId = playerSparseArray.get(lastPass.getPlayerId()).getId();
                ret.put(giverId, (int) ret.get(giverId, 0) + 1);
                lastPass = null;
            }

            if (currEvent.getCRTime() > maxTime)
                break; // We are finished now

            if (currEvent instanceof Pass && currEvent.isSuccess())
                lastPass = currEvent;
        }

        return ret;
    }

    public static SparseArray getPassesBy(SparseArray ret, List<OPTA_Event> events, SparseArray<OPTA_Player> playerSparseArray,
                                          OPTA_Player giver, int minTime, int maxTime)
    {
        boolean lastWasPass = false;
        for (OPTA_Event currEvent : events) {
            if (currEvent.getCRTime() < minTime)
                continue; // Not quite there yet

            if (lastWasPass) {
                //TODO: Filter out data noise
                try {
                    int receiverID = playerSparseArray.get(currEvent.getPlayerId()).getId();
                    ret.put(receiverID, (int) ret.get(receiverID, 0) + 1);
                    lastWasPass = false;
                } catch (Exception e) {
                    Log.d("PLAYER_TEAM", "[CATCH] Couldn't find anything for: " + currEvent.getPlayerId());
                }
            }

            if (currEvent.getCRTime() > maxTime)
                break; // We are finished now

            // Check if currEvent is a successful pass by the @giver player
            if (currEvent instanceof Pass && currEvent.isSuccess()
                    && currEvent.getPlayerId() == giver.getId())
                lastWasPass = true;
        }
        return ret;
    }


}
