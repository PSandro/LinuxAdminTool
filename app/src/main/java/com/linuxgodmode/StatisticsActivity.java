package com.linuxgodmode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.lsa.logic.CommandUpdateEvent;
import de.lsa.logic.LinuxCommand;

public class StatisticsActivity extends AppCompatActivity implements CommandUpdateEvent {


    public static Map<LinuxCommand, TextView> statisticTextViews = new HashMap<>();

    @Override
    public void update(LinuxCommand linuxCommand, long l, List<String> list) {
        Preconditions.checkNotNull(list, "The list cannot be null");

        if (!statisticTextViews.containsKey(linuxCommand)) return;

        TextView textView = statisticTextViews.get(linuxCommand);

        if (list.size() < 1) {
            System.out.println("answer resut size was lower/equal null");
            return;
        }

        if (textView == null) return;

        String answer = list.get(0);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(answer);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statisticTextViews.put(LinuxCommand.CPU_USAGE, (TextView) findViewById(R.id.cpuUsagetextView));
                statisticTextViews.put(LinuxCommand.HOSTNAME, (TextView) findViewById(R.id.hostnametextView));
                statisticTextViews.put(LinuxCommand.TEMPERATURE, (TextView) findViewById(R.id.temptextView));
                statisticTextViews.put(LinuxCommand.UPTIME, (TextView) findViewById(R.id.uptimetextView));
                statisticTextViews.put(LinuxCommand.DATE, (TextView) findViewById(R.id.dateTextView));
                statisticTextViews.put(LinuxCommand.LANGUAGE, (TextView) findViewById(R.id.languagetextView));
                statisticTextViews.put(LinuxCommand.TRAFFIC, (TextView) findViewById(R.id.traffictextView));
            }
        });


    }


}
