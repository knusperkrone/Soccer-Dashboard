package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Attempt_Saved;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Foul;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Goal;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Miss;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Pass;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Post;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Tackle;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/27/17.
 * Statistic numbers
 */

public class TeamPassGraphFragment extends Fragment implements ITeamFragment
{

    private TextView passCounterView;
    private TextView shootCounterView;
    private TextView tackleCounterView;
    private TextView foulCounterView;
    private OPTA_Team team;


    public static Fragment newInstance(OPTA_Team team)
    {
        TeamPassGraphFragment frag = new TeamPassGraphFragment();
        frag.team = team;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.stat_teaminfo_passgraph, container, false);

        passCounterView = (TextView) root.findViewById(R.id.team_paesse);
        shootCounterView = (TextView) root.findViewById(R.id.team_shoots);
        tackleCounterView = (TextView) root.findViewById(R.id.team_tackles);
        foulCounterView = (TextView) root.findViewById(R.id.team_fouls);

        // Draw values
        StatusBar bar = StatusBar.getInstance();
        seekBarChanged(bar.getMinRange(), bar.getMaxRange());
        return root;
    }

    @Override
    public void setNewTeam(OPTA_Team team)
    {
        this.team = team;
    }

    @Override
    public void setActive()
    {
        StatusBar.getInstance().registerOnClicked(this);
    }

    @Override
    public void setInactive()
    {
        StatusBar.getInstance().unRegisterOnClicked(this);
    }

    @Override
    public void seekBarChanged(int minVal, int maxVal)
    {
        int[][] countedEvents = countTeamPasses(minVal, maxVal);
        setText(passCounterView, countedEvents[0]);
        setText(shootCounterView, countedEvents[1]);
        setText(tackleCounterView, countedEvents[2]);
        setText(foulCounterView, countedEvents[3]);
    }

    private void setText(TextView view, int[] values)
    {
        view.setText(" " + values[0] + " : " + values[1]);
    }

    private int[][] countTeamPasses(int minTime, int maxTime)
    {
        List<OPTA_Event> events = team.getEvents();
        int[] passCounter = new int[2];
        int[] shootCounter = new int[2];
        int[] tackleCounter = new int[2];
        int[] foulCounter = new int[2];
        int i = 0;

        // Get first index in time-range
        while (i < events.size() && events.get(i).getCRTime() < minTime) i++;

        // Iterate over end of list or time-range
        for (; i < events.size() && events.get(i).getCRTime() < maxTime; i++) {

            OPTA_Event event = events.get(i);
            int[] counter = null;

            // Check if current event is interesting
            if (event instanceof Pass) {
                counter = passCounter;
            } else if (event instanceof Tackle) {
                counter = tackleCounter;
            } else if (event instanceof Attempt_Saved || event instanceof Goal) { // Shoots on
                counter = shootCounter;
            } else if (event instanceof Miss || event instanceof Post) { // Shoots off
                counter = shootCounter;
                shootCounter[1]--;
            } else if (event instanceof Foul) {
                counter = foulCounter;
            }

            // Count up
            if (counter != null) {
                counter[0]++;
                if (event.isSuccess())
                    counter[1]++;
            }
        }
        return new int[][]{passCounter, shootCounter, tackleCounter, foulCounter};
    }

}
