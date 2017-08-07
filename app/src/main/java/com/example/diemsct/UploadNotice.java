package com.example.diemsct;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class UploadNotice extends Fragment {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect, btnSubmit;
    private ImageView ivImage;
    private String userChoosenTask;
    private EditText title, body;
    private MaterialBetterSpinner classsp, division, branch;
    private File destination;
    private Bitmap bm;
    private View view;
    private ProgressDialog dialog;
    private boolean responseReceived;

    public UploadNotice() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_notice, container, false);
        btnSelect = (Button) view.findViewById(R.id.btnSelectPhoto);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        classsp = (MaterialBetterSpinner) view.findViewById(R.id.classSpinner);
        division = (MaterialBetterSpinner) view.findViewById(R.id.divisionSpinner);
        branch = (MaterialBetterSpinner) view.findViewById(R.id.branchSpinner);
        title = (EditText) view.findViewById(R.id.noticeTitle);
        body = (EditText) view.findViewById(R.id.noticeBody);

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
                if (title.getText().toString().trim().equals("")) {
                    title.setError("Title is required");
                    return;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if(bm == null)
                {
                    Toast.makeText(getActivity(), "Please select photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                String image = Base64.encodeToString(b, Base64.DEFAULT);

                responseReceived = false;
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Loading");
                dialog.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!responseReceived)
                            if(!responseReceived)
                            {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Error occured: 500", Toast.LENGTH_SHORT).show();
                            }
                    }
                },5000);
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    String URL = getString(R.string.IP) + "/notice" + "?access_token=" + MainActivity.accessToken;
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("title", title.getText().toString().trim());
                    jsonBody.put("body", body.getText().toString().trim());
                    jsonBody.put("image", image);
                    jsonBody.put("class", classsp.getText().toString());
                    jsonBody.put("branch", branch.getText().toString());
                    jsonBody.put("division", division.getText().toString());

                    JsonObjectRequest json_request = new JsonObjectRequest(URL, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            responseReceived = true;
                            dialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            body.setText("Error occured" + error.toString());
                        }
                    });

                    requestQueue.add(json_request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                        cameraIntent();
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
                        cameraIntent();

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

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            startActivityForResult(intent, REQUEST_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        bm = thumbnail;

        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        thumbnail = Bitmap.createScaledBitmap(thumbnail, 2000, 2000, true);
//        ivImage.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ivImage.setImageBitmap(thumbnail);
//        Matrix matrix = new Matrix();

//        matrix.postRotate(90);

//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbnail, 2700, 1800, true);
//        Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbnail, 400, 600, true);
//        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
//        ivImage.setImageBitmap(rotatedBitmap);
//        btnContinue.setVisibility(View.VISIBLE);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

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

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //resultText.setText("Hello, " + editText.getText());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
