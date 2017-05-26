package de.uni_erlangen.wi1.footballdashboard.UI_Components.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.UI_Components.Views.PlayerView;


public class FormationFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";

    private int layoutId;
    private PlayerView[] players;


    public FormationFragment() {
        players = new PlayerView[11];
    }

    public static Fragment newInstance(int layoutId)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layoutId);

        FormationFragment tmp = new FormationFragment();
        tmp.setArguments(args);
        return tmp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null)
            layoutId = args.getInt(ARG_PARAM1, R.layout.formation2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        final View root = inflater.inflate(layoutId, container, false);

        // Init players
        players[0] = (PlayerView) root.findViewById(R.id.p1);
        players[1] = (PlayerView) root.findViewById(R.id.p2);
        players[2] = (PlayerView) root.findViewById(R.id.p3);
        players[3] = (PlayerView) root.findViewById(R.id.p4);
        players[4] = (PlayerView) root.findViewById(R.id.p5);
        players[5] = (PlayerView) root.findViewById(R.id.p6);
        players[6] = (PlayerView) root.findViewById(R.id.p7);
        players[7] = (PlayerView) root.findViewById(R.id.p8);
        players[8] = (PlayerView) root.findViewById(R.id.p9);
        players[9] = (PlayerView) root.findViewById(R.id.p10);
        players[10] = (PlayerView) root.findViewById(R.id.p11);

        // JUST ADD SOME COLORS
        for (PlayerView player : players) {
            setPlayerColors(player);
        }
        players[rand.nextInt(11)].setCaptain();

        return root;
    }

    private static Random rand = new Random(1999);

    private void setPlayerColors(PlayerView player)
    {
        // TODO: Make thie based on data not random numbers!
        int dice = rand.nextInt(3);

        int borderColor;
        if (dice == 0) { //good
            borderColor = R.color.good_perf;
            if (rand.nextBoolean())
                player.setImageOverlay(R.color.best_perf);
        } else if (dice == 1) { //average
            borderColor = R.color.average_perf;
        } else { //bad
            borderColor = R.color.bad_perf;
            if (rand.nextBoolean())
                player.setImageOverlay(R.color.worst_perf);
        }
        player.setBorderColor(borderColor);

        if(rand.nextInt(100) < 15)
            player.setYellowCard();
    }

}
