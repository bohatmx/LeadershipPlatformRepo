package com.oneconnect.leadership.library.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.flask.colorpicker.slider.AlphaSlider;
import com.flask.colorpicker.slider.LightnessSlider;
import com.google.firebase.auth.FirebaseAuth;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.cache.CacheContract;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Util;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ColorPickerActivity extends AppCompatActivity implements CompanyContract.View{

    Context ctx;
    RelativeLayout layout;
    ColorPickerView color_picker_view;
    LightnessSlider v_lightness_slider;
    AlphaSlider v_alpha_slider;
    //CheckBox colorBox1, colorBox2;
    TextView colorBox1, colorBox2, companyNameTxt, textColor2, textColor1;
    private UserDTO user;
    private int type, color1, color2;
    CompanyDTO company;
    CompanyPresenter companyPresenter;
    Button updateBTN;
    private FirebaseAuth firebaseAuth;
    int themeDarkColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);
        Log.i(TAG, "*** onCreate ***");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        firebaseAuth = FirebaseAuth.getInstance();

        companyPresenter = new CompanyPresenter(this);

        themeDarkColor = getIntent().getIntExtra("darkColor", R.color.absa_red);

        layout = (RelativeLayout) findViewById(R.id.layout);
        color_picker_view = (ColorPickerView) findViewById(R.id.color_picker_view);
        v_lightness_slider = (LightnessSlider) findViewById(R.id.v_lightness_slider);
        v_alpha_slider = (AlphaSlider) findViewById(R.id.v_alpha_slider);
        colorBox1 = (TextView) findViewById(R.id.colorBox1);
        colorBox2 = (TextView) findViewById(R.id.colorBox2);
        companyNameTxt = (TextView) findViewById(R.id.companyNameTxt);
        updateBTN = (Button) findViewById(R.id.updateColors);
        textColor2 = (TextView) findViewById(R.id.textColor2);
        textColor1 = (TextView) findViewById(R.id.textColor1);

        if (getIntent().getSerializableExtra("user") != null) {
            type = ResponseBag.USERS;
            user = (UserDTO) getIntent().getSerializableExtra("user");
            companyNameTxt.setText(user.getCompanyName());
        } /*else {
            companyPresenter.getUser(firebaseAuth.getCurrentUser().getEmail());
        }*/
        if (getIntent().getSerializableExtra("company") != null) {
            type = ResponseBag.COMPANIES;
            company = (CompanyDTO) getIntent().getSerializableExtra("company");
            companyNameTxt.setText(company.getCompanyName());
        } else {
            Log.i(TAG, "fetching Company");
            companyPresenter.getCompany(user.getCompanyID());
        }
        colorBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBox1.setBackgroundColor(color_picker_view.getSelectedColor());
                textColor1.setTextColor(color_picker_view.getSelectedColor());
                company.setPrimaryColor(textColor1.getCurrentTextColor());
                companyPresenter.updateCompany(company);

            }
        });

        colorBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorBox2.setBackgroundColor(color_picker_view.getSelectedColor());
                textColor2.setTextColor(color_picker_view.getSelectedColor());
                company.setPrimaryColor(textColor2.getCurrentTextColor());
                companyPresenter.updateCompany(company);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // layout.setBackgroundColor(color_picker_view.getSelectedColor());
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        updateBTN.setVisibility(View.GONE);

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCompanyColors(company);
            }
        });

    //    showDialog();
    }

    private void updateCompanyColors(CompanyDTO company) {
        Log.i(TAG, "updating company colors");
        if (textColor1.getCurrentTextColor() != 0) {
            company.setPrimaryColor(textColor1.getCurrentTextColor());
        }

      //  company.setPrimaryColor(color1);
        if (textColor2.getCurrentTextColor() != 0) {
            company.setSecondaryColor(textColor2.getCurrentTextColor());
        }

       // company.setSecondaryColor(color2);
        companyPresenter.updateCompany(company);
    }

    private void showDialog() {
        ColorPickerDialogBuilder
                .with(ctx)
                .setTitle("Choose color")
                .initialColor(Color.BLUE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toasty.success(ctx, getString(R.string.success),
                                Toast.LENGTH_LONG, true).show();
                        Toasty.info(ctx, "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG, true).show();
                      //  Toasty.(ctx, "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        layout.setBackgroundColor(selectedColor);
                        Color.red(selectedColor);
                       // changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    @Override
    public void onCompanyFound(CompanyDTO comp) {
        Log.i(TAG, "*** onCompanyFound: " + comp.getCompanyName());
        company = comp;
        companyNameTxt.setText(company.getCompanyName());

        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "updating company colors");
                color1 = color_picker_view.getSelectedColor();
                color2 = color_picker_view.getSelectedColor();
                company.setPrimaryColor(color1);
                company.setSecondaryColor(color2);
                companyPresenter.updateCompany(company);
               // updateCompanyColors(company);
                /*Util.flashOnce(updateColors, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        updateCompanyColors(company);
                    }
                });*/
            }
        });


    }

    Snackbar snackbar;


    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(color_picker_view, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }


    @Override
    public void onError(String message) {
        showSnackbar(message, "DISMISS", "red");
    }

    @Override
    public void onCompanyNotFound() {

    }

    boolean themeChanged;

    @Override
    public void onCompanyUpdated(CompanyDTO company) {
       // showSnackbar(company.getCompanyName() + " colors updated ", "DISMISS", "green");
        Toasty.success(ctx, "color updated", Toast.LENGTH_SHORT, true).show();
        themeChanged = true;
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "%%% onBackPressed, themeChanged: " + themeChanged);
        if (themeChanged) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public void onUserFound(UserDTO user) {
        Log.i(TAG, "*** onUserFound ***" + user.getFullName() + "\n fetching company");
        companyPresenter.getCompany(user.getCompanyID());
    }

    @Override
    public void onCompanyCreated(CompanyDTO company) {

    }

    static final String TAG = ColorPickerActivity.class.getSimpleName();
}
