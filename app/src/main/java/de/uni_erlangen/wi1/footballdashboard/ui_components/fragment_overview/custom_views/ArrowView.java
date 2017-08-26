package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;

/**
 * Created by knukro on 6/28/17
 * .
 */


public class ArrowView extends View
{

    private static final int[] mColors = {
            Color.parseColor("#c51162"), Color.parseColor("#f50057"), Color.parseColor("#aa00ff"),
            Color.parseColor("#651fff"), Color.parseColor("#3d5afe"), Color.parseColor("#ff6434"),
            Color.parseColor("#00b0ff"), Color.parseColor("#1de9b6"), Color.parseColor("#c6ff00"),
            Color.parseColor("#ffc400"), Color.parseColor("#ff9100"), Color.BLACK
    };

    // Holder for the X/Y coordinates
    private final static int[] startCoords = new int[2];
    private final static int[] endCoords = new int[2];
    // Graphic stuff
    private final static Paint linePaint = new Paint();
    private final static Paint headPaint = new Paint();

    private int maxLineSize;
    private int lineSizeFactor;

    private PlayerView startView; // The selected player
    private PlayerView[] players; // The other players

    private SparseArray passCounter; // Holds the amount of passes between @startView and @players
    private boolean centerOut; // Draw centerIn or centerOut flag

    public ArrowView(Context context)
    {
        super(context);
        init(context, startView, players, passCounter, centerOut);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(context, startView, players, passCounter, centerOut);
    }

    public ArrowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, startView, players, passCounter, centerOut);
    }

    public ArrowView(@NonNull Context context, @NonNull PlayerView startView,
                     @NonNull PlayerView[] players, @NonNull SparseArray passCounter,
                     boolean centerOut)
    {
        super(context);
        init(context, startView, players, passCounter, centerOut);
    }

    private void init(Context context, PlayerView startView, PlayerView[] players, SparseArray passCounter, boolean centerOut)
    {
        this.startView = startView;
        this.players = players;
        this.passCounter = passCounter;
        this.centerOut = centerOut;

        getMaxLineSize(context);

        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
        headPaint.setStyle(Paint.Style.FILL);
        headPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        if (centerOut)
            drawCenterOut(canvas); // Draw from the middle ( <- player ->)
        else
            drawCenterIn(canvas); // Draw to the middle (-> player <-)
    }

    private void drawCenterOut(Canvas canvas)
    {
        // Get the middle of the selected View
        startView.getMiddleCoords(startCoords);

        int i = 0; // Counter variable
        for (PlayerView endView : players) {
            // Only draw for other views and there was at least a single Pass
            if (endView == startView || setStrokeCount(endView.getMappedPlayer(), i++) == 0)
                continue;

            // Get Coordinates of playerImage-Border
            endView.getBorderCoords(endCoords, startCoords);
            // Draw arrow
            drawHead(canvas, startCoords[0], startCoords[1], endCoords[0], endCoords[1]);
        }
    }

    private void drawCenterIn(Canvas canvas)
    {
        int i = 0; // Counter variable
        for (PlayerView endView : players) {
            // Only draw for other views and there was at least 1 Pass
            if (endView == startView || setStrokeCount(endView.getMappedPlayer(), i++) == 0)
                continue;


            // Get Coordinates of Center
            endView.getMiddleCoords(startCoords);
            // Get Coordinates of Border
            startView.getBorderCoords(endCoords, startCoords);

            // Draw arrow
            drawHead(canvas, startCoords[0], startCoords[1], endCoords[0], endCoords[1]);
        }
    }

    @SuppressWarnings("unchecked")
    private int setStrokeCount(OPTA_Player player, int index)
    {
        // Extract the amount of passes, @player has done
        int count = (int) passCounter.get(player.getId(), 0);
        if (count > maxLineSize)
            count = maxLineSize;

        linePaint.setStrokeWidth((float) count * lineSizeFactor);

        // Choose color
        int color = mColors[index % mColors.length];
        linePaint.setColor(color);
        headPaint.setColor(color);

        return count;
    }

    private void drawHead(Canvas canvas, float from_x, float from_y, float to_x, float to_y)
    {
        float angle, anglerad, radius, lineangle, offset;

        // Values to change for other appearance
        radius = 20;
        angle = 25;
        offset = 15;

        // Adjust arrowHead Size
        while (radius - linePaint.getStrokeWidth() < 15)
            radius += 10;

        // Adjust arrowHead Offset
        if (linePaint.getStrokeWidth() >= 9)
            offset += 5;

        // Some angle calculations
        anglerad = (float) (Math.PI * angle / 180.0f);
        lineangle = (float) (Math.atan2(to_y - from_y, to_x - from_x));

        // Draw line
        canvas.drawLine(from_x, from_y, to_x, to_y, linePaint);

        // Setup arrowHead
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        // Calc ArrowHead offset
        to_x = Double.valueOf(to_x + offset * Math.cos(lineangle)).floatValue();
        to_y = Double.valueOf(to_y + offset * Math.sin(lineangle)).floatValue();

        // Draw ArrowHead
        path.moveTo(to_x, to_y);
        path.lineTo((float) (to_x - radius * Math.cos(lineangle - (anglerad / 2.0))),
                (float) (to_y - radius * Math.sin(lineangle - (anglerad / 2.0))));
        path.lineTo((float) (to_x - radius * Math.cos(lineangle + (anglerad / 2.0))),
                (float) (to_y - radius * Math.sin(lineangle + (anglerad / 2.0))));
        path.close();

        canvas.drawPath(path, headPaint);
    }


    private void getMaxLineSize(Context context)
    {
        int screenSize = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        // TODO: Play with the values
        switch (screenSize) {

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                this.maxLineSize = 9;
                this.lineSizeFactor = 2;
                break;

            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                this.maxLineSize = 9;
                this.lineSizeFactor = 1;
                break;

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                this.maxLineSize = 9;
                this.lineSizeFactor = 1;
                break;
        }
    }

}