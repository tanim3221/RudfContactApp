package com.androiddeft.firebasepushnotification;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.blog.library.UpdateChecker;
import com.google.android.material.navigation.NavigationView;

public class WebviewActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    private WebView webView = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    String alumni = "http://rudf.6te.net/webapp/database/view/alumni.php";
    String executive = "http://rudf.6te.net/webapp/database/view/executive.php";
    String general = "http://rudf.6te.net/webapp/database/view/member.php";
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

    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                return netInfos.isConnected();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 22) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_webview);
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        String url = getIntent().getExtras().getString("url");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        liContext = this.getApplicationContext();
        FrameLayout frameLayout = findViewById(R.id.layout);
        final WebView webView = frameLayout.findViewById(R.id.webView);
        /*  final ProgressBar progress = frameLayout.findViewById(R.id.progress);*/
        final SwipeRefreshLayout swipeRefreshLayout = frameLayout.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


       /* //progressbar tinting color
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Drawable wrapDrawable = DrawableCompat.wrap(progress.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getApplicationContext(),
                    R.color.colorPrimary));
            progress.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));

        } else {
            progress.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
        //progressbar tinting color*/
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);
        webView.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollbarFadingEnabled(true);
        if (isNetworkStatusAvialable(getApplicationContext())) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }

        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                webView.loadUrl("about:blank");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            @TargetApi(Build.VERSION_CODES.M)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.loadUrl("about:blank");
                super.onReceivedError(view, request, error);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);

                    Toast.makeText(getApplicationContext(), (getString(R.string.call_toast)),
                            Toast.LENGTH_LONG).show();

                    return true;

                } else if (url.startsWith("mailto:")) {
                    if (isNetworkStatusAvialable(getApplicationContext())) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), (getString(R.string.email_toast)),
                                Toast.LENGTH_LONG).show();
                    } else {

                        String titleText = getString(R.string.email_dialog);
                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(140, 140, 140));
                        SpannableStringBuilder color = new SpannableStringBuilder(titleText);
                        color.setSpan(foregroundColorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WebviewActivity.this);
                        builder.setTitle(getString(R.string.connect_net))
                                .setMessage(color)
                                /*   .setNegativeButton(getString(R.string.ok_btn), null)*/
                                .setCancelable(true)
                                .show();
                    }
                    return true;
                } else if (url.startsWith("fb:")) {
                    if (isNetworkStatusAvialable(getApplicationContext())) {
                        if (isAppInstalled(liContext, "com.facebook.orca") || isAppInstalled(liContext, "com.facebook.katana")
                                || isAppInstalled(liContext, "com.example.facebook") || isAppInstalled(liContext, "com.facebook.android")) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), (getString(R.string.facebook_view)),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), (getString(R.string.not_fb)),
                                    Toast.LENGTH_LONG).show();
                        }

                    } else {
                        String titleText = getString(R.string.fb_pro);
                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(140, 140, 140));
                        SpannableStringBuilder color = new SpannableStringBuilder(titleText);
                        color.setSpan(foregroundColorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WebviewActivity.this);
                        builder.setTitle(getString(R.string.connect_net))
                                .setMessage(color)
                                /*  .setNegativeButton(getString(R.string.ok_btn), null)*/
                                .setCancelable(true)
                                .show();
                    }
                    return true;
                } else {
                    view.loadUrl(url);
                    return true;
                }

            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                swipeRefreshLayout.setRefreshing(true);


            }

            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
                super.onPageFinished(view, url);
            }
        });
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

    /* @Override
     public boolean onPrepareOptionsMenu(Menu menu) {
         MenuItem menuItem = menu.findItem(R.id.notify);
         menuItem.setVisible(false);
         return true;
     }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_about_app:
                Intent h = new Intent(WebviewActivity.this, AboutApp.class);
                startActivity(h);
                return true;
            case R.id.pro:
                Intent p = new Intent(WebviewActivity.this, ProfileActivity.class);
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
                Intent h = new Intent(WebviewActivity.this, Home.class);
                startActivity(h);
                break;
            case R.id.Notification:
                Intent i = new Intent(getApplicationContext(), WebviewActivity.class);
                i.putExtra("url", (notice));
                startActivity(i);
                break;
            case R.id.alumni:
                Intent aa = new Intent(getApplicationContext(), WebviewActivity.class);
                aa.putExtra("url", (alumni));
                startActivity(aa);
                break;
            case R.id.adviser:
                String notify = getString(R.string.commingsoon);
                AlertDialog.Builder alert = new AlertDialog.Builder(WebviewActivity.this);
                alert.setMessage(notify)
                        /*.setNegativeButton(getString(R.string.ok_btn), null)*/
                        .setCancelable(true)
                        .show();
                break;
            case R.id.ExecutiveMember:
                Intent ie = new Intent(getApplicationContext(), WebviewActivity.class);
                ie.putExtra("url", (executive));
                startActivity(ie);
                break;
            case R.id.GeneralMember:
                Intent gi = new Intent(getApplicationContext(), WebviewActivity.class);
                gi.putExtra("url", (general));
                startActivity(gi);
                break;

            case R.id.nav_update:
                if (isNetworkStatusAvialable(getApplicationContext())) {
                    UpdateChecker.checkForDialog(WebviewActivity.this);
                } else {
                    String titleText = getString(R.string.check_new_update_error);
                    ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.rgb(140, 140, 140));
                    SpannableStringBuilder color = new SpannableStringBuilder(titleText);
                    color.setSpan(foregroundColorSpan, 0, titleText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebviewActivity.this);
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
                Intent a = new Intent(WebviewActivity.this, About.class);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebviewActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(WebviewActivity.this);
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
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedbackMail)});
                intent.putExtra(Intent.EXTRA_SUBJECT, (getString(R.string.feedbacksub)));
                intent.putExtra(Intent.EXTRA_TEXT, (getString(R.string.msg_feedback)));
                startActivity(Intent.createChooser(intent, (getString(R.string.feedTitle))));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
