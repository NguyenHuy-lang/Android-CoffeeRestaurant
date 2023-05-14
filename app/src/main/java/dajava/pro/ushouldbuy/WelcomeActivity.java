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
        myViewPagerAdapter.addFragment(new FragmentAds());
        myViewPagerAdapter.addFragment(new FragmentAds1());
        myViewPagerAdapter.addFragment(new FragmentWelcome());
        viewPager2.setAdapter(myViewPagerAdapter);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_item_ads:
                        viewPager2.setCurrentItem(0,false);
                        break;
                    case R.id.menu_item_ads1:
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
        User user = (User) getIntent().getExtras().get("object_user");
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