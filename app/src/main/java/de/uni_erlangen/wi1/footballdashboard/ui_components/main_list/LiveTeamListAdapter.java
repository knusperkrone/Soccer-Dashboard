package de.uni_erlangen.wi1.footballdashboard.ui_components.main_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;

import java.util.LinkedList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Event;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Qualifier;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_overview.EventParser;

/**
 * Created by knukro on 6/16/17.
 */

public class LiveTeamListAdapter extends ExpandableRecyclerAdapter<OPTA_Event, OPTA_Qualifier, LiveTeamListAdapter.ViewHolderEvent, LiveTeamListAdapter.ViewHolderQualifier>
{

    private final LayoutInflater inflater;
    private final RecyclerView parent;
    private ILiveFilter filter;
    private int teamId;

    private EventParser parser;

    LiveTeamListAdapter(@NonNull Context context, RecyclerView parent)
    {
        super(new LinkedList<OPTA_Event>());
        this.parent = parent;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolderEvent onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new ViewHolderEvent(inflater.inflate(R.layout.frag_overview_list_parent, viewGroup, false));
    }

    @NonNull
    @Override
    public ViewHolderQualifier onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        return new ViewHolderQualifier(inflater.inflate(R.layout.frag_overview_list_child, viewGroup, false));
    }

    @Override
    public void onBindParentViewHolder(@NonNull ViewHolderEvent viewHolderEvent, int i, @NonNull OPTA_Event eventInfo)
    {
        viewHolderEvent.text.setText(eventInfo.getHRTime() + ": " + eventInfo.getDescription());
    }

    @Override
    public void onBindChildViewHolder(@NonNull ViewHolderQualifier viewHolderQualifier, int i, int i1, @NonNull OPTA_Qualifier qualifier)
    {
        viewHolderQualifier.text.setText(qualifier.getValue());
    }

    void teamChanged(ILiveFilter filter, int teamId, int fromSec, int toSec)
    {
        this.filter = filter;
        this.teamId = teamId;
        refreshList(fromSec, toSec);
    }

    public void prePendEventInfo(OPTA_Event info)
    {
        if (filter != null && !filter.isValid(info))
            return;

        if (parser != null)
            parser.addActiveEvent(info);

        int scrollOffset = parent.computeVerticalScrollOffset();
        getParentList().add(0, info); // Insert in list
        notifyParentInserted(0);  // Insert in UI

        if (scrollOffset < 20) // Is the user scrolling?
            parent.scrollToPosition(0);
    }

    public void refreshList(int fromSec, int toSec)
    {
        List<OPTA_Event> refreshed =
                DatabaseAdapter.getInstance().getLiveListData(teamId, fromSec, toSec);

        if (parser != null)
            parser.setActiveEvents(refreshed);

        setParentList(refreshed, false);
        notifyDataSetChanged();
        parent.scrollToPosition(0);
    }


    /*ViewHolder!*/
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
