package com.radiantkey.daymonitor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HCustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<HistoryContainer> mItemList;

    DatabaseHelper mdb;

    public HCustomAdapter(Context mContext, List<HistoryContainer> mItemList) {
        this.mContext = mContext;
        this.mItemList = mItemList;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return mItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.history_listview_item,null);


        TextView name = v.findViewById(R.id.hist_name);
        TextView date = v.findViewById(R.id.hist_date);
        TextView tLength = v.findViewById(R.id.hist_length);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mItemList.get(i).getStartTime());
        name.setText(mItemList.get(i).getName());
        date.setText(formatter.format(calendar.getTime()));
        long millis = mItemList.get(i).getTimeLength();
        tLength.setText(String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))));

        v.setTag(mItemList.get(i).getId());
        return v;
    }
}
