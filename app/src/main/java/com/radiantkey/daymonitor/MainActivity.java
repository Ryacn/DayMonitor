package com.radiantkey.daymonitor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private Fragment recordFragment, historyFragment, graphFragment;

    public Fragment getCurFragment() {
        return curFragment;
    }

    private Fragment curFragment;

    DatabaseHelper mdb;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = recordFragment;
                    break;
                case R.id.navigation_dashboard:
                    fragment = historyFragment;
                    break;
                case R.id.navigation_notifications:
                    fragment = graphFragment;
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdb = new DatabaseHelper(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recordFragment = new FragmentRecord();
        historyFragment = new FragmentHistory();
        graphFragment = new FragmentGraph();
        loadFragment(recordFragment);
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            curFragment = fragment;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragContainer, fragment).commit();
            return true;
        }
        return false;
    }

//    public void addData(){
//        boolean isSuccessful = mdb.insertData("","");
//        if(isSuccessful){
//            Toast.makeText(this, "data inserted", Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(this, "data not inserted", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void viewAll(){
//        Cursor res = mdb.getAllData();
//        if(res.getCount() == 0){
//            //Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
//            showMessage("Error", "No data found");
//            return;
//        }else{
//            StringBuffer buffer = new StringBuffer();
//            while(res.moveToNext()){
//                buffer.append("Id: " + res.getString(0) + "\nName: " + res.getString(1) + "\nCategory: " + res.getString(2) + "\nTime: " + res.getInt(0) + "\n");
//                //Toast.makeText(this, res.toString(), Toast.LENGTH_LONG).show();
//                showMessage("data", buffer.toString());
//
//            }
//        }
//    }
//
//    public void showMessage(String title, String message){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(true);
//        builder.setTitle(title);
//        builder.setMessage(message);
//        builder.show();
//    }
//
//    public void updateData(){
//        boolean isUpdate = mdb.updateData("test","test", "test", 5);
//        if(isUpdate){
//            Toast.makeText(this, "data update", Toast.LENGTH_LONG).show();
//        }else{
//
//            Toast.makeText(this, "data not update", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void deleteData(){
//        Integer deletedRows = mdb.deleteData("1");
//        if(deletedRows > 0){
//            Toast.makeText(this, "data deleted", Toast.LENGTH_LONG).show();
//        }else {
//            Toast.makeText(this, "data not deleted", Toast.LENGTH_LONG).show();
//        }
//    }
}
