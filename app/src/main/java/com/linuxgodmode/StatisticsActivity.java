package com.linuxgodmode;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;

import com.google.common.base.Preconditions;
import com.linuxgodmode.business.LinuxStatistic;

import java.util.List;

import de.lsa.logic.CommandUpdateEvent;
import de.lsa.logic.LinuxCommand;

public class StatisticsActivity extends ListActivity implements CommandUpdateEvent {


    private static StatisticsAdapter statisticsAdapter;
    private final Handler handler = new Handler();

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
        this.handler.post(() -> {
            statisticsAdapter.notifyDataSetChanged();
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.handler.post(() -> {
            statisticsAdapter = new StatisticsAdapter(this);
            setListAdapter(statisticsAdapter);
        });

    }


}
