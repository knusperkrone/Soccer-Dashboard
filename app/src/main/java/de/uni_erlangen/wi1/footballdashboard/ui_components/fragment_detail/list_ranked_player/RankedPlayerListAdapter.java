package de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.list_ranked_player;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.uni_erlangen.wi1.footballdashboard.PlayerTeam;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.opta_api.OPTA_Player;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.PlayerInfoFragment;
import de.uni_erlangen.wi1.footballdashboard.ui_components.fragment_detail.fragments.TeamInfoFragment;

public class RankedPlayerListAdapter extends
        RecyclerView.Adapter<RankedPlayerListAdapter.PlayerViewHolder>
{

    private static final String[] RANKS = {
            " 1. ", " 2. ", " 3. ", " 4. ", " 5. ", " 6. ", " 7. ", " 8. ", " 9. ", "10.", "11."
    };

    private PlayerTeam team;
    private final LayoutInflater inflater;
    private final Context context;
    private final FragmentManager fm;
    private int selectedPosition = -1;
    private final int clicked, def;

    public RankedPlayerListAdapter(FragmentManager fm, Context context, PlayerTeam team)
    {
        super();
        this.team = team;
        this.context = context;
        this.fm = fm;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        clicked = ContextCompat.getColor(context, R.color.card_clicked);
        def = ContextCompat.getColor(context, R.color.card_default);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new PlayerViewHolder(inflater.inflate(R.layout.frag_detail_list_elem, parent, false));
    }

    public boolean changeTeam(PlayerTeam team)
    {
        // Set new team and selected player
        boolean newTeam = team.getId() != this.team.getId();
        OPTA_Player selectedPlayer = null;
        if (newTeam) {
            this.team = team;
            selectedPosition = -1;
        } else if (selectedPosition != -1) {
            selectedPlayer = this.team.getRankedPlayers()[selectedPosition];
        }
        // Reorder list
        team.sortRankedPlayers();
        notifyDataSetChanged();

        // Restore selected player
        if (!newTeam && selectedPlayer != null) {
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

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, final int position)
    {
        final OPTA_Player player = team.getRankedPlayers()[position];
        holder.rank.setText(RANKS[position]);
        holder.name.setText(player.getName());
        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.lloris));
        holder.card.setCardBackgroundColor((selectedPosition == position) ? clicked : def);

        holder.card.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (selectedPosition != -1) // Only one item can be selected
                    notifyItemChanged(selectedPosition);

                if (selectedPosition == position) { // Was already clicked
                    //Show TeamInfo
                    //holder.card.setCardBackgroundColor(def);
                    Log.d("[RankedPlayerListAdap]", "Switching to TeamView");
                    selectedPosition = -1;
                    fm.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.detail_content, TeamInfoFragment.newInstance(team))
                            .commit();
                } else {
                    //Show playerInfo
                    selectedPosition = position;
                    holder.card.setCardBackgroundColor(clicked);
                    Fragment frag = fm.findFragmentById(R.id.detail_content);
                    if (frag instanceof PlayerInfoFragment) { // Already has a existing fragment
                        ((PlayerInfoFragment) frag).setValues(player);
                    } else {
                        // Create new fragment
                        frag = PlayerInfoFragment.newInstance(player);
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
