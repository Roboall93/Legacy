package com.example.admin.nn4mapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/*
    MainMenu class
    First activity to start the app
    River Island Logo with a Next Page Button
 */
public class MainMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set splash screen as theme before setting content view
        // Pause for a second just to ake sure it's visible
        setTheme(R.style.AppTheme);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Grab ImageButton and set it to start the next activity -> ProductList
        ImageButton mNextPageButton = (ImageButton) findViewById(R.id.playButton);
        mNextPageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToProductPage();
            }
        });

    }

    private void goToProductPage() {
        Intent intent = new Intent(this, ProductList.class);
        startActivity(intent);
    }
}
