package com.example.mycoffee;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mycoffee.adapter.MyViewPagerAdapter;
import com.example.mycoffee.fragment.FragmentStatisticOrderUser;
import com.example.mycoffee.fragment.FragmentUpdateInforUser;
import com.example.mycoffee.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;


public class WelcomeActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private MyViewPagerAdapter myViewPagerAdapter;
    Bundle bundle=new Bundle();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        myViewPagerAdapter =
                new MyViewPagerAdapter
                        (getSupportFragmentManager(),getLifecycle());
        User user = (User) getIntent().getSerializableExtra("object_user");
        FragmentUpdateInforUser adsFragment = new FragmentUpdateInforUser();
        Bundle adsBundle = new Bundle();
        adsBundle.putSerializable("object_user", user);
        adsFragment.setArguments(adsBundle);

        FragmentStatisticOrderUser ads1Fragment =
                new FragmentStatisticOrderUser();
        Bundle ads1Bundle = new Bundle();
        ads1Bundle.putSerializable("object_user", user);
        ads1Fragment.setArguments(ads1Bundle);


        myViewPagerAdapter.addFragment(adsFragment);
        myViewPagerAdapter.addFragment(ads1Fragment);

        viewPager2.setAdapter(myViewPagerAdapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bundle.putSerializable("object_user",user);
                switch (item.getItemId()){
                    case R.id.menu_item_user_infor:
                        bundle = new Bundle();
                        bundle.putSerializable("object_user", user);
                        FragmentUpdateInforUser adsFragment = new FragmentUpdateInforUser();
                        adsFragment.setArguments(bundle);
                        viewPager2.setCurrentItem(0,false);
                        break;
                    case R.id.menu_item_user_order:
                        bundle = new Bundle();
                        bundle.putSerializable("object_user", user);
                        FragmentStatisticOrderUser adsFragment1 = new FragmentStatisticOrderUser();
                        adsFragment1.setArguments(bundle);
                        viewPager2.setCurrentItem(1, false);
                        break;
                }
                return true;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_user_infor).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_user_order).setChecked(true);
                        break;
                }
            }
        });
        bundle.putSerializable("object_user",user);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void initView() {
        viewPager2 = findViewById(R.id.viewPager2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
    }
}