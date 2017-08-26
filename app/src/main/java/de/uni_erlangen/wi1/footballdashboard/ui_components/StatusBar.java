package de.uni_erlangen.wi1.footballdashboard.ui_components;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.LiveActionListAdapter;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.OnSeekBarChangeAble;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.RangeBarOnClickListener;


public class StatusBar
{

    private static StatusBar instance;

    private final GameGovernor governor = GameGovernor.getInstance();
    private final TextView teamLabel, timeLabel, goalLabel;
    private final Handler refreshHandler;
    private final Context context;
    private final int[] currentRange;
    private final int maxPeriodTime;

    private Clock clock;
    private RangeSeekBar<Integer> rangeBar;
    private boolean showingHome;

    private final RangeBarOnClickListener rangeListener;
    private final List<OnSeekBarChangeAble> registeredSeekBarChangeAbles = new ArrayList<>(4);

    private StatusBar(Context context, Handler refreshHandler, TextView timeLabel, TextView goalLabel,
                      TextView teamLabel)
    {
        this.refreshHandler = refreshHandler;
        this.context = context;
        this.teamLabel = teamLabel;
        this.timeLabel = timeLabel;
        this.goalLabel = goalLabel;
        this.maxPeriodTime = GameGovernor.getInstance().getLatestEventTime();
        teamSwitched(0);


        currentRange = new int[2];
        rangeListener = new RangeBarOnClickListener(this);
    }

    public static void initInstance(Context context, Handler handler, TextView timeLabel,
                                    TextView goalLabel, TextView teamLabel)
    {
        if (instance == null)
            instance = new StatusBar(context, handler, timeLabel, goalLabel, teamLabel);
    }

    public static StatusBar getInstance()
    {
        return instance;
    }

    public void teamSwitched(int position)
    {
        if (position == 0) {
            showingHome = true;
            teamLabel.setText(governor.getHomeTeamName());
        } else {
            showingHome = false;
            teamLabel.setText(governor.getAwayTeamName());
        }
    }

    public void setRangeBar(RangeSeekBar<Integer> rangeBar)
    {
        this.rangeBar = rangeBar;
        rangeBar.setRangeValues(0, maxPeriodTime);
        rangeBar.setSelectedMaxValue(0);
        rangeBar.setSelectedMinValue(0);

        rangeBar.setTextAboveThumbsColorResource(android.R.color.tab_indicator_text);
        rangeBar.setOnRangeSeekBarChangeListener(rangeListener);
        rangeBar.setOnTouchListener(new View.OnTouchListener()
        { // Stop clock while dragging

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startDrag();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    stopDrag();
                }
                return false;
            }
        });
    }

    public void refreshTimeView()
    {
        timeLabel.setText(timeHR(currentRange[1]));
        updateRegisteredAdapters();
    }

    private void updateRegisteredAdapters()
    {
        // Update registered adapters
        for (OnSeekBarChangeAble changeAble : registeredSeekBarChangeAbles)
            changeAble.seekBarChanged(currentRange[0], currentRange[1]);
        rangeListener.updateDatabaseAdapter(currentRange[1]);

        // Update team
        OPTA_Team homeTeam = governor.getHomeTeam();
        OPTA_Team awayTeam = governor.getAwayTeam();
        homeTeam.refreshUI(currentRange[1]);
        awayTeam.refreshUI(currentRange[1]);
    }

    public void setLiveActionListAdapter(LiveActionListAdapter adapter)
    {
        rangeListener.registerLiveListAdapter(adapter);
    }

    public void unregisterLiveActionListAdapter(LiveActionListAdapter adapter)
    {
        rangeListener.unregisterLiveListAdapter(adapter);
    }

    public void registerOnClicked(OnSeekBarChangeAble clickedView)
    {
        this.registeredSeekBarChangeAbles.add(clickedView);
    }

    public void unRegisterOnClicked(OnSeekBarChangeAble clickedView)
    {
        this.registeredSeekBarChangeAbles.remove(clickedView);
    }

    public void increaseGameTime()
    {
        if (clock.timeIndex >= clock.TIME_FORWARD.length) {
            makeToast("You can't go faster!");
        } else {
            clock.timeIndex++;
            makeToast("Your speed is " + clock.TIME_FORWARD[clock.timeIndex] + " seconds");
        }
    }

    public void decreaseGameTime()
    {
        if (clock.timeIndex == 0) {
            makeToast("You can't go slower!");
        } else {
            clock.timeIndex--;
            makeToast("Your speed is " + clock.TIME_FORWARD[clock.timeIndex] + " seconds");
        }
    }


    public void setGoals(int goals, boolean homeTeam)
    {
        String[] currGoals = goalLabel.getText().toString().split(":");

        if (homeTeam)
            goalLabel.setText(goals + ":" + currGoals[1]);
        else
            goalLabel.setText(currGoals[0] + ":" + goals);

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

    private void startDrag()
    {
        clock.dragged = true;
    }

    private void stopDrag()
    {
        clock.dragged = false;
    }

    private class Clock implements Runnable
    {
        private boolean stopped = false;
        private boolean dragged = false;

        final int[] TIME_FORWARD = {1, 4, 10, 20, 30, 60, 120, 300}; // In seconds
        int timeIndex = 2;

        @Override
        public void run()
        {
            if (stopped || dragged || currentRange[1] > maxPeriodTime) { // Nothing to do here!
                refreshHandler.postDelayed(this, 1000);
                return;
            }

            currentRange[1] += TIME_FORWARD[timeIndex]; // Time forward

            // Update time-View and registered listeners
            refreshTimeView();

            // Set new time to rangeBar
            rangeBar.setSelectedMaxValue(currentRange[1]);
            refreshHandler.postDelayed(this, 1000);
        }
    }

    private void makeToast(String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
