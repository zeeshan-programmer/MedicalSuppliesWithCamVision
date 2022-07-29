package jbox.skillz.medic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TrackingActivity extends AppCompatActivity {

      TextView orderIDTv, orderAddressTv, orderPaymentType, orderStatusTv;

      ImageView processingImg, confirmedImg, dispatchImg, deliveredImg;

      Button backBtn;

      String orderStatus = "processing";

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tracking);

            getSupportActionBar().setTitle("Track Order");

            orderIDTv = findViewById(R.id.track_order_id);
            orderStatusTv = findViewById(R.id.track_order_status);
            orderPaymentType = findViewById(R.id.track_order_payment_type);
            orderAddressTv = findViewById(R.id.track_order_address);

            processingImg = findViewById(R.id.track_order_process_img);
            confirmedImg = findViewById(R.id.track_order_confirm_img);
            dispatchImg = findViewById(R.id.track_order_dispatch_img);
            deliveredImg = findViewById(R.id.track_order_delivered_img);

            backBtn = findViewById(R.id.track_order_back);

            Intent intent = getIntent();
            orderIDTv.setText(intent.getStringExtra("ordid"));
            orderStatusTv.setText(intent.getStringExtra("status"));
            orderStatus = intent.getStringExtra("status");
            orderPaymentType.setText(intent.getStringExtra("payment"));
            orderAddressTv.setText(intent.getStringExtra("address"));

      }

      @Override
      protected void onStart() {
            super.onStart();

            backBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        finish();
                  }
            });

            if (orderStatus.equals("processing"))
            {
                  processingImg.setImageResource(R.drawable.progress_blue);
                  orderStatusTv.setText("Your order is being Processed");
            }
            else if (orderStatus.equals("confirmed"))
            {
                  processingImg.setImageResource(R.drawable.progress_blue);
                  confirmedImg.setImageResource(R.drawable.progress_blue);
                  orderStatusTv.setText("Your order is Confirmed");
            }
            else if (orderStatus.equals("dispatched"))
            {
                  processingImg.setImageResource(R.drawable.progress_blue);
                  confirmedImg.setImageResource(R.drawable.progress_blue);
                  dispatchImg.setImageResource(R.drawable.progress_blue);
                  orderStatusTv.setText("Your order is Dispatched");
            }
            else if (orderStatus.equals("delivered"))
            {
                  processingImg.setImageResource(R.drawable.progress_blue);
                  confirmedImg.setImageResource(R.drawable.progress_blue);
                  dispatchImg.setImageResource(R.drawable.progress_blue);
                  deliveredImg.setImageResource(R.drawable.progress_blue);
                  orderStatusTv.setText("Your order is Delivered");
            }

      }
}