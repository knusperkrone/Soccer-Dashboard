package de.uni_erlangen.wi1.footballdashboard.ui_components;

import android.os.Handler;
import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.OnSeekBarChangeAble;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.RangeBarOnClickListener;
import de.uni_erlangen.wi1.footballdashboard.ui_components.live_list.LiveTeamListAdapter;


public class StatusBar
{

    private static StatusBar instance;

    private final DatabaseAdapter dbAdapter = DatabaseAdapter.getInstance();
    private final GameGovernor governor = GameGovernor.getInstance();
    private final RangeBarOnClickListener rangeListener;
    private final TextView teamLabel, timeLabel, goalLabel;
    private final Handler refreshHandler;
    private final int[] currentRange;

    private Clock clock;
    private int maxPeriodTime;
    private boolean showingHome;
    public int shownTeamId = -1;

    private LiveTeamListAdapter liveTeamListAdapter;
    private RangeSeekBar<Integer> rangeBar;

    private OnSeekBarChangeAble clickedView;

    private StatusBar(Handler refreshHandler, TextView timeLabel, TextView goalLabel,
                      TextView teamLabel)
    {
        this.refreshHandler = refreshHandler;
        this.teamLabel = teamLabel;
        this.timeLabel = timeLabel;
        this.goalLabel = goalLabel;
        this.maxPeriodTime = GameGovernor.getInstance().getLatestEventTime();
        teamSwitched(0);

        currentRange = new int[2];
        rangeListener = new RangeBarOnClickListener(this);
    }

    public static void initInstance(Handler handler, TextView timeLabel,
                                    TextView goalLabel, TextView teamLabel)
    {
        if (instance == null)
            instance = new StatusBar(handler, timeLabel, goalLabel, teamLabel);
    }

    public static StatusBar getInstance()
    {
        return instance;
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

    public void setRangeBar(RangeSeekBar<Integer> rangeBar)
    {
        this.rangeBar = rangeBar;
        rangeBar.setRangeValues(0, maxPeriodTime);
        rangeBar.setOnRangeSeekBarChangeListener(rangeListener);
        rangeBar.setSelectedMaxValue(0);
        rangeBar.setSelectedMinValue(0);
    }

    public void setLiveTeamListAdapter(LiveTeamListAdapter adapter)
    {
        this.liveTeamListAdapter = adapter;
        rangeListener.setLiveListAdapter(adapter);
    }

    public void refreshTimeView()
    {
        timeLabel.setText(timeHR(currentRange[1]));
        if (clickedView != null)
            clickedView.seekBarChanged(currentRange[0], currentRange[1]);
    }

    public void registerOnClicked(OnSeekBarChangeAble clickedView)
    {
        this.clickedView = clickedView;
    }

    public void unRegisterOnClicked(OnSeekBarChangeAble clickedView)
    {
        this.clickedView = null;
    }

    public int getMinRange()
    {
        return currentRange[0];
    }

    public int getMaxRange()
    {
        return currentRange[1];
    }

    public void setMinRange(int value)
    {
        currentRange[0] = value;
    }

    public void setMaxRange(int value)
    {
        currentRange[1] = value;
    }

    public boolean isShowingHome()
    {
        return showingHome;
    }

    public void stopClock()
    {
        clock.stopped = true;
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

    private class Clock implements Runnable
    {
        private boolean stopped = false;

        @Override
        public void run()
        {
            if (stopped || currentRange[1] > maxPeriodTime) {
                refreshHandler.postDelayed(this, 1000);
                return;
            }

            currentRange[1]++; // Time forward

            if (showingHome)
                governor.getHomeTeam().updateLightColors(currentRange[1]);
            else
                governor.getAwayTeam().updateLightColors(currentRange[1]);

            dbAdapter.updateLiveList(liveTeamListAdapter, currentRange[1]);
            rangeBar.setSelectedMaxValue(currentRange[1]);
            refreshTimeView();

            refreshHandler.postDelayed(this, 1000);
        }
    }

    private static String timeHR(int currTime)
    {
        int minutes = currTime / 60;
        int seconds = currTime % 60;

        if (seconds < 10)
            return "" + minutes + ":0" + seconds;
        else
            return "" + minutes + ":" + seconds;
    }

}
