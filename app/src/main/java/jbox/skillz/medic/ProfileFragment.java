package jbox.skillz.medic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


      View profileView;

      ImageView changeProfile, logOut, userProfileImg;
      TextView userName;
      EditText nameEv, emailEv, addressEv, mobileEv;
      Button updateBtn;

      DatabaseReference databaseReference;
      FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();

      String uid;

      String imageOnly;

      // TODO: Rename parameter arguments, choose names that match
      // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
      private static final String ARG_PARAM1 = "param1";
      private static final String ARG_PARAM2 = "param2";

      // TODO: Rename and change types of parameters
      private String mParam1;
      private String mParam2;

      public ProfileFragment() {
            // Required empty public constructor
      }

      /**
       * Use this factory method to create a new instance of
       * this fragment using the provided parameters.
       *
       * @param param1 Parameter 1.
       * @param param2 Parameter 2.
       * @return A new instance of fragment ProfileFragment.
       */
      // TODO: Rename and change types and number of parameters
      public static ProfileFragment newInstance(String param1, String param2) {
            ProfileFragment fragment = new ProfileFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
      }

      @Override
      public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                  mParam1 = getArguments().getString(ARG_PARAM1);
                  mParam2 = getArguments().getString(ARG_PARAM2);
            }
      }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            profileView =  inflater.inflate(R.layout.fragment_profile, container, false);

            changeProfile = profileView.findViewById(R.id.change_profile);
            logOut = profileView.findViewById(R.id.log_out);
            userProfileImg = profileView.findViewById(R.id.user_profile_image);

            userName = profileView.findViewById(R.id.user_name);
            nameEv = profileView.findViewById(R.id.user_name_ev);
            emailEv = profileView.findViewById(R.id.user_email_ev);
            addressEv = profileView.findViewById(R.id.user_adsress_ev);
            mobileEv = profileView.findViewById(R.id.user_mobile_ev);
            updateBtn = profileView.findViewById(R.id.update_btn);

            nameEv.setEnabled(false);
            emailEv.setEnabled(false);
            addressEv.setEnabled(false);
            mobileEv.setEnabled(false);
            updateBtn.setVisibility(View.GONE);

            return profileView;
      }

      @Override
      public void onStart() {
            super.onStart();
            if (current_user != null)
            {
                  uid = current_user.getUid();
                  databaseReference = FirebaseDatabase.getInstance().getReference().child("Accounts").child(uid);
                  databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                              String username = dataSnapshot.child("name").getValue().toString();
                              String loc = dataSnapshot.child("location").getValue().toString();
                              String mobile = dataSnapshot.child("mobile").getValue().toString();
                              imageOnly = dataSnapshot.child("image").getValue().toString();
                              String email = dataSnapshot.child("email").getValue().toString();

                              userName.setText(username);
                              nameEv.setText(username);
                              addressEv.setText(loc);
                              mobileEv.setText(mobile);
                              emailEv.setText(email);
                              Picasso.get().load(imageOnly).placeholder(R.drawable.profile_dp).into(userProfileImg);

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                  });
            }

            changeProfile.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        nameEv.setEnabled(true);
                        emailEv.setEnabled(true);
                        addressEv.setEnabled(true);
                        mobileEv.setEnabled(true);
                        updateBtn.setVisibility(View.VISIBLE);
                  }
            });

            logOut.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                  }
            });

            updateBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        String name = nameEv.getText().toString();
                        String email = emailEv.getText().toString();
                        String location = addressEv.getText().toString();
                        String mob = mobileEv.getText().toString();

                        if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(location) || !TextUtils.isEmpty(mob)){

                              HashMap<String,String> userMap = new HashMap<>();
                              userMap.put("name",name);
                              userMap.put("email",email);
                              userMap.put("location",location);
                              userMap.put("mobile",mob);
                              userMap.put("image",imageOnly);

                              if (uid != null)
                              {
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Accounts").child(uid);
                                    databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                                nameEv.setEnabled(false);
                                                emailEv.setEnabled(false);
                                                addressEv.setEnabled(false);
                                                mobileEv.setEnabled(false);
                                                updateBtn.setVisibility(View.GONE);
                                          }
                                    });
                              }
                              else
                              {
                                    Toast.makeText(getContext(), "Login to make Changes", Toast.LENGTH_LONG).show();
                              }
                        }
                        else
                        {
                              Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                        }
                  }
            });

      }
}