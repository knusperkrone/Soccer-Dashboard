package de.uni_erlangen.wi1.footballdashboard.UI_Components.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

import de.uni_erlangen.wi1.footballdashboard.R;


/**
 * Created by knukro on 5/22/17.
 */

public class PlayerView extends LinearLayout
{

    private CircularImageView playerImage;
    private TextView playerName;
    private TextView indicatorCaptain;
    private View indicatorCard;


    public PlayerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.view_player, this);
        playerImage = (CircularImageView) findViewById(R.id.playerImage);
        playerName = (TextView) findViewById(R.id.playerName);
        indicatorCaptain = (TextView) findViewById(R.id.playerCaptain);
        indicatorCard = (View) findViewById(R.id.playerCard);

        // Do stuff on click
        playerImage.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //TODO: Do something useful here
                Toast.makeText(getContext(), "Clicked: " + playerName.getText() + ", Captain: false" + ", YellowCard: " + hasYellowCard(), Toast.LENGTH_SHORT).show();
                clearOverLay();
            }
        });
    }


    public void setBorderColor(int colorID)
    {
        playerImage.setBorderColor(ContextCompat.getColor(getContext(), colorID));
    }

    public void setImageOverlay(int colorID)
    {
        playerImage.setColorFilter(ContextCompat.getColor(getContext(), colorID));
    }

    public void clearOverLay() {
        playerImage.setColorFilter(null);
    }

    public void setPlayerImage(@NonNull Drawable drawable)
    {
        try {
            playerImage.setImageDrawable(drawable);
        } catch (Exception e) {
            //playerImage.setImageDrawable(); //TODO: Set default image
        }
    }

    public void setPlayerImage(Bitmap bm)
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

    public boolean isCaptain() {
        return indicatorCaptain.getVisibility() == VISIBLE;
    }

    public void setCaptain() {
        indicatorCaptain.setVisibility(VISIBLE);
    }

    public void setCaptain(boolean on) {
        indicatorCaptain.setVisibility(on ? VISIBLE : GONE);
    }

    public boolean hasYellowCard()
    {
        return indicatorCard.getVisibility() == VISIBLE;
    }

    public void setYellowCard()
    {
        indicatorCard.setVisibility(VISIBLE);
    }

    public void setYellowCard(boolean on)
    {
        indicatorCard.setVisibility(on ? VISIBLE : GONE);
    }

}


