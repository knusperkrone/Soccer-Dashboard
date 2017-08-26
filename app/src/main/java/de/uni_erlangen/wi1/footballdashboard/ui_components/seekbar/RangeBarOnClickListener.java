package de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar;


import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.LinkedList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameGovernor;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.LiveActionListAdapter;

/**
 * Created by knukro on 7/4/17
 * .
 */

public class RangeBarOnClickListener implements RangeSeekBar.OnRangeSeekBarChangeListener<Integer>
{

    private final List<LiveActionListAdapter> registeredLiveListAdapter = new LinkedList<>();
    private final StatusBar statusBar;

    public RangeBarOnClickListener(StatusBar statusBar)
    {
        this.statusBar = statusBar;
    }

    /**
     * Due strange design of the library, this method only get's called if the user
     * active(!) drags the bar
     * It get's not called if we programmatically change values.
     */
    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue)
    {
        triggerCallbacks(minValue, maxValue);
        resetAndRefreshUIEvents(maxValue);
    }

    public void registerLiveListAdapter(LiveActionListAdapter adapter)
    {
        this.registeredLiveListAdapter.add(adapter);
    }

    public void unregisterLiveListAdapter(LiveActionListAdapter adapter)
    {
        this.registeredLiveListAdapter.remove(adapter);
    }

    public void updateDatabaseAdapter(int maxTime)
    {
        // Just update List
        for (LiveActionListAdapter adapter : registeredLiveListAdapter)
            DatabaseAdapter.getInstance().updateLiveList(adapter, maxTime);
    }

    private void triggerCallbacks(int minValue, int maxValue)
    {
        // Set's new bounds and adjusts shown time
        statusBar.setMinRange(minValue);
        statusBar.setMaxRange(maxValue);
        statusBar.refreshTimeView();

        // Refresh whole list
        for (LiveActionListAdapter adapter : registeredLiveListAdapter)
            adapter.refreshList(minValue, maxValue);
    }

    private void resetAndRefreshUIEvents(int maxValue)
    {
        GameGovernor gov = GameGovernor.getInstance();
        OPTA_Team homeTeam = gov.getHomeTeam();
        OPTA_Team awayTeam = gov.getAwayTeam();

        homeTeam.resetAndRefreshUI(maxValue);
        awayTeam.resetAndRefreshUI(maxValue);
    }

}
