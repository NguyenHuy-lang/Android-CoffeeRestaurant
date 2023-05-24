package com.example.mycoffee;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mycoffee.adapter.MyViewPagerAdapter;
import com.example.mycoffee.fragment.FragmentManageItem;
import com.example.mycoffee.fragment.FragmentManageOrder;
import com.example.mycoffee.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class ManageActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private MyViewPagerAdapter myViewPagerAdapter;
    Bundle bundle=new Bundle();
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),getLifecycle());
        User user = (User) getIntent().getSerializableExtra("object_user");
        viewPager2 = findViewById(R.id.viewPagerManage);
        bottomNavigationView = findViewById(R.id.bottomNavigationManageView);
        FragmentManageItem fragmentManageItem = new FragmentManageItem();
        fab = findViewById(R.id.fabManage);
        Bundle adsBundle = new Bundle();
        adsBundle.putSerializable("object_user", user);
        fragmentManageItem.setArguments(adsBundle);
        myViewPagerAdapter.addFragment(fragmentManageItem);
        FragmentManageOrder fragmentManageOrder = new FragmentManageOrder();
        Bundle adsBundle2 = new Bundle();
        adsBundle2.putSerializable("object_user", user);
        fragmentManageOrder.setArguments(adsBundle2);
        myViewPagerAdapter.addFragment(fragmentManageOrder);
        viewPager2.setAdapter(myViewPagerAdapter);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bundle.putSerializable("object_user",user);
                switch (item.getItemId()){
                    case R.id.menu_item_manage_item:
                        bundle = new Bundle();
                        bundle.putSerializable("object_user", user);
                        FragmentManageItem fagmentManageItem = new FragmentManageItem();
                        fagmentManageItem.setArguments(bundle);
                        viewPager2.setCurrentItem(0,false);
                        break;
                    case R.id.menu_item_manage_order:
                        bundle = new Bundle();
                        bundle.putSerializable("object_user", user);
                        FragmentManageOrder fragmentManageOrder = new FragmentManageOrder();
                        fragmentManageOrder.setArguments(bundle);
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
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_manage_item).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_item_manage_order).setChecked(true);
                        break;
                }
            }
        });
        bundle.putSerializable("object_user",user);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageActivity.this,HomeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}