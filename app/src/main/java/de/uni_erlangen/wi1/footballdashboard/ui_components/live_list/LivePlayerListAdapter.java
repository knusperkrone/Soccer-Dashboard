package de.uni_erlangen.wi1.footballdashboard.ui_components.list_live_event;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Team;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.PlayerInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.TeamInfoFragment;

public class LivePlayerListAdapter extends
        RecyclerView.Adapter<LivePlayerListAdapter.PlayerViewHolder>
{

    private static final String[] RANKS = {
            " 1. ", " 2. ", " 3. ", " 4. ", " 5. ", " 6. ", " 7. ", " 8. ", " 9. ", "10.", "11."
    };

    private OPTA_Team team;
    private final LayoutInflater inflater;
    private final Context context;
    private final FragmentManager fm;
    private int selectedPosition = -1;
    private final int clickedColor, defaultColor;
    private OPTA_Player[] rankedPlayers;

    public LivePlayerListAdapter(FragmentManager fm, Context context, OPTA_Team team)
    {
        super();
        this.team = team;
        this.context = context;
        this.fm = fm;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        clickedColor = ContextCompat.getColor(context, R.color.card_clicked);
        defaultColor = ContextCompat.getColor(context, R.color.card_default);
    }

    public boolean changedTeam(OPTA_Team team)
    {
        // Set new team and selected player
        boolean newTeam = team.getId() != this.team.getId();

        OPTA_Player selectedPlayer = null;
        if (newTeam) {
            // If team changed, reset the selected state!
            this.team = team;
            selectedPosition = -1;

        } else if (selectedPosition != -1) {
            // There is a selected player, but position in array will be changed after refresh
            selectedPlayer = rankedPlayers[selectedPosition];
        }

        // Get current sorted list and refresh view
        rankedPlayers = team.getRankedPlayers();
        notifyDataSetChanged();

        // Restore selected player, if there was one and the team didn't change!
        if (!newTeam && selectedPlayer != null) {
            // Linear search for the selected player
            int i = 0;
            for (OPTA_Player player : team.getRankedPlayers()) {
                if (selectedPlayer.getId() == player.getId()) {
                    selectedPosition = i;
                    break;
                }
                i++;
            }
        }
        return newTeam;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new PlayerViewHolder(inflater.inflate(R.layout.frag_detail_list_elem, parent, false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, final int position)
    {
        if (rankedPlayers == null) // On first run there aren't any players set!
            return;

        // Get player and set values
        final OPTA_Player player = rankedPlayers[position];
        holder.rank.setText(RANKS[position]);
        holder.name.setText(player.getName());
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.lloris)); // TODO: Set picture dynamically
        // Set Color according to click state
        holder.card.setCardBackgroundColor((selectedPosition == position) ? clickedColor : defaultColor);
        // Adjust onClickListener
        holder.card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // Only one item can be selected, so
                if (selectedPosition != -1)
                    notifyItemChanged(selectedPosition);

                if (selectedPosition == position) {
                    // This view is already clicked -> show TeamInfoFragment again
                    selectedPosition = -1;
                    fm.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.detail_content, TeamInfoFragment.newInstance(team))
                            .commit();

                } else {
                    // This view just got clicked -> show (new|old) playerInfo Fragment
                    // Mark this viewHolder as clicked
                    selectedPosition = position;
                    holder.card.setCardBackgroundColor(clickedColor);

                    // Get current shown Fragment
                    Fragment frag = fm.findFragmentById(R.id.detail_content);
                    if (frag instanceof PlayerInfoFragment) {
                        // There is already a playerFragment, adjust the values
                        ((PlayerInfoFragment) frag).setValues(player, team.getId());
                    } else {
                        // TeamFragment is shown, replace with PlayerFragment
                        frag = PlayerInfoFragment.newInstance(player, team.getId());
                        fm.beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.detail_content, frag).commit();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return 11;
    }

    // ViewHolder Class
    static class PlayerViewHolder extends RecyclerView.ViewHolder
    {
        final CardView card;
        final TextView rank, name;
        final ImageView imageView;

        PlayerViewHolder(View itemView)
        {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.detail_list_card);
            rank = (TextView) itemView.findViewById(R.id.detail_list_rank);
            name = (TextView) itemView.findViewById(R.id.detail_list_name);
            imageView = (ImageView) itemView.findViewById(R.id.detail_list_image);
        }
    }
}
