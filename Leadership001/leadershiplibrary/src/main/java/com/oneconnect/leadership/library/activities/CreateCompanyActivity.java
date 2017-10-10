package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class CreateCompanyActivity extends AppCompatActivity implements CompanyContract.View {


    ImageView companyLogoIMG;
    EditText companyName, companyEmail, companyAddress;
    TextView txtColor1, txtColor2;
    Button createCompanyBtn;
    CompanyPresenter presenter;
    Context ctx;
    CompanyDTO company;

    private int RESULT_LOAD_IMG = 201;
    FirebaseStorageAPI fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_company);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        companyLogoIMG = (ImageView) findViewById(R.id.companyLogoIMG);

        companyName = (EditText) findViewById(R.id.companyName);
        companyEmail = (EditText) findViewById(R.id.companyEmail);
        companyAddress = (EditText) findViewById(R.id.companyAddress);
        /*txtColor1 = (TextView) findViewById(R.id.txtColor1);
        txtColor2 = (TextView) findViewById(R.id.txtColor2);*/
        createCompanyBtn = (Button) findViewById(R.id.createCompanyBtn);
        createCompanyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCompany();
               /* Util.flashOnce(createCompanyBtn, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                      //  openInGallery();
                        createCompany();
                    }
                });*/
            }
        });

        companyLogoIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Logo selected, now open image gallery");
                Intent photoPickerIntent = new Intent();
                photoPickerIntent.setType("image/*");
                photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
                // photoPickerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // startActivity(photoPickerIntent);
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), RESULT_LOAD_IMG);
               /* Util.flashOnce(companyLogoIMG, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        *//*Intent i = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMG);*//*
                        *//*Log.i(TAG, "Logo selected, now open image gallery");
                        Intent photoPickerIntent = new Intent();
                        photoPickerIntent.setType("image*//**//*");
                        photoPickerIntent.setAction(Intent.ACTION_VIEW);
                        photoPickerIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(photoPickerIntent);*//*
                       // startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), RESULT_LOAD_IMG);
                    }
                });*/
            }
        });
        presenter = new CompanyPresenter(this);

        fbs = new FirebaseStorageAPI();
    }
    String imagePath;

    public void openInGallery(/*String imageId*/) {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon()/*.appendPath(imageId)*/.build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void createCompany() {
        Log.i(TAG, "Creating new Company....");
        if (companyName.getText().toString() == null) {
            Toast.makeText(this, "You did not enter company name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (companyEmail.getText().toString() == null) {
            Toast.makeText(this, "You did not enter company email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (companyAddress.getText().toString() == null) {
            Toast.makeText(this, "You did not enter company address", Toast.LENGTH_SHORT).show();
            return;
        }

        final PhotoDTO p = new PhotoDTO();
        if (selectedImagePath != null) {
            p.setFilePath(selectedImagePath);
        }
        File file = new File(selectedImagePath);
        p.setImageSize(file.length());
        p.setBytes(file.length());
        if (file.getName() != null) {
            p.setCaption(file.getName());
        } else {
            p.setCaption(companyName.getText() + "Logo");
        }
        if (file != null) {
            fbs.uploadPhoto(p, new FirebaseStorageAPI.StorageListener() {
                @Override
                public void onResponse(String key) {
                    Log.i(TAG, "onResponse: " + key);
                    company = new CompanyDTO();
                    company.setCompanyName(companyName.getText().toString());
                    company.setEmail(companyEmail.getText().toString());
                    company.setAddress(companyAddress.getText().toString());
                    company.setPrimaryColor(Color.RED);
                    if (p != null) {
                        company.setPhoto(p);
                    }

                    presenter.createCompany(company);
                }

                @Override
                public void onProgress(long transferred, long size) {

                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, message);
                }
            });
        } else {
            Log.i(TAG, "creating company with no photo");
            company = new CompanyDTO();
            company.setCompanyName(companyName.getText().toString());
            company.setEmail(companyEmail.getText().toString());
            company.setAddress(companyAddress.getText().toString());
            presenter.createCompany(company);
        }


    }

    private String selectedImagePath;

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            /*if (resultCode == RESULT_LOAD_IMG) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }*/
            try {
                //
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                companyLogoIMG.setImageBitmap(selectedImage);
                selectedImagePath = imageUri.getPath();
                getPath(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }


    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }


    @Override
    public void onCompanyFound(CompanyDTO company) {

    }

    @Override
    public void onError(String message) {
        Log.e(TAG, message);
    }

    Snackbar snackbar;

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(companyLogoIMG, title, Snackbar.LENGTH_INDEFINITE);
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
    public void onCompanyNotFound() {

    }

    @Override
    public void onCompanyUpdated(CompanyDTO company) {

    }

    @Override
    public void onUserFound(UserDTO user) {

    }

    @Override
    public void onCompanyCreated(CompanyDTO company) {
        showSnackbar("companyCreated: " + company.getCompanyName(), "DISMISS", "green");
        companyName.setText("");
        companyName.setHint("Enter Company Name");
        companyEmail.setText("");
        companyEmail.setHint("Enter Company Email");
        companyAddress.setText("");
        companyAddress.setHint("Enter Company Address");
        companyLogoIMG.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.coffee_screens));


       // finish();
    }

    static final String TAG = CreateCompanyActivity.class.getSimpleName();
}
