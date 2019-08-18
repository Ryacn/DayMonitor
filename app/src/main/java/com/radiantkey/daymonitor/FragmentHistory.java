package com.radiantkey.daymonitor;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FragmentHistory extends Fragment {
    ListView histView;
    private List<HistoryContainer> mList;
    private HCustomAdapter adapter;

    DatabaseHelper mdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        histView = (ListView) view.findViewById(R.id.historyView);
        setupHistory(view.getContext());
    }

    private void setupHistory(final Context context){
        mdb = new DatabaseHelper(this.getContext());
        mList = new ArrayList<>();
        //set list int id, String name, String cat, boolean switchState
        loadAll();

        adapter = new HCustomAdapter(context, mList);
        histView.setAdapter(adapter);

        histView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "clicked product id = " + ((TextView) view.findViewById(R.id.hist_name)).getText(), Toast.LENGTH_SHORT).show();
            }
        });
        histView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(context, "clicked product id = " + view.getTag(), Toast.LENGTH_SHORT).show();
                //deleting data
                deleteData(mList.get(i).getId(), i);
                return true;
            }
        });
    }

    public void deleteData(long id, int i){
        if(mdb.deleteDataH(id) > 0) {
            mList.remove(i);
            adapter.notifyDataSetChanged();
            Toast.makeText(this.getContext(), "data deleted", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.getContext(), "data not deleted", Toast.LENGTH_LONG).show();
        }
    }

    public void loadAll(){
        Cursor res = mdb.getAllDataH();
        if(res.getCount() == 0){
            //Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
            showMessage("Error", "No data found");
        }else{
//            HISTORY database column: ID, NAME, CATEGORY, START_TIME, LENGTH
            while(res.moveToNext()){
                mList.add(new HistoryContainer(res.getLong(0), res.getString(1), res.getString(2), res.getLong(3), res.getLong(4)));
            }
        }
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
