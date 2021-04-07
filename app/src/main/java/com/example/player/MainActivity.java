package com.example.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
String[]items;
ListView listView;



SearchView searchView;
    public static final int PERMISSION_READ = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchView=findViewById(R.id.search_Btn);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView=findViewById(R.id.listView);
        runTimePermission();
//        if (checkPermission()) {
//            displaySong();
//        }


    }

    public void runTimePermission(){
        Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }


      ArrayList<File> findSong(File file){
          ArrayList<File>  arrayList=new ArrayList<>();
        File[] files=file.listFiles();

        for (File singleFile:files){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            }else {
                if (singleFile.getName().endsWith(".mp3")  || singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

        void displaySong(){
        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());
        items=new String[mySongs.size()];
            if (mySongs.size()>0){
                for (int i=0;i<mySongs.size();i++){
                    items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
                }
            }

//            ArrayAdapter<String > myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
//            listViewSong.setAdapter(myAdapter);

            CustomAdapter customAdapter=new CustomAdapter();
            listView.setAdapter(customAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName= (String) listView.getItemAtPosition(position);
                startActivity(new Intent(MainActivity.this,PlayerActivity.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songName",songName)
                        .putExtra("pos",position));
                }
            });

        }

class CustomAdapter extends BaseAdapter{
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView=getLayoutInflater().inflate(R.layout.list_item,null);
        TextView textSong=myView.findViewById(R.id.textSongName);
        textSong.setSelected(true);
        textSong.setText(items[position]);

        return myView ;
    }
}




}