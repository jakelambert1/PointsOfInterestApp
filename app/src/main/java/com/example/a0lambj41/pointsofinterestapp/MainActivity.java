package com.example.a0lambj41.pointsofinterestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends Activity
{

    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        setContentView(R.layout.activity_main);

        mv = (MapView)findViewById(R.id.map1);

        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(14);
        mv.getController().setCenter(new GeoPoint(50.9,-1.4));

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), null);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.addpoi)
        {
            // react to the menu item being selected...
            // Launch second activity
            Intent intent = new Intent(this,AddPOIActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }
        else if (item.getItemId() == R.id.save)
        {
            try {
                FileWriter fw = new FileWriter(dir_path + "/notes.txt");
                PrintWriter pw = new PrintWriter(fw);
                pw.println(name.getText());
                pw.println(type.getText());
                pw.println(description.getText());
                pw.flush();
                pw.close();
            } catch (IOException e){
                System.out.println("ERROR! " + e.getMessage());
            }
            Toast.makeText(MainActivity.this, "Marker Created!", Toast.LENGTH_SHORT).show();
            // react to the menu item being selected...
            return true;
        }
        else if (item.getItemId() == R.id.load) {

            try {
                FileReader fr = new FileReader(dir_path + "/notes.txt");
                BufferedReader br = new BufferedReader(fr);
                name.setText(br.readLine());
                type.setText(br.readLine());
                description.setText(br.readLine());
                br.close();
            }
            catch(IOException e){
                System.out.println("ERROR! " + e.getMessage());
            }
            Toast.makeText(MainActivity.this, "Marker Created!", Toast.LENGTH_SHORT).show();
            // react to the menu item being selected...
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 0) {
            Bundle bundle = intent.getExtras();

            String poiname = bundle.getString("com.example.pointofinterestapp.name");
            String poitype = bundle.getString("com.example.pointofinterestapp.type");
            String poidesc = bundle.getString("com.example.pointofinterestapp.desc");

            double latitude = mv.getMapCenter().getLatitude();
            double longitude = mv.getMapCenter().getLongitude();

            OverlayItem addpoi = new OverlayItem(poiname, poitype + poidesc, new GeoPoint(latitude, longitude));

            items.addItem(addpoi);
            mv.getOverlays().add(items);
            
            mv.refreshDrawableState();

            Toast.makeText(MainActivity.this, "Marker Created!", Toast.LENGTH_SHORT).show();

        }else if(requestCode == 1) {
        }
    }
}