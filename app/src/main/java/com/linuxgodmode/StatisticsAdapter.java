package com.linuxgodmode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.linuxgodmode.business.LinuxStatistic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import de.lsa.logic.LinuxCommand;

/**
 * Created by sandro on 15.04.17 for project androidapp.
 */

public class StatisticsAdapter extends BaseAdapter {

    private final LayoutInflater inflator;
    private final Map<LinuxCommand, LinuxStatistic> linuxStatistics = new LinkedHashMap<>();

    public StatisticsAdapter(Context context) {
        this.inflator = LayoutInflater.from(context);
        this.initLinuxStatistics();
    }

    private void initLinuxStatistics() {
        this.linuxStatistics.put(LinuxCommand.CPU_USAGE, new LinuxStatistic(R.drawable.cpu_usage, R.string.cpu_usage, "0.0", "%"));
        this.linuxStatistics.put(LinuxCommand.HOSTNAME, new LinuxStatistic(R.drawable.hostname, R.string.hostname, "...", ""));
        this.linuxStatistics.put(LinuxCommand.TEMPERATURE, new LinuxStatistic(R.drawable.temperature, R.string.temperature, "0.0", "°C"));
        this.linuxStatistics.put(LinuxCommand.UPTIME, new LinuxStatistic(R.drawable.uptime, R.string.uptime, "0", "m"));
        this.linuxStatistics.put(LinuxCommand.DATE, new LinuxStatistic(R.drawable.date, R.string.date, "...", ""));
        this.linuxStatistics.put(LinuxCommand.LANGUAGE, new LinuxStatistic(R.drawable.language, R.string.language, "...", ""));
        this.linuxStatistics.put(LinuxCommand.TRAFFIC, new LinuxStatistic(R.drawable.traffic, R.string.traffic, "0.0  0.0", "kb/s"));

    }

    @Override
    public int getCount() {
        return this.linuxStatistics.size();
    }

    @Override
    public Object getItem(int position) {
        Collection values = this.linuxStatistics.values();
        return (new ArrayList<String>(values)).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position,
                        View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflator.inflate(R.layout.activity_statistics,
                    parent, false);
            holder = new ViewHolder();
            holder.name =
                    (TextView) convertView.findViewById(R.id.statisticName);
            holder.value = (TextView) convertView
                    .findViewById(R.id.statisticValue);
            holder.icon =
                    (ImageView) convertView.findViewById(R.id.statisticIcon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Context context = parent.getContext();
        LinuxStatistic linuxStatistic = (LinuxStatistic) getItem(position);
        holder.name.setText(linuxStatistic.getName(context));
        holder.icon.setImageResource(linuxStatistic.getIcon());
        holder.value.setText(linuxStatistic.getValue() + " " + linuxStatistic.getUnit());
        return convertView;
    }

    public LinuxStatistic getLinuxStatisticForCommand(LinuxCommand command) {
        Preconditions.checkNotNull(command, "The linuxCommand cannot be null");
        return this.linuxStatistics.get(command);
    }

    public void updateLinuxStatistic(LinuxStatistic linuxStatistic, LinuxCommand linuxCommand) {
        Preconditions.checkNotNull(linuxCommand, "The linuxCommand cannot be null");
        Preconditions.checkNotNull(linuxCommand, "The linuxStatistic cannot be null");
        this.linuxStatistics.put(linuxCommand, linuxStatistic);
    }

    static class ViewHolder {
        TextView name, value;
        ImageView icon;
    }
}
