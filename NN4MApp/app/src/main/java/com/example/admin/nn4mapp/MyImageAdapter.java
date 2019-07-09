package com.example.admin.nn4mapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Custom adapter to translate the list of products to the listview
public class MyImageAdapter extends ArrayAdapter<Product> {
    int resource;

    TextView itemName;
    TextView itemCost;
    ImageView itemImage;
    ListView lv;

    // Stores our image download async tasks as they're running
    private List<DownloadImageTask> tasks;

    public MyImageAdapter(Context context, int resource, List<Product> products, ListView parent) {
        super(context, resource, products);
        this.resource = resource;
        tasks = new ArrayList<>();
        lv = parent;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // ListView recycles some list elements
        // Checking to see if we have a recycled view to use
        LinearLayout productView = null;

        if(convertView == null) {
            // Make a new layout and inflate it
            productView = new LinearLayout(getContext());
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(resource, productView, true);
        }
        else productView = (LinearLayout) convertView;

        // Create the layout views
        TextView itemName = (TextView) productView.findViewById(R.id.txtItemName);
        TextView itemCost = (TextView) productView.findViewById(R.id.txtItemCost);

        ImageView itemImage = (ImageView) productView.findViewById(R.id.imgItemImage);

        ProgressBar loadingIndicator = (ProgressBar) productView.findViewById(R.id.pbLoadingIndicator);

        Product p = getItem(position);

        itemName.setText(p.getName());
        itemCost.setText(String.format(Locale.UK, "$%.2f", p.getCost()));
        // Set a tag on the image for later reference
        itemImage.setTag(position);

        // If the image hasn't been stored, start a new task to acquire it
        if(p.getCachedImage() == null) {
            String imageURL = String.format("http://riverisland.scene7.com/is/image/RiverIsland/%s_main", p.getId());
            itemImage.setVisibility(View.INVISIBLE);
            loadingIndicator.setVisibility(View.VISIBLE);
            DownloadImageTask dit = new DownloadImageTask(loadingIndicator, itemImage, p, position);
            tasks.add(dit);
            dit.execute(imageURL);
        }
        else
        {
            itemImage.setImageBitmap(p.getCachedImage());
        }

        // Cancel tasks for list items that are not on screen
        List<DownloadImageTask> tempTasks = new ArrayList<DownloadImageTask>();
        for(DownloadImageTask task : tasks)
        {
            if(task.getPosition() < lv.getFirstVisiblePosition() - 2 || task.getPosition() > lv.getLastVisiblePosition() + 2) task.cancel(true);
            else if(!task.done) tempTasks.add(task);
        }
        // Set list of active tasks
        tasks = tempTasks;

        return productView;
    }

    // Async task to download product images in the background while scrolling
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private String tag;

        ProgressBar loadingIndicator;
        ImageView itemImage;
        Product p;
        boolean done = false;
        int position;


        private DownloadImageTask(ProgressBar pb, ImageView iv, Product prod, int position){
            loadingIndicator = pb;
            itemImage = iv;
            p = prod;
            this.position = position;
            tag = String.valueOf(iv.getTag());
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap prodImg = null;
            try {
                prodImg = BitmapFactory.decodeStream(new URL(urldisplay).openConnection().getInputStream());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return prodImg;
        }

        protected void onPostExecute(Bitmap result) {
            // Cache image since we did the work and set done
            p.setCachedImage(result);
            setDone(true);
            // We don't set the image unless tags are equal (Imageview is the same)
            if (!itemImage.getTag().toString().equals(tag)) return;
            loadingIndicator.setVisibility(View.INVISIBLE);
            itemImage.setVisibility(View.VISIBLE);
            itemImage.setImageBitmap(result);

        }
    }
}