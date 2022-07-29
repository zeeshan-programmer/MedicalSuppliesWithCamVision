package jbox.skillz.medic;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jbox.skillz.medic.Adapter.PrescAdapter;
import jbox.skillz.medic.Model.Cart;
import jbox.skillz.medic.Model.Presc;

public class UploadPrescription extends AppCompatActivity {

      TextView suggTv;

      Button uploadBtn, nextBtn;

      Button cancelBtn, tryAgainBtn;

      ListView listView;

      String finalString;
      String uid;

      ArrayList<Presc> prescList = new ArrayList<>();
      List<Cart> cartList = new ArrayList<>();

      LinearLayout headerLL;

      public ArrayList<String> prescItemsNames;

      private static final int REQUEST_CAMERA_CODE = 100;
      Bitmap bitmap;

      DatabaseReference reference;
      DatabaseReference inventryReference;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload_prescription);

            getSupportActionBar().setTitle("Quick Order");

            uploadBtn = findViewById(R.id.btn_cap);
            nextBtn = findViewById(R.id.next_btn);

            suggTv = findViewById(R.id.sugg_tv);

            headerLL = findViewById(R.id.header);

            listView = findViewById(R.id.presc_list);

            prescItemsNames = new ArrayList<>();
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            uid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("Cart").child(uid);
            inventryReference = FirebaseDatabase.getInstance().getReference().child("Inventory");

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED)
            {
                  ActivityCompat.requestPermissions(this, new String[]{
                          Manifest.permission.CAMERA
                  }, REQUEST_CAMERA_CODE);
            }

            Query queryyy = inventryReference.orderByChild("name");
            queryyy.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                              for (DataSnapshot dss : snapshot.getChildren()) {
                                    Cart cart= dss.getValue(Cart.class);
                                    prescItemsNames.add(cart.getName());
                              }
                        }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
            });

            uploadBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        prescList.clear();
                        cartList.clear();
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(UploadPrescription.this);
                  }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                        cartList.clear();
                        String[] items = finalString.split("\n");
                        List<String> itemList = Arrays.asList(items);

                        for (int i = 0; i<prescList.size(); i++)
                        {
                              String str = prescList.get(i).getPrescText();
                              search(str, i);
                        }

                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                              @Override
                              public void run() {
                                    runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                                decisionFunc();
                                          }
                                    });
                              }
                        }, 1000);
                  }

            });


      }

      @Override
      protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                  CropImage.ActivityResult  result = CropImage.getActivityResult(data);
                  if (resultCode == RESULT_OK)
                  {
                        Uri resultUri = result.getUri();
                        try {
                              bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                              getTextFromImage(bitmap);
                        } catch (IOException e) {
                              e.printStackTrace();
                        }
                  }
            }
            else
            {
                  Toast.makeText(this, "Image Crop Activity not Responding ", Toast.LENGTH_SHORT).show();
            }

      }

      private void getTextFromImage(Bitmap bitmap)
      {
            TextRecognizer  recognizer = new TextRecognizer.Builder(this).build();
            if (!recognizer.isOperational())
            {
                  Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
            else
            {
                  Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                  SparseArray<TextBlock> textBlockSparseArray = recognizer.detect(frame);
                  StringBuilder stringBuilder = new StringBuilder();

                  for (int i = 0; i<textBlockSparseArray.size(); i++)
                  {
                        TextBlock textBlock = textBlockSparseArray.valueAt(i);
                        stringBuilder.append(textBlock.getValue());
                        stringBuilder.append("\n");
                  }

                  finalString = stringBuilder.toString();

                  uploadBtn.setText("Retake");
                  nextBtn.setVisibility(View.VISIBLE);
                  suggTv.setVisibility(View.VISIBLE);
                  headerLL.setVisibility(View.GONE);


                  String[] items = finalString.split("\n");
                  List<String> itemList = Arrays.asList(items);

//                  ArrayList<Presc> prescList = new ArrayList<>();
                  for (int i = 0; i<=itemList.size()-1; i++)
                  {
                        prescList.add(new Presc(""+itemList.get(i)));
                  }

                  PrescAdapter adapter=new PrescAdapter(this,R.layout.presc_list_layout, prescList, prescItemsNames);
                  listView.setAdapter(adapter);
                  adapter.notifyDataSetChanged();
            }
      }

      private void search( String s, int i) {

            Query query = inventryReference.orderByChild("name").equalTo(s);

            query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if (snapshot.hasChildren()) {
                                    for (DataSnapshot dss : snapshot.getChildren()) {
                                          Cart cart= dss.getValue(Cart.class);
                                          if (cart.getName().equals(s)){
                                                cartList.add(new Cart(cart.getName(), ""+cart.getPrice(), ""+cart.getPrice(),""+cart.getImage(), ""+cart.getQuantity(), ""+cart.getDescription()));
                                                reference.setValue(cartList);
                                          }
                                    }
                              }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
            });
      }
      private void decisionFunc(){
            if (cartList.isEmpty())
            {
                  Dialog dialog = new Dialog(this);
                  dialog.setContentView(R.layout.error_alert);

                  cancelBtn = dialog.findViewById(R.id.error_btn);
                  tryAgainBtn = dialog.findViewById(R.id.try_btn);

                  cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              dialog.cancel();
                        }
                  });
                  tryAgainBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              dialog.cancel();
                        }
                  });
                  dialog.show();
            }
            else {
                  Intent intent = new Intent(UploadPrescription.this, CartActivity.class);
                  startActivity(intent);
            }
      }
}