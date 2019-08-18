package com.radiantkey.daymonitor;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

public class FragmentRecord extends Fragment implements RecordDialog.RecordDialogListener {

    private ListView eventView;
    private CustomAdapter adapter;
    private List<ItemContainer> mList;

    private FloatingActionButton addButton;

    DatabaseHelper mdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        eventView = (ListView) view.findViewById((R.id.TimeRecorder));
        addButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        setupRecord(view.getContext());
    }

    private void setupRecord(final Context context){
        mdb = new DatabaseHelper(this.getContext());

        mList = new ArrayList<>();
        //set list int id, String name, String cat, boolean switchState
        loadAll();

        adapter = new CustomAdapter(context, mList);
        eventView.setAdapter(adapter);

        eventView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long viewId = view.getId();
                if(viewId == R.id.onSwitch){
                    Toast.makeText(context, "switch", Toast.LENGTH_SHORT).show();
                }else {
                    //maybe add alertdialog to change names?
                    Toast.makeText(context, "clicked product id = " + ((TextView) view.findViewById(R.id.itemName)).getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        eventView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //deleting data
                deleteData(mList.get(i).getId(), i);
                return true;
            }
        });
    }

    public void openDialog(){
        RecordDialog dialog = new RecordDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "example");
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void loadAll(){
        Cursor res = mdb.getAllData();
        if(res.getCount() == 0){
            //Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
            showMessage("Error", "No data found");
            return;
        }else{
//            Record database column: ID,NAME,CATEGORY,SWITCH_STATE,START_TIME
//            StringBuffer buffer = new StringBuffer();
            while(res.moveToNext()){
//                buffer.append("Id: " + res.getString(0) + "\nName: " + res.getString(1) + "\nCategory: " + res.getString(2) + "\nTime: " + res.getInt(0) + "\n");
                //Toast.makeText(this, res.toString(), Toast.LENGTH_LONG).show();
//                showMessage("data", buffer.toString());
                mList.add(new ItemContainer(res.getLong(0), res.getString(1), res.getString(2), (res.getInt(3) != 0), res.getLong(4)));
            }
        }
    }

    public void addData(String name, String cat){
        long isSuccessful = mdb.insertData(name, cat);
        if(isSuccessful >= 0){
            Toast.makeText(this.getContext(), "data inserted", Toast.LENGTH_LONG).show();
            mList.add(new ItemContainer(isSuccessful, name, cat, false, 0));
        }else{
            Toast.makeText(this.getContext(), "data not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteData(long id, int i){
        if(mdb.deleteData(id) > 0) {
            mList.remove(i);
            adapter.notifyDataSetChanged();
            Toast.makeText(this.getContext(), "data deleted", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this.getContext(), "data not deleted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void applyTexts(String name, String cat) {
        addData(name, cat);
    }
}
