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
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UploadNotice extends Fragment {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    private String userChoosenTask;
    private EditText title, body;
    private Spinner classsp, division, branch;
    private Bitmap bm;
    private ProgressDialog dialog;
    private boolean fromCamera, fromGallery;
    private String image;
    private TextView endDate, error;
    private TextInputLayout titleLayout, branchLayout, divisionLayout, classLayout;

    public UploadNotice() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.actionBar.setTitle("Upload Notice");

        View view = inflater.inflate(R.layout.fragment_upload_notice, container, false);
        Button btnSelect = (Button) view.findViewById(R.id.btnSelectPhoto);
        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        classsp = (Spinner) view.findViewById(R.id.classSpinner);
        division = (Spinner) view.findViewById(R.id.divisionSpinner);
        branch = (Spinner) view.findViewById(R.id.branchSpinner);
        title = (EditText) view.findViewById(R.id.noticeTitle);
        body = (EditText) view.findViewById(R.id.noticeBody);
        endDate = (TextView) view.findViewById(R.id.endDate);
        error = (TextView) view.findViewById(R.id.errorUpload);
        titleLayout = (TextInputLayout) view.findViewById(R.id.titleLayout);
        branchLayout = (TextInputLayout) view.findViewById(R.id.branchLayout);
        divisionLayout = (TextInputLayout) view.findViewById(R.id.divisionLayout);
        classLayout = (TextInputLayout) view.findViewById(R.id.classLayout);

        String[] divisionArray = {"Division", "All", "1", "2", "3", "4", "5", "6", "7", "8"};
        String[] branchArray = {"Branch", "All", "FE", "CSE", "Mech", "E&TC", "Civil", "MBA", "Staff"};
        String[] classArray = {"Class", "All", "FE", "SE", "TE", "BE"};

        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, divisionArray);
        division.setAdapter(aa);
        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, branchArray);
        branch.setAdapter(aa);

//        branch.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
//        ArrayAdapter <String> br = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, branchArray);
//        br.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        branch.setAdapter(br);

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
                    titleLayout.setError("Title is required");
                    cont = false;
                }

                if ((mCurrentPhotoPath == null || mCurrentPhotoPath.equals("")) || !(fromCamera || fromGallery)) {
                    errorString += "Please Select Photo";
                    cont = false;
                }

                if (division.getSelectedItem().toString().equals("Division")) {
                    divisionLayout.setError("Division is required");
                    cont = false;
                }

                if (branch.getSelectedItem().toString().equals("Branch")) {
                    branchLayout.setError("Branch is required");
                    cont = false;
                }

                if (classsp.getSelectedItem().toString().equals("Class")) {
                    classLayout.setError("Class is required");
                    cont = false;
                }

//                if (endDate.getText().equals("")) {
//                    if (!errorString.equals(""))
//                        errorString += "\n";
//                    errorString += "Please Select End Date";
//                    cont = false;
//                }

                if (!cont) {
                    if (!errorString.equals("")) {
                        error.setText(errorString);
                        error.setVisibility(View.VISIBLE);
                    } else
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

                dialog = new ProgressDialog(getActivity());
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Loading");
                dialog.show();

                final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("title", title.getText().toString().trim());
                    jsonBody.put("body", body.getText().toString().trim());
                    jsonBody.put("image", image);
                    jsonBody.put("class", classsp.getSelectedItem().toString());
                    jsonBody.put("branch", branch.getSelectedItem().toString());
                    jsonBody.put("division", division.getSelectedItem().toString());
                    jsonBody.put("u_type", MainActivity.userData.getLoginType());
                    if (endDate.getText().toString().equals(""))
                        jsonBody.put("end_date", null);
                    else
                        jsonBody.put("end_date", formatDate(endDate.getText().toString(), "dd/mm/yyyy", "yyyy/mm/dd"));

                    String url = MainActivity.IP + "/api/notices?access_token=" + MainActivity.userData.getAccessToken();
                    final JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.login, new AdminDashBoard())
                                    .commit();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Error Occurred. Please try again", Toast.LENGTH_SHORT).show();
                            Log.e("Volley Error", error.getMessage());
                        }
                    });

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!json_request.hasHadResponseDelivered()) {
                                requestQueue.cancelAll(json_request);
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Error occurred. Please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 30000);

                    json_request.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(json_request);
                } catch (JSONException | ParseException e) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    e.printStackTrace();
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();
        Button selectDate = (Button) view.findViewById(R.id.selectDate);

        selectDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
        fromGallery = false;
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        ivImage.setImageURI(contentUri);
    }

    private void onSelectFromGalleryResult(Intent data) {

        fromCamera = false;
        fromGallery = true;
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

    static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);

        return formatter.format(initDate);
    }
}
