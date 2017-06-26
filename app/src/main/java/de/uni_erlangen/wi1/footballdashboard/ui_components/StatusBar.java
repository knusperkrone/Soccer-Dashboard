package de.uni_erlangen.wi1.footballdashboard.ui_components;

import android.os.Handler;
import android.widget.TextView;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.list_live_event.LiveEventListAdapter;


public class StatusBar
{

    private static StatusBar instance;

    private final DatabaseAdapter dbAdapter = DatabaseAdapter.getInstance();
    private final GameGovernor governor = GameGovernor.getInstance();

    private final TextView teamLabel, timeLabel, goalLabel;
    private final Handler refreshHandler;
    private int maxPeriodTime;
    private boolean showingHome;
    private Clock clock;

    public int currTime = 60 * 40 + 40;
    public int shownTeamId = -1;

    private LiveEventListAdapter liveEventListAdapter;


    private StatusBar(Handler refreshHandler, int maxPeriodTime, TextView timeLabel, TextView goalLabel,
                      TextView teamLabel)
    {
        this.refreshHandler = refreshHandler;
        this.teamLabel = teamLabel;
        this.timeLabel = timeLabel;
        this.goalLabel = goalLabel;
        this.maxPeriodTime = maxPeriodTime;
        teamSwitched(0);
    }

    public static void initInstance(Handler handler, int maxMinutes, TextView timeLabel,
                                         TextView goalLabel, TextView teamLabel)
    {
        if (instance == null)
            instance = new StatusBar(handler, maxMinutes, timeLabel, goalLabel, teamLabel);
    }

    public static StatusBar getInstance()
    {
        return instance;
    }


    public void setLiveEventListAdapter(LiveEventListAdapter adapter)
    {
        this.liveEventListAdapter = adapter;
    }

    public void teamSwitched(int position)
    {
        if (position == 0) {
            showingHome = true;
            shownTeamId = governor.getHomeTeamId();
            teamLabel.setText(governor.getHomeTeamName());
        } else {
            showingHome = false;
            shownTeamId = governor.getAwayTeamId();
            teamLabel.setText(governor.getAwayTeamName());
        }
    }

    public boolean isShowingHome()
    {
        return showingHome;
    }


    public void startClock()
    {
        if (clock == null) {
            clock = new Clock();
            refreshHandler.postDelayed(clock, 1000);
        } else {
            clock.stopped = false;
        }
    }

    public void stopClock()
    {
        clock.stopped = true;
    }

    private class Clock implements Runnable
    {
        private boolean stopped = false;

        @Override
        public void run()
        {
            if (stopped)
                return;

            currTime++;

            if (showingHome)
                governor.getHomeTeam().updateLightColors();
            else
                governor.getAwayTeam().updateLightColors();


            dbAdapter.updateLiveList(liveEventListAdapter, currTime);

            int minutes = currTime / 60;
            int seconds = currTime % 60;
            if (minutes <= maxPeriodTime) {
                if (seconds < 10)
                    timeLabel.setText("" + minutes + ":0" + seconds);
                else
                    timeLabel.setText("" + minutes + ":" + seconds);
            } else {
                // TODO: Send Singal or something
            }
            refreshHandler.postDelayed(this, 1000);
        }
    }

}
