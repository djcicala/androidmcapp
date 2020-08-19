package com.example.marketcheckcarsearchapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.NumberFormat;

public class SingleListingWindow extends AppCompatActivity {

    TextView title, vin, price, engine, mileage, cylinders, condition, transmission, color, drivetype, oneowner, cleantitle, dealer, dealer_link, address, distance;
    ImageView displayed;
    CarListing selected, with_images;
    Button left_button, right_button;
    int image_index;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_listing_window);
        Intent in = getIntent();
        selected = in.getParcelableExtra("listing");
        image_index = 0;
        GetAllImages gai = new GetAllImages(selected, this);
        gai.execute();

        displayed = (ImageView) findViewById(R.id.image);

        left_button = (Button) findViewById(R.id.left_button);
        right_button = (Button) findViewById(R.id.right_button);
        right_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                image_index++;
                if(image_index > with_images.image_bmps.size())
                {
                    image_index = 0;
                }
                displayed.setImageBitmap(with_images.image_bmps.get(image_index));
                displayed.invalidate();
            }
        });
        left_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                image_index--;
                if(image_index < 0)// with_images.image_bmps.size())
                {
                    image_index = with_images.image_bmps.size() - 1;
                }
                displayed.setImageBitmap(with_images.image_bmps.get(image_index));
                displayed.invalidate();
            }
        });

        title = (TextView) findViewById(R.id.Title);
        vin = (TextView) findViewById(R.id.vin);
        price = (TextView) findViewById(R.id.price);
        engine = (TextView) findViewById(R.id.Engine);
        mileage = (TextView) findViewById(R.id.mileage);
        cylinders = (TextView) findViewById(R.id.cylinders);
        condition = (TextView) findViewById(R.id.condition);
        transmission = (TextView) findViewById(R.id.Transmission);
        color = (TextView) findViewById(R.id.color);
        drivetype = (TextView) findViewById(R.id.drivetype);
        oneowner = (TextView) findViewById(R.id.oneowner);
        cleantitle = (TextView) findViewById(R.id.cleantitle);
        dealer = (TextView) findViewById(R.id.dealer);
        dealer_link = (TextView) findViewById(R.id.dealer_link);
        address = (TextView) findViewById(R.id.address);
        distance = (TextView) findViewById(R.id.distance);

        title.setText(selected.listview_hashmap.get("title"));
        vin.setText("VIN: " + selected.vin);
        price.setText("Price: $" + NumberFormat.getIntegerInstance().format(selected.price));
        engine.setText("Engine: " + selected.engine);
        mileage.setText("Mileage: " + NumberFormat.getIntegerInstance().format(selected.mileage));
        cylinders.setText("Cylinders: " + Integer.toString(selected.cylinders));
        condition.setText("Condition: " + selected.condition);
        transmission.setText("Transmission: " + selected.transmission);
        condition.setText("Condition: " + selected.condition);
        color.setText("Color: " + selected.color);
        drivetype.setText("Drivetype: " + selected.drivetype);
        if(selected.one_owner) oneowner.setText("Carfax 1-Owner: Yes");
        else oneowner.setText("Carfax 1-Owner: No");
        if(selected.clean_title) cleantitle.setText("Clean Title: Yes");
        else cleantitle.setText("Clean Tite: No");
        dealer.setText(selected.seller_name);
        dealer_link.setText(selected.website);
        address.setText(selected.city + ", " + selected.state);
        distance.setText(Double.toString(selected.distance) + "miles from your ZIP");
    }

    public void setImageBitmaps(CarListing with_images)
    {
        this.with_images = with_images;
        if(with_images.image_bmps.size() > 0)
        {
            displayed.setImageBitmap(with_images.image_bmps.get(0));
            displayed.invalidate();
        }
    }
}
