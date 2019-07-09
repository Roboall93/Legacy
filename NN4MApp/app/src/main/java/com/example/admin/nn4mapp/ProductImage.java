package com.example.admin.nn4mapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // No titlebar
        setContentView(R.layout.activity_product_image);

        Product prod = ProductList.SelectedProduct;
        ImageView iv =(ImageView) findViewById(R.id.imgLarge);
        iv.setImageBitmap(prod.getCachedImage());
    }
}
