package de.uni_erlangen.wi1.footballdashboard.ui_components;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.uni_erlangen.wi1.footballdashboard.MainActivity;
import de.uni_erlangen.wi1.footballdashboard.R;
import de.uni_erlangen.wi1.footballdashboard.database_adapter.DatabaseAdapter;

/**
 * Created by knukro on 23.08.17.
 */

public class GamePicker extends DialogFragment
{

    private final static String SETTING_KEY = "gameID";

    private NumberPicker picker;
    private final List<String> gameNames = new ArrayList<>(128);
    private final List<Integer> gameIds = new ArrayList<>(128);


    @NonNull
    @SuppressWarnings("ConstantConditions")
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View pickerView = inflater.inflate(R.layout.dialog_game_spinner, null);
        initPicker(pickerView, sp);
        builder.setView(pickerView);

        builder.setMessage("App will restart after your choice:")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        int newId = gameIds.get(picker.getValue());


                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(SETTING_KEY, newId + "");

                        if (edit.commit())
                            triggerRebirth(getActivity());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        Dialog diag = builder.create();
        diag.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return diag;
    }


    private void initPicker(View view, SharedPreferences sp)
    {
        DatabaseAdapter.getInstance().getAllGames(gameNames, gameIds);
        picker = (NumberPicker) view.findViewById(R.id.game_picker);
        picker.setMaxValue(gameNames.size() - 1);
        picker.setDisplayedValues(gameNames.toArray(new String[gameNames.size()]));


        int currId = Integer.valueOf(sp.getString(SETTING_KEY, "838532"));
        for (int i = 0; i < gameIds.size(); i++) {
            if (gameIds.get(i) == currId) {
                picker.setValue(i);
                break;
            }
        }

    }

    private static void triggerRebirth(Context context)
    {
        Toast.makeText(context, "App get's restarted..", Toast.LENGTH_LONG).show(); // Well..

        Intent mStartActivity = new Intent(context, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity,
                PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1, mPendingIntent);
        System.exit(0);
    }

}
