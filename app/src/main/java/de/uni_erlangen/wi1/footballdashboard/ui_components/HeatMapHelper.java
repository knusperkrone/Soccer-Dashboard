package de.uni_erlangen.wi1.footballdashboard.ui_components;

import android.support.annotation.NonNull;

import ca.hss.heatmaplib.HeatMap;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;

/**
 * Created by knukro on 6/20/17.
 */

public class HeatMapHelper
{

    public static void addDataPointsToHeatmap(@NonNull OPTA_Player player, @NonNull HeatMap heatMap,
                                              boolean homeTeam)
    {
        for (OPTA_Event info : player.getActions()) {
            if (info.getCRTime() > StatusBar.getInstance().currTime)
                break; // Nothing to do here

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

            HeatMap.DataPoint point = new HeatMap.DataPoint(x, y, 40.0);
            heatMap.addData(point);
        }
    }


}
