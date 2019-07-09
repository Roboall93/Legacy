package com.example.admin.nn4mapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
    Second activity class, uses a helper class to read the JSON file
    and display the contents in a list view.
 */

public class ProductList extends AppCompatActivity implements Serializable {

    // Global variable stores currently selected product to be referenced in the large image view
    // I experimented with the parcelable package but couldn't get it to work passing objects
    public static Product SelectedProduct = null;
    private String TAG = ProductList.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get products JSON
    private static String url = "https://ri.nn4m.net/RI/sv5/api/public/index.php/category/2508/products.json";

    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        // List to store the product objects as we make them
        productList = new ArrayList<Product>();
        //ListView to display the list of products
        lv = (ListView) findViewById(R.id.list);

        new GetProducts().execute();
    }


    /**
     * Async task class to get json by making HTTP call (background threading)
     */
    private class GetProducts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog in case of hangups
            pDialog = new ProgressDialog(ProductList.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray products = jsonObj.getJSONArray("Products");

                    // looping through All Products
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Grab the entries we need to store
                        String name = c.getString("name");
                        Double price = c.getDouble("costUSD");
                        String prodid = c.getString("prodid"); // Used to grab image url

                         // creating Product object from JSON info
                        Product p = new Product(name, price, prodid);
                        Log.e(TAG, p.getName());

                        // adding product to product list
                        productList.add(p);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
             // Updating parsed JSON data into ListView
            ListAdapter adapter = new MyImageAdapter(
                    ProductList.this, R.layout.list_item, productList, lv);

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adView, View view, int position, long id)
                {
                    Product prod = (Product) adView.getItemAtPosition(position);
                    goToProductImage(prod);
                }
            });
        }

    }

    private void goToProductImage(Product prod) {
        Intent intent = new Intent(this, ProductImage.class);
        SelectedProduct = prod;
        startActivity(intent);
    }
}
