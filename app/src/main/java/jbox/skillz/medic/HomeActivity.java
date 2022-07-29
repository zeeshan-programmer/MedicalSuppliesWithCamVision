package jbox.skillz.medic;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

      BottomNavigationView bottomNavigationView;

      HomeFragment homeFragment = new HomeFragment();
      ProfileFragment profileFragment = new ProfileFragment();
      OrderFagment orderFagment = new OrderFagment();

      String flag ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

         getSupportActionBar().hide();


          bottomNavigationView  = findViewById(R.id.bottom_navigation);

          getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

//          BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.article);
//          badgeDrawable.setVisible(true);

          bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                      switch (item.getItemId()){
                            case R.id.home:
                                  getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                                  return true;
//                            case R.id.article:
//                                  getSupportFragmentManager().beginTransaction().replace(R.id.container,articleFragment).commit();
//                                  return true;
                            case R.id.order:
                                  getSupportFragmentManager().beginTransaction().replace(R.id.container, orderFagment).commit();
                                  return true;
                            case R.id.profile:
                                  getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                                  return true;
                      }

                      return false;
                }
          });


    }

      @Override
      protected void onStart() {
            super.onStart();


      }

}