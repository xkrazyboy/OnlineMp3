package myapp.onlinemp3.MP3;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import myapp.onlinemp3.AsyncTask.LoadAbout;
import myapp.onlinemp3.Interfaces.AboutListener;
import myapp.onlinemp3.R;
import myapp.onlinemp3.Utils.Constant;
import myapp.onlinemp3.Utils.DBHelper;
import myapp.onlinemp3.Utils.JsonUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    DBHelper dbHelper;
    WebView webView;
    TextView textView_appname, textView_email, textView_website, textView_company, textView_contact, textView_version;
    ImageView imageView_logo;
    LinearLayout ll_email, ll_website, ll_company, ll_contact;
    String website, email, desc, applogo, appname, appversion, appauthor, appcontact, privacy, developedby;
    ProgressDialog pbar;
    JsonUtils utils;
    LoadAbout loadAbout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        dbHelper = new DBHelper(this);
        utils = new JsonUtils(this);
        utils.forceRTLIfSupported(getWindow());
        utils.setStatusColor(getWindow());

        toolbar = this.findViewById(R.id.toolbar_about);
        toolbar.setTitle(getString(R.string.menu_about));
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pbar = new ProgressDialog(this);
        pbar.setMessage(getResources().getString(R.string.loading));
        pbar.setCancelable(false);

        webView = findViewById(R.id.webView);
        textView_appname = findViewById(R.id.textView_about_appname);
        textView_email = findViewById(R.id.textView_about_email);
        textView_website = findViewById(R.id.textView_about_site);
        textView_company = findViewById(R.id.textView_about_company);
        textView_contact = findViewById(R.id.textView_about_contact);
        textView_version = findViewById(R.id.textView_about_appversion);
        imageView_logo = findViewById(R.id.imageView_about_logo);

        ll_email = findViewById(R.id.ll_email);
        ll_website = findViewById(R.id.ll_website);
        ll_contact = findViewById(R.id.ll_contact);
        ll_company = findViewById(R.id.ll_company);

        dbHelper.getAbout();
        if (JsonUtils.isNetworkAvailable(AboutActivity.this)) {
            loadAbout = new LoadAbout(new AboutListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onEnd(Boolean success) {
                    if (success) {
                        setVariables();
                        dbHelper.addtoAbout();
                    }
                }
            });
            loadAbout.execute(Constant.URL_APP_DETAILS);
        } else {
            if (dbHelper.getAbout()) {
                setVariables();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    public void setVariables() {
        appname = Constant.itemAbout.getAppName();
        applogo = Constant.itemAbout.getAppLogo();
        desc = Constant.itemAbout.getAppDesc();
        appversion = Constant.itemAbout.getAppVersion();
        appauthor = Constant.itemAbout.getAuthor();
        appcontact = Constant.itemAbout.getContact();
        email = Constant.itemAbout.getEmail();
        website = Constant.itemAbout.getWebsite();
        privacy = Constant.itemAbout.getPrivacy();
        developedby = Constant.itemAbout.getDevelopedby();

        textView_appname.setText(appname);
        if (!email.trim().isEmpty()) {
            ll_email.setVisibility(View.VISIBLE);
            textView_email.setText(email);
        }

        if (!website.trim().isEmpty()) {
            ll_website.setVisibility(View.VISIBLE);
            textView_website.setText(website);
        }

        if (!appauthor.trim().isEmpty()) {
            ll_company.setVisibility(View.VISIBLE);
            textView_company.setText(appauthor);
        }

        if (!appcontact.trim().isEmpty()) {
            ll_contact.setVisibility(View.VISIBLE);
            textView_contact.setText(appcontact);
        }

        if (!appversion.trim().isEmpty()) {
            textView_version.setText(appversion);
        }

        if (applogo.trim().isEmpty()) {
            imageView_logo.setVisibility(View.GONE);
        } else {
            Picasso
                    .get()
                    .load(Constant.URL_ABOUT_US_LOGO + applogo)
                    .into(imageView_logo);
        }

        String mimeType = "text/html;charset=UTF-8";
        String encoding = "utf-8";

        String text = "<html><head>"
                + "<style> body{color:#fff !important;text-align:left}"
                + "</style></head>"
                + "<body>"
                + desc
                + "</body></html>";


        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadData(text, mimeType, encoding);
    }
}
