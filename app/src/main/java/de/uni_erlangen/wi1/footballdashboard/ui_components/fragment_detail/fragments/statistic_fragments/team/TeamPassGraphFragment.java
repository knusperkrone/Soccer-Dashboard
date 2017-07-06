package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.team;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.EVENT_INFO.Pass;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/27/17.
 * TestFragment
 */

public class TeamPassGraphFragment extends Fragment implements ITeamFragment
{

    private TextView countView;
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

        countView = (TextView) root.findViewById(R.id.team_paesse);

        drawStatistics();
        return root;
    }

    @Override
    public void setNewTeam(OPTA_Team team)
    {
        this.team = team;
    }

    @Override
    public void drawStatistics()
    {
        int[] passes = countTeamPasses();
        countView.setText("" + passes[0] + "/" + passes[1]);
    }


    private int[] countTeamPasses()
    {
        int[] counter = new int[2];
        int currTime = StatusBar.getInstance().getMaxRange();
        OPTA_Event lastPass = null;

        for (OPTA_Event event : team.getEvents()) {
            if (lastPass != null) {
                lastPass = null;
            }

            if (event.getCRTime() > currTime)
                break;

            if (event instanceof Pass) {
                lastPass = event;
                counter[0]++;
                if (event.isSuccess())
                    counter[1]++;
            }
        }
        return counter;
    }


}
