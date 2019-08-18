package com.radiantkey.daymonitor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<ItemContainer> mItemList;

    DatabaseHelper mdb;

    public CustomAdapter(Context mContext, List<ItemContainer> mItemList) {
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
        View v = View.inflate(mContext, R.layout.listview_item,null);
        TextView itemName = (TextView) v.findViewById(R.id.itemName);
        TextView catName = (TextView) v.findViewById(R.id.catName);
        Switch onSwitch = (Switch) v.findViewById(R.id.onSwitch);

        itemName.setText(mItemList.get(i).getName());
        catName.setText(mItemList.get(i).getCat());
        onSwitch.setChecked(mItemList.get(i).getSwitchState());

        onSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                View parentView = ((View) compoundButton.getParent());
                ListView tempLV = (ListView) parentView.getParent();
                int position = tempLV.getPositionForView(parentView);
                ItemContainer tContainer = (ItemContainer) getItem(position);
//                Log.w("asdfsfd", " " + tContainer.getName());

                mdb = new DatabaseHelper(parentView.getContext());
                tContainer.setSwitchState(b);
                if(b){
                    tContainer.setStartTime(Calendar.getInstance().getTimeInMillis());
                }else{
                    //add new entry to history
                    Toast.makeText(mContext, "history data inserted", Toast.LENGTH_LONG).show();
                    mdb.insertDataH(tContainer.getName(), tContainer.getCat(), tContainer.getStartTime(), Calendar.getInstance().getTimeInMillis() - tContainer.getStartTime());
                }
                mdb.updateData(tContainer.getId(), tContainer.getName(), tContainer.getCat(), tContainer.getSwitchState(), tContainer.getStartTime());
            }
        });

        v.setTag(mItemList.get(i).getId());
        return v;
    }
}
