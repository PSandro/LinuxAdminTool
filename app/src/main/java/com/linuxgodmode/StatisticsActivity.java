package com.linuxgodmode;

import android.app.ListActivity;
import android.os.Bundle;

import com.google.common.base.Preconditions;
import com.linuxgodmode.business.LinuxStatistic;

import java.util.List;

import de.lsa.logic.CommandUpdateEvent;
import de.lsa.logic.LinuxCommand;

public class StatisticsActivity extends ListActivity implements CommandUpdateEvent {


    private static StatisticsAdapter statisticsAdapter;

    @Override
    public void update(LinuxCommand linuxCommand, long l, List<String> list) {
        Preconditions.checkNotNull(list, "The list cannot be null");
        if (linuxCommand == null || statisticsAdapter == null) return;

        LinuxStatistic linuxStatistic = statisticsAdapter.getLinuxStatisticForCommand(linuxCommand);
        if (linuxStatistic == null) {
            System.out.println("No linuxStatistic found for command " + linuxCommand.name());
            return;
        }

        String answer;
        if (list.size() != 0 && list.get(0) != null) {
            answer = list.get(0);
            if (answer.contains("command not found")) return;
        } else {
            answer = linuxStatistic.getEmptyValue();
        }


        linuxStatistic.setValue(answer);
        runOnUiThread(() -> {
            statisticsAdapter.notifyDataSetChanged();
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runOnUiThread(() -> {
            statisticsAdapter = new StatisticsAdapter(this);
            setListAdapter(statisticsAdapter);
        });

    }


}
