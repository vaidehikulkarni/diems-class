package org.diems.diemsapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UploadNotice extends Fragment {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect, btnSubmit;
    private ImageView ivImage;
    private String userChoosenTask;
    private EditText title, body;
    private MaterialBetterSpinner classsp, division, branch;
    private Bitmap bm;
    private View view;
    private ProgressDialog dialog;
    private boolean responseReceived;
    private boolean fromCamera;
    private String image;
    private TextView endDate,error;

    public UploadNotice() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.actionBar.setTitle("Upload Notice");

        view = inflater.inflate(R.layout.fragment_upload_notice, container, false);
        btnSelect = (Button) view.findViewById(R.id.btnSelectPhoto);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        classsp = (MaterialBetterSpinner) view.findViewById(R.id.classSpinner);
        division = (MaterialBetterSpinner) view.findViewById(R.id.divisionSpinner);
        branch = (MaterialBetterSpinner) view.findViewById(R.id.branchSpinner);
        title = (EditText) view.findViewById(R.id.noticeTitle);
        body = (EditText) view.findViewById(R.id.noticeBody);
        endDate = (TextView) view.findViewById(R.id.endDate);
        error = (TextView) view.findViewById(R.id.errorUpload);

        String[] divisionArray = {"All", "1", "2", "3", "4", "5", "6", "7", "8"};
        String[] branchArray = {"All", "FE", "CSE", "Mech", "E&TC", "Civil"};
        String[] classArray = {"All", "FE", "SE", "TE", "BE"};

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, divisionArray);
        division.setAdapter(aa);
        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, branchArray);
        branch.setAdapter(aa);
        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, classArray);
        classsp.setAdapter(aa);

        btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                boolean cont = true;
                String errorString = "";
                if (title.getText().toString().trim().equals("")) {
                    title.setError("Title is required");
                    cont = false;
                }

                if (mCurrentPhotoPath == null || mCurrentPhotoPath.equals("")) {
                    errorString += "Please Select Photo";
                    cont = false;
                }
                else
                    error.setVisibility(View.GONE);

                if (division.getText().toString().equals("")) {
                    division.setError("Division is required");
                    cont = false;
                }

                if (branch.getText().toString().equals("")) {
                    branch.setError("Branch is required");
                    cont = false;
                }

                if (classsp.getText().toString().equals("")) {
                    classsp.setError("Class is required");
                    cont = false;
                }

                if(endDate.getText().equals(""))
                {
                    if(!errorString.equals(""))
                        errorString += "\n";
                    errorString += "Please Select End Date";
                    cont = false;
                }
                else
                    error.setVisibility(View.GONE);

                if (!cont)
                {
                    if(!errorString.equals(""))
                    {
                        error.setText(errorString);
                        error.setVisibility(View.VISIBLE);
                    }
                    else
                        error.setVisibility(View.GONE);
                    return;
                }


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (fromCamera) {
                    BitmapFactory
                            .decodeFile(mCurrentPhotoPath)
                            .compress(Bitmap.CompressFormat.JPEG, 90, baos);
                    byte[] b = baos.toByteArray();
                    image = Base64.encodeToString(b, Base64.DEFAULT);
                } else {
                    bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                    byte[] b = baos.toByteArray();
                    image = Base64.encodeToString(b, Base64.DEFAULT);
                }

                responseReceived = false;
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Loading");
                dialog.show();

                final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!responseReceived) {
                            requestQueue.cancelAll("timeout");
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Error occured. Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 15000);

                try {
                    String URL = MainActivity.IP + "/notices" + "?access_token=" + MainActivity.accessToken;
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("title", title.getText().toString().trim());
                    jsonBody.put("body", body.getText().toString().trim());
                    jsonBody.put("image", image);
                    jsonBody.put("class", classsp.getText().toString());
                    jsonBody.put("branch", branch.getText().toString());
                    jsonBody.put("division", division.getText().toString());
                    jsonBody.put("u_type", MainActivity.loginType);
                    jsonBody.put("end_date", endDate.getText().toString());

                    JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responseReceived = true;
                            dialog.dismiss();
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.login, new AdminDashBoard())
                                    .commit();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error Occured. Please try again", Toast.LENGTH_SHORT).show();
                            Log.e("Volley Error", error.getMessage());
                        }
                    });

                    requestQueue.add(json_request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();
        Button selectDate = (Button) view.findViewById(R.id.selectDate);

        selectDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            }
        });
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        dispatchTakePictureIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        dispatchTakePictureIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                try {
                    onCaptureImageResult(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void onCaptureImageResult(Intent data) throws IOException {

        fromCamera = true;
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        ivImage.setImageURI(contentUri);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        fromCamera = false;
        bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);

    }

    private void dispatchTakePictureIntent() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "org.diems.diemsapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    String mCurrentPhotoPath;
    String mCurrentPhotoName;

    private File createImageFile() throws IOException {
        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date());
        String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        mCurrentPhotoName = imageFileName;
        return image;
    }
}
