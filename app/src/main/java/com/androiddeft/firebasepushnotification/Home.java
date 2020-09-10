package com.androiddeft.firebasepushnotification;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.blog.library.UpdateChecker;
import com.google.android.material.navigation.NavigationView;

import static com.androiddeft.firebasepushnotification.WebviewActivity.isNetworkStatusAvialable;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    ScrollView scrollView;
    Toolbar toolbar = null;
    int action = 0;
    String alumni_url = "http://rudf.6te.net/webapp/database/view/alumni.php";
    String executive_url = "http://rudf.6te.net/webapp/database/view/executive.php";
    String general_url = "http://rudf.6te.net/webapp/database/view/member.php";
    String adviser = "http://rudf.6te.net/webapp/database/view/adviser.php";
    String notice = "http://rudf.6te.net/webapp/database/view/notice.php";
    String fbapp = "fb://group/49880688703";
    String fburl = "https://www.facebook.com/groups/bfdf.ru/";
    String pageApp = "fb://page/169680089735915";
    String pageurl = "https://www.facebook.com/rubfdf/";
    private Context liContext = null;

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 22) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_home);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        hideitem();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        liContext = this.getApplicationContext();

        FrameLayout frameLayout = findViewById(R.id.layout);
        final TextView alumni = frameLayout.findViewById(R.id.alumni);
        TextView adviser = frameLayout.findViewById(R.id.adviser);
        final TextView executive = frameLayout.findViewById(R.id.executive);
        final TextView general = frameLayout.findViewById(R.id.general);
        TextView notification = frameLayout.findViewById(R.id.notification);
        final Button checknew = frameLayout.findViewById(R.id.checkNew);

        checknew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateChecker.checkForDialog(Home.this);
            }
        });

        if (isNetworkStatusAvialable(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checknew.performClick();
                }
            }, 1);
        } else {

            Toast.makeText(getApplicationContext(), (getString(R.string.connect_net)), Toast.LENGTH_LONG).cancel();
        }

        executive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("url", (executive_url));
                setTitle("Executive Member");
                startActivity(i);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("url", (notice));
                setTitle("Notifications");
                startActivity(i);
            }
        });

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("url", (general_url));
                setTitle("General Member");
                startActivity(i);
            }
        });

        adviser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String notify = getString(R.string.commingsoon);
                AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
                alert.setMessage(notify)
                        /*.setNegativeButton(getString(R.string.ok_btn), null)*/
                        .setCancelable(true)
                        .show();
            }
        });
        alumni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("url", (alumni_url));
                setTitle("Alumni Member");
                startActivity(i);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void hideitem() {
        navigationView = findViewById(R.id.nav_view);
        Menu drawer_menu = navigationView.getMenu();
        drawer_menu.findItem(R.id.nav_home).setChecked(true).setEnabled(false);
    }

    private void doAction(Class c) {
        startActivity(new Intent(this, c));
        this.action++;
    }

    public void About(View view) {
        doAction(About.class);
    }

    public void Profile(View view) {
        doAction(ProfileActivity.class);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem1 = menu.findItem(R.id.pro);
        menuItem1.setVisible(false);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_about_app:
                Intent h = new Intent(Home.this, AboutApp.class);
                startActivity(h);
                return true;
            case R.id.pro:
                Intent p = new Intent(Home.this, ProfileActivity.class);
                startActivity(p);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent h = new Intent(Home.this, Home.class);
                startActivity(h);
                break;
            case R.id.Notification:
                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("url", (notice));
                startActivity(i);
                break;
            case R.id.alumni:
                Intent aa = new Intent(getApplicationContext(), WebviewActivity.class);
                aa.putExtra("url", (alumni_url));
                startActivity(aa);
                break;
            case R.id.adviser:
                String notify = getString(R.string.commingsoon);
                AlertDialog.Builder alert = new AlertDialog.Builder(Home.this);
                alert.setMessage(notify)
                        /*.setNegativeButton(getString(R.string.ok_btn), null)*/
                        .setCancelable(true)
                        .show();
                break;
            case R.id.ExecutiveMember:
                Intent ie = new Intent(getApplicationContext(), WebviewActivity.class);
                ie.putExtra("url", (executive_url));
                startActivity(ie);
                break;
            case R.id.GeneralMember:
                Intent gi = new Intent(getApplicationContext(), WebviewActivity.class);
                gi.putExtra("url", (general_url));
                startActivity(gi);
                break;

            case R.id.nav_update:
                if (isNetworkStatusAvialable(getApplicationContext())) {
                    UpdateChecker.checkForDialog(Home.this);
                } else {
                    String titleText = getString(R.string.check_new_update_error);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(140, 140, 140));
                    SpannableStringBuilder color = new SpannableStringBuilder(titleText);
                    color.setSpan(foregroundColorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle(getString(R.string.connect_net))
                            .setMessage(color)
                            /*.setNegativeButton(getString(R.string.ok_btn), null)*/
                            .setCancelable(true)
                            .show();
                }
                break;

            case R.id.nav_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, (getString(R.string.app_name)));
                String shareMessage = (getString(R.string.msg_share));
                shareMessage = shareMessage + getString(R.string.app_download_link);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, (getString(R.string.share))));
                break;
            case R.id.nav_about:
                Intent a = new Intent(Home.this, About.class);
                startActivity(a);
                break;

            case R.id.FacebookGroup:
                if (isNetworkStatusAvialable(getApplicationContext())) {
                    if (isAppInstalled(liContext, "com.facebook.orca") || isAppInstalled(liContext, "com.facebook.katana")
                            || isAppInstalled(liContext, "com.example.facebook") || isAppInstalled(liContext, "com.facebook.android")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fbapp)));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fburl)));
                    }

                } else {
                    String titleText = getString(R.string.fb_grp);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(140, 140, 140));
                    SpannableStringBuilder color = new SpannableStringBuilder(titleText);
                    color.setSpan(foregroundColorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle(getString(R.string.connect_net))
                            .setMessage(color)
                            /*.setNegativeButton(getString(R.string.ok_btn), null)*/
                            .setCancelable(true)
                            .show();
                }
                break;

            case R.id.FacebookPage:
                if (isNetworkStatusAvialable(getApplicationContext())) {
                    if (isAppInstalled(liContext, "com.facebook.orca") || isAppInstalled(liContext, "com.facebook.katana")
                            || isAppInstalled(liContext, "com.example.facebook") || isAppInstalled(liContext, "com.facebook.android")) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pageApp)));
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pageurl)));
                    }

                } else {
                    String titleText = getString(R.string.fb_grp);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(140, 140, 140));
                    SpannableStringBuilder color = new SpannableStringBuilder(titleText);
                    color.setSpan(foregroundColorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setTitle(getString(R.string.connect_net))
                            .setMessage(color)
                            /* .setNegativeButton(getString(R.string.ok_btn), null)*/
                            .setCancelable(true)
                            .show();
                }
                break;
            case R.id.nav_feedback:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getString(R.string.feedbackMail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, (getString(R.string.feedbacksub)));
                intent.putExtra(Intent.EXTRA_TEXT,(getString(R.string.msg_feedback)));
                startActivity(Intent.createChooser(intent, (getString(R.string.feedTitle))));
                break;
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
