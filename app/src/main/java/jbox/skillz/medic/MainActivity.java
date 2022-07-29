package jbox.skillz.medic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

      Button loginBt, guestBtn;

      GoogleSignInClient mGoogleSignInClient;
      final static int RC_SIGN_IN = 123;
      FirebaseAuth mAuth;
      DatabaseReference databaseReference;
      String uid;
      FirebaseUser user;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            setTheme(R.style.Theme_Medic);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            getSupportActionBar().hide();

            loginBt  = findViewById(R.id.signin_btn);
            guestBtn  = findViewById(R.id.guest_btn);

            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();

            if (user != null)
            {
                  Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                  startActivity(intent);
                  finish();
            }
            else
            {
                  createRequest();
            }


            loginBt.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        signIn();
                  }
            });

            guestBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                  }
            });

      }

      @Override
      protected void onStart() {
            super.onStart();

      }

      void createRequest() {

            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("144597358079-gdn5ue002leb2f8tlet2gq9cvhc1qc0h.apps.googleusercontent.com")
                    .requestEmail()
                    .build();


            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


      }


      private void signIn() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
      }


      @Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                  Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                  try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                  } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        // ...
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
            }
      }


      private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                      // Sign in success, update UI with the signed-in user's information
                                      user = mAuth.getCurrentUser();
                                      uid = user.getUid();


                                      HashMap<String,String> userMap = new HashMap<>();
                                      userMap.put("name", acct.getDisplayName());
                                      userMap.put("email", acct.getEmail());
                                      userMap.put("image", acct.getPhotoUrl().toString());
                                      userMap.put("mobile", "03009999999");
                                      userMap.put("location","jinnah Colony, Fslbd, Punjab, Pakistan");

                                      databaseReference = FirebaseDatabase.getInstance().getReference().child("Accounts").child(uid);
                                      databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                  Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                  startActivity(intent);
                                                  finish();
                                            }
                                      });
                                }
                                else
                                {
                                      Toast.makeText(MainActivity.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();
                                }

                          }
                    });
      }



}