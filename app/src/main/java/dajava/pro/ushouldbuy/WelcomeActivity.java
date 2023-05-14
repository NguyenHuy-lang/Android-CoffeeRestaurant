package dajava.pro.ushouldbuy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import dajava.pro.ushouldbuy.adapter.MyViewPagerAdapter;
import dajava.pro.ushouldbuy.fragment.FragmentAds;
import dajava.pro.ushouldbuy.fragment.FragmentAds1;
import dajava.pro.ushouldbuy.fragment.FragmentWelcome;
import dajava.pro.ushouldbuy.model.User;

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
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        User user = (User) getIntent().getSerializableExtra("object_user");
        FragmentAds adsFragment = new FragmentAds();
        Bundle adsBundle = new Bundle();
        adsBundle.putSerializable("object_user", user);
        adsFragment.setArguments(adsBundle);

        FragmentAds1 ads1Fragment = new FragmentAds1();
        Bundle ads1Bundle = new Bundle();
        ads1Bundle.putSerializable("object_user", user);
        ads1Fragment.setArguments(ads1Bundle);

        FragmentWelcome welcomeFragment = new FragmentWelcome();
        Bundle welcomeBundle = new Bundle();
        welcomeBundle.putSerializable("object_user", user);
        welcomeFragment.setArguments(welcomeBundle);

        myViewPagerAdapter.addFragment(adsFragment);
        myViewPagerAdapter.addFragment(ads1Fragment);
        myViewPagerAdapter.addFragment(welcomeFragment);

        viewPager2.setAdapter(myViewPagerAdapter);

        viewPager2.setAdapter(myViewPagerAdapter);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bundle.putSerializable("object_user",user);
                switch (item.getItemId()){
                    case R.id.menu_item_ads:
                        bundle = new Bundle();
                        bundle.putSerializable("object_user", user);
                        FragmentAds adsFragment = new FragmentAds();
                        adsFragment.setArguments(bundle);
                        viewPager2.setCurrentItem(0,false);
                        break;
                    case R.id.menu_item_ads1:
                        bundle = new Bundle();
                        bundle.putSerializable("object_user", user);
                        FragmentAds1 adsFragment1 = new FragmentAds1();
                        adsFragment1.setArguments(bundle);
                        viewPager2.setCurrentItem(1, false);
                        break;
                    case R.id.menu_item_welcome:
                        viewPager2.setCurrentItem(2,false);
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
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_ads).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_ads1).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_welcome).setChecked(true);
                        break;
                }
            }
        });
        bundle.putSerializable("object_user",user);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeActivity.this,ShopActivity.class);
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