package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.statistic_fragments.player;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.GameInfo;
import de.uni_erlangen.wi1.footballdashboard.helper.StatisticHelper;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.ActiveView;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;
import de.uni_erlangen.wi1.footballdashboard.ui_components.seekbar.OnSeekBarChangeAble;

/**
 * Created by knukro on 7/4/17.
 * .
 */

public class LineChartFragment extends Fragment implements IPlayerFragment, OnSeekBarChangeAble,
        ActiveView, OnChartGestureListener, OnChartValueSelectedListener
{

    private static final int[] mColors = ColorTemplate.MATERIAL_COLORS;

    private CheckBox[] checkBoxes;
    private OPTA_Player player;
    private OPTA_Team team;
    private int chartIndex = 0;
    private List<GameInfo> teamGames;

    private TextView passCounterView;
    private TextView shootCounterView;
    private TextView tackleCounterView;
    private TextView foulCounterView;
    private TextView totalCounterView;

    private LineChart mChart;


    public static LineChartFragment newInstance(OPTA_Team team, OPTA_Player player)
    {
        LineChartFragment frag = new LineChartFragment();
        frag.player = player;
        frag.team = team;
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final View root = inflater.inflate(R.layout.stat_playerinfo_ranking, container, false);

        initTextView(root);
        initChartView(root);
        initCheckBoxes(root);

        setActive();
        return root;
    }

    @Override
    public void setActive()
    {
        chartIndex = 0;
        if (mChart.getData() != null)
            mChart.clearValues();
        initStatisticData();
        StatusBar.getInstance().registerOnClicked(this);
        mChart.getData().notifyDataChanged();
        mChart.notifyDataSetChanged();
        for (CheckBox cb : checkBoxes)
            cb.setChecked(false);
    }

    @Override
    public void setInactive()
    {
        StatusBar.getInstance().unRegisterOnClicked(this);
    }

    @Override
    public void seekBarChanged(int minVal, int maxVal)
    {
        seekBarChangedChart(maxVal);
        seekBarChangedText(minVal, maxVal);
    }


    private void initStatisticData()
    {
        ArrayList<Entry> values = new ArrayList<>();

        // Add all values (until currTime)
        int maxTime = StatusBar.getInstance().getMaxRange();
        for (Short rank : this.player.playerRankings) {
            int tmp = 11 - rank;
            if (tmp >= 0 || chartIndex == 0)
                values.add(new Entry(chartIndex, rank));
            if (chartIndex * 10 >= maxTime)
                break;
            chartIndex++;
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Current Game");
            set1.setDrawIcons(false);

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setDrawValues(false);
            //set1.setDrawCircles(false);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.BLACK);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);
            // set data
            mChart.setData(data);
        }
    }


    private void addOtherGameStatistic(GameInfo gameInfo)
    {
        OPTA_Team oldTeam = DatabaseAdapter
                .getInstance().getGameDataForGameTeam(getContext(), gameInfo.getID(), team);
        OPTA_Player oldPlayer = oldTeam.getPlayers().get(player.getId());

        LineData data = mChart.getData();

        if (data != null) {

            ArrayList<Entry> values = new ArrayList<>();
            int count = (data.getDataSetCount() + 1);

            int i = 0;
            if (oldPlayer != null) {
                for (Short rank : oldPlayer.playerRankings) {
                    int tmp = 11 - rank;
                    if (tmp >= 0)
                        values.add(new Entry(i, tmp));
                    i++;
                }
            } else {
                Toast.makeText(getActivity(), "[CRITICAL ERROR] Player hasn't any data", Toast.LENGTH_LONG).show();
            }

            if (values.isEmpty())
                values.add(new Entry(i, -1));
            LineDataSet set = new LineDataSet(values, gameInfo.toString());
            set.setLineWidth(2.5f);
            set.setCircleRadius(4.5f);

            int color = mColors[count % mColors.length];

            set.setColor(color);
            set.setCircleColor(color);
            set.setDrawValues(false);
            //set.setDrawCircles(false);
            set.setHighLightColor(color);
            set.setValueTextSize(10f);
            set.setValueTextColor(color);

            data.addDataSet(set);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void removeOtherGameStatistic(GameInfo gameInfo)
    {
        LineData data = mChart.getData();
        for (int i = 0; i < data.getDataSetCount(); i++) {
            ILineDataSet dataSet = data.getDataSetByIndex(i);
            if (dataSet.getLabel().equals(gameInfo.toString())) {
                data.removeDataSet(dataSet);
                break;
            }
        }
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    private void seekBarChangedText(int minVal, int maxVal)
    {
        int[][] countedEvents = new int[5][2];

        StatisticHelper.countPlayerEvents(player, countedEvents, minVal, maxVal);

        StatisticHelper.setText(passCounterView, countedEvents[0]);
        StatisticHelper.setText(shootCounterView, countedEvents[1]);
        StatisticHelper.setText(tackleCounterView, countedEvents[2]);
        StatisticHelper.setText(foulCounterView, countedEvents[3]);
        StatisticHelper.setText(totalCounterView, countedEvents[4]);
    }

    private void seekBarChangedChart(int maxVal)
    {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet dataSet = data.getDataSetByIndex(0);
            // Check if we extended or reduce values
            if (maxVal < chartIndex * 10) {

                // Reset all values and redraw selected graphs
                chartIndex = 0;
                mChart.clearValues();
                initStatisticData();

                for (int i = 0; i < checkBoxes.length; i++)
                    if (checkBoxes[i].isChecked())
                        addOtherGameStatistic(teamGames.get(i));

            } else {

                // Add points until maxVal
                while (chartIndex < player.playerRankings.size()) {
                    if (chartIndex * 10 >= maxVal)
                        break;

                    dataSet.addEntry(new Entry(chartIndex, 11 - player.playerRankings.get(chartIndex)));
                    chartIndex++;
                }

            }

            // Redraw
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.invalidate();
        }
    }

    private void initTextView(View root)
    {
        totalCounterView = (TextView) root.findViewById(R.id.player_overall);
        passCounterView = (TextView) root.findViewById(R.id.player_paesse);
        shootCounterView = (TextView) root.findViewById(R.id.player_shoots);
        tackleCounterView = (TextView) root.findViewById(R.id.player_tackles);
        foulCounterView = (TextView) root.findViewById(R.id.player_fouls);
    }

    private void initCheckBoxes(View root)
    {
        // Init old games Checkboxes
        TableLayout ll = (TableLayout) root.findViewById(R.id.checkboxes);
        teamGames = DatabaseAdapter.getInstance().getGamesForTeam(team);
        checkBoxes = new CheckBox[teamGames.size()];
        int i = 0;
        for (final GameInfo gameInfo : teamGames) {
            // Add checkBox for every game, to add playerData for remove it
            final CheckBox cb = new CheckBox(getContext());
            cb.setText(gameInfo.toString());
            cb.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (cb.isChecked())
                        addOtherGameStatistic(gameInfo);
                    else
                        removeOtherGameStatistic(gameInfo);
                }
            });
            // Save reference and add view
            checkBoxes[i++] = cb;
            ll.addView(cb);
        }
    }

    private void initChartView(View root)
    {
        mChart = (LineChart) root.findViewById(R.id.chart);
        mChart.setOnChartGestureListener(this);
        mChart.setDrawGridBackground(false);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setAxisMaximum(11.1f);
        leftAxis.setAxisMinimum(1f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);
        mChart.animateX(1500);
    }


    @Override
    public void setNewPlayer(OPTA_Player player)
    {
        this.player = player;
    }



    /* ------------------------ Useless interface methods start here ---------------------------- */

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture)
    {
        // un-highlight values after the gesture is finished and no single-tap
        if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
            mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
    }

    @Override
    public void onChartLongPressed(MotionEvent me)
    {
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me)
    {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me)
    {
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY)
    {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY)
    {
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY)
    {
    }

    @Override
    public void onValueSelected(Entry e, Highlight h)
    {
    }

    @Override
    public void onNothingSelected()
    {
    }
}
