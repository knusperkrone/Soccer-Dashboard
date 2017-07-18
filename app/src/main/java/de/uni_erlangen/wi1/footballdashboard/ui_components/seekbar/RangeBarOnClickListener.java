package de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar;


import org.florescu.android.rangeseekbar.RangeSeekBar;

import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.main_list.LiveTeamListAdapter;

/**
 * Created by knukro on 7/4/17.
 *
 */

public class RangeBarOnClickListener implements RangeSeekBar.OnRangeSeekBarChangeListener<Integer>
{

    private LiveTeamListAdapter liveListAdapter;
    private final StatusBar statusBar;

    public RangeBarOnClickListener(StatusBar statusBar)
    {
        this.statusBar = statusBar;
    }

    @Override
    public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue)
    {
        // Set's new bounds and adjusts shown time
        statusBar.setMinRange(minValue);
        statusBar.setMaxRange(maxValue);
        statusBar.refreshTimeView();
        // Adjust the list data
        if (liveListAdapter != null)
            liveListAdapter.refreshList(minValue, maxValue);
    }

    public void setLiveListAdapter(LiveTeamListAdapter adapter)
    {
        this.liveListAdapter = adapter;
    }

}
