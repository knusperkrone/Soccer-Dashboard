package de.uni_erlangen.wi1.footballdashboard.ui_components.main_list;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;

/**
 * Created by knukro on 23.08.17.
 */

class ViewHolder
{

    static class ViewHolderEvent extends ParentViewHolder<OPTA_Event, OPTA_Qualifier>
    {
        final TextView text;

        ViewHolderEvent(@NonNull View itemView)
        {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.overviewlist_parent_text);
        }
    }

    static class ViewHolderQualifier extends ChildViewHolder
    {
        final TextView text;

        ViewHolderQualifier(@NonNull View itemView)
        {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.overviewlist_child_text);
        }
    }

}
