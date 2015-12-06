package ua.com.qascript.android.tab;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import ua.com.qascript.android.ProfileActivity;
import ua.com.qascript.android.R;
import ua.com.qascript.android.app.App;
import ua.com.qascript.android.view.ProfileActivityNew;

public class MainActivityTab extends AppCompatActivity {
        Toolbar toolbar;
        ImageButton imageButton;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main_tab);
                imageButton = (ImageButton)findViewById(R.id.imageButtonProfile);
                imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                profileButtonHandler(v);
                        }
                });
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Around You");
                toolbar.setTitleTextColor(Color.WHITE);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText("Folks"));
                tabLayout.addTab(tabLayout.newTab().setText("Asks"));
                //tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                tabLayout.setTabTextColors(R.color.tab_unselected_text, R.color.tab_selected_text);
                final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                final PagerAdapter adapter = new PagerAdapter
                        (getSupportFragmentManager(), tabLayout.getTabCount());
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                                viewPager.setCurrentItem(tab.getPosition());
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                });
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                //getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_settings) {
                        return true;
                }

                return super.onOptionsItemSelected(item);
        }

        public void profileButtonHandler(View v) {
                Intent i = new Intent(MainActivityTab.this, ProfileActivityNew.class);
                i.putExtra("profileId", App.getInstance().getId());
                startActivity(i);
        }
}
