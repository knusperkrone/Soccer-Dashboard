package de.uni_erlangen.wi1.footballdashboard.helper;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import java.util.Map;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.StatusBar;

/**
 * Created by knukro on 6/20/17.
 */

public class HeatMapHelper
{

    private static final double DEFAULT_POINT_VAL = 40;
    private static final double MIDDLE_POINT_VAL = 30;
    private static final double SMALL_POINT_VAL = 25;

    public static void setupHeatMap(@NonNull HeatMap heatMap)
    {
        heatMap.setMinimum(10.0);
        heatMap.setMaximum(100.0);
        Map<Float, Integer> colors = new ArrayMap<>();
        colors.put(0.0f, 0xffeef442);
        colors.put(1.0f, 0xff0000);
        heatMap.setColorStops(colors);
    }

    public static void drawCurrentHeatmap(@NonNull OPTA_Player player, @NonNull HeatMap heatMap,
                                          boolean homeTeam)
    {
        heatMap.clearData();
        StatusBar bar = StatusBar.getInstance();
        addDataPointsToHeatmap(player, heatMap, homeTeam, bar.getMinRange(), bar.getMaxRange());
        heatMap.forceRefresh();
    }

    private static void addDataPointsToHeatmap(@NonNull OPTA_Player player, @NonNull HeatMap heatMap,
                                               boolean homeTeam, int startTime, int endTime)
    {
        addDataPointsToHeatmap(player, heatMap, homeTeam, startTime, endTime,
                evaluatePointValue(startTime, endTime));
    }

    public static void addDataPointsToHeatmap(@NonNull OPTA_Player player, @NonNull HeatMap heatMap,
                                              boolean homeTeam, int startTime, int endTime, double val)
    {
        for (OPTA_Event info : player.getActions()) {
            if (info.getCRTime() < startTime)
                continue; // Not quite there yet

            if (info.getCRTime() > endTime)
                break; // That is to much now

            float x = Double.valueOf(info.x / 100).floatValue();
            float y = Double.valueOf(info.y / 100).floatValue();

            // Inverse x/y
            if (!homeTeam) x = 1.0f - x;
            else y = 1.0f - y;

            // "Idealize" points
            if (x > 0.8) x -= 0.05f;
            else if (x < 0.2) x += 0.05f;
            if (y > 0.8) y -= 0.1f;
            else if (y < 0.2) y += 0.1f;

            HeatMap.DataPoint point = new HeatMap.DataPoint(x, y, val);
            heatMap.addData(point);
        }
    }

    public static double evaluatePointValue(int startTime, int endTime)
    {
        if (endTime - startTime < 40 * 60)
            return HeatMapHelper.DEFAULT_POINT_VAL;
        else if (endTime - startTime < 70 * 60)
            return HeatMapHelper.MIDDLE_POINT_VAL;
        else
            return HeatMapHelper.SMALL_POINT_VAL;
    }


}
