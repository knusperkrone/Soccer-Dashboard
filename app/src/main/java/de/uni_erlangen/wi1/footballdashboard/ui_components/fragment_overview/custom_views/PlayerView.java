package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.custom_views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;


/**
 * Created by knukro on 5/22/17.
 */

public class PlayerView extends LinearLayout
{

    Paint paint = new Paint();

    public CircularImageView playerImage;
    private TextView playerName;
    private TextView indicatorCaptain;
    private View indicatorCard;
    private boolean isClickedMode = false;
    private boolean isBest = false;
    private boolean isWorst = false;
    private int currPoints;

    private OPTA_Player mappedPlayer;

    public PlayerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.view_player, this);
        paint.setColor(Color.BLACK);
        playerImage = (CircularImageView) findViewById(R.id.playerImage);
        playerName = (TextView) findViewById(R.id.playerName);
        indicatorCaptain = (TextView) findViewById(R.id.playerCaptain);
        indicatorCard = findViewById(R.id.playerCard);
    }

    public void setMappedPlayer(OPTA_Player OPTAPlayer)
    {
        this.mappedPlayer = OPTAPlayer;
        setCaptain(mappedPlayer.isCaptain());
        playerName.setText(OPTAPlayer.getName());
        updateColorOverlay();
    }

    public void setDefaultMode()
    {
        isClickedMode = false;
        updateColorOverlay();
    }

    public void setClickedMode()
    {
        isClickedMode = true;
        clearOverLay();
    }

    public void updateCircleLight()
    {
        int oldPoints = currPoints;
        currPoints = mappedPlayer.getRankingPoints();
        if (oldPoints >= 70 && currPoints < 70 ||
                oldPoints <= 40 && currPoints > 40 ||
                oldPoints > 40 && oldPoints < 70 && (currPoints <= 40 || currPoints >= 70))
            updateColorOverlay();
    }

    private void updateColorOverlay()
    {
        int points = mappedPlayer.getRankingPoints();
        if (points >= 70)
            setImageOverlay(R.color.good_perf);
        else if (points <= 40)
            setImageOverlay(R.color.bad_perf);
        else
            setImageOverlay(R.color.average_perf);
    }

    public void setTop()
    {
        if (!isBest)
            setBorderColor(R.color.best_perf);
        isBest = true;
        isWorst = false;
    }

    public void setAverage()
    {
        if (isBest || isWorst)
            setBorderColor(R.color.default_border_color);
        isBest = isWorst = false;
    }

    public void setBad()
    {
        if (!isWorst)
            setBorderColor(R.color.worst_perf);
        isWorst = true;
        isBest = false;
    }

    public int[] getBorderCoords(@NonNull int[] thisVector, int[] inVector)
    {
        // Get Coordinates of playerImage center
        getMiddleCoords(thisVector);
        // Get size of playerImage - offset for arrowHead
        double circleWidth = playerImage.getWidth() / 2 - 10;
        // Calculate angle between @thisVector and @inVector
        double angle = getAngle(inVector, thisVector);

        // Don't overdraw the target playerImage
        if (angle < 180)
            circleWidth -= playerImage.getWidth();

        // Save new x/y coordinates
        double circleX = thisVector[0] + circleWidth * Math.cos(angle);
        double circleY = thisVector[1] + circleWidth * Math.sin(angle);
        thisVector[0] = (int) Math.round(circleX);
        thisVector[1] = (int) Math.round(circleY);

        return thisVector;
    }

    public int[] getMiddleCoords(@NonNull int[] tmp)
    {
        playerImage.getLocationOnScreen(tmp);
        // TODO: Maybe calculate Center?
        tmp[0] -= 115;
        tmp[1] += 20;
        return tmp;
    }

    private void setBorderColor(int colorID)
    {
        playerImage.setBorderColor(ContextCompat.getColor(getContext(), colorID));
    }

    private void setImageOverlay(int colorID)
    {
        playerImage.setColorFilter(ContextCompat.getColor(getContext(), colorID));
    }

    private void clearOverLay()
    {
        playerImage.setColorFilter(null);
    }

    private void setPlayerImage(@NonNull Drawable drawable)
    {
        try {
            playerImage.setImageDrawable(drawable);
        } catch (Exception e) {
            //playerImage.setImageDrawable(); //TODO: Set default image
        }
    }

    private void setPlayerImage(@Nullable Bitmap bm)
    {
        if (bm == null) {
            //playerImage.setImageDrawable(); //TODO: Set default image
        } else {
            try {
                playerImage.setImageBitmap(bm);
            } catch (Exception e) {
                //playerImage.setImageDrawable(); //TODO: Set default image
            }
        }
    }

    public boolean isClickedMode()
    {
        return isClickedMode;
    }

    private void setCaptain(boolean on)
    {
        indicatorCaptain.setVisibility(on ? VISIBLE : GONE);
    }

    private void setYellowCard(boolean on)
    {
        indicatorCard.setVisibility(on ? VISIBLE : GONE);
    }

    public OPTA_Player getMappedPlayer()
    {
        return mappedPlayer;
    }


    // Helper to calculate angles
    private static double getAngle(int[] start, int[] target)
    {
        int deltaY = target[1] - start[1];
        int deltaX = target[0] - start[0];

        // Trunk angle
        double a = Math.atan2(deltaY, deltaX);

        while (a < 0.0) {
            a += Math.PI * 2;
        }
        return a;
    }

}


