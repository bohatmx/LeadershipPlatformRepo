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
import android.view.View;
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
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.Util;

import es.dmoral.toasty.Toasty;

public class ColorPickerActivity extends AppCompatActivity {

    Context ctx;
    RelativeLayout layout;
    ColorPickerView color_picker_view;
    LightnessSlider v_lightness_slider;
    AlphaSlider v_alpha_slider;
    //CheckBox colorBox1, colorBox2;
    TextView colorBox1, colorBox2;
    private UserDTO user;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        layout = (RelativeLayout) findViewById(R.id.layout);
        color_picker_view = (ColorPickerView) findViewById(R.id.color_picker_view);
        v_lightness_slider = (LightnessSlider) findViewById(R.id.v_lightness_slider);
        v_alpha_slider = (AlphaSlider) findViewById(R.id.v_alpha_slider);
        colorBox1 = (TextView) findViewById(R.id.colorBox1);
        colorBox2 = (TextView) findViewById(R.id.colorBox2);


        if (getIntent().getSerializableExtra("user") != null) {
            type = ResponseBag.USERS;
            user = (UserDTO) getIntent().getSerializableExtra("user");
          //  getSupportActionBar().setSubtitle(user.getFullName());
        }
        colorBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(colorBox1, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        colorBox1.setBackgroundColor(color_picker_view.getSelectedColor());
                    }
                });
            }
        });

        colorBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(colorBox2, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        colorBox2.setBackgroundColor(color_picker_view.getSelectedColor());
                    }
                });
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout.setBackgroundColor(color_picker_view.getSelectedColor());
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

    //    showDialog();
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

}
