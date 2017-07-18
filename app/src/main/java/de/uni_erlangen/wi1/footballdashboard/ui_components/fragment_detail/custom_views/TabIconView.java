package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.custom_views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import de.uni_erlangen.wi1.footballdashboard.R;

/**
 * Created by knukro on 5/24/17.
 */

public class TabIconView extends LinearLayout
{

    public TabIconView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        inflate(getContext(), R.layout.view_tabicon, this);
    }

}
