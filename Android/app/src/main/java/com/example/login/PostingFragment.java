package com.example.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.login.Data.PostingData;
import com.example.login.network.NetworkTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PostingFragment extends Fragment {
    private Button btn_cancel;
    private Button btn_apply;
    private EditText et_title;
    private EditText et_posting;
    private ImageButton ib_add_photo;
    private String title, name, date, contents;
    Fragment fragment_board;
    MainPageActivity activity;
    Context context;
    private Button btn_camera;
    private Button btn_album;
    private PopupWindow ll_chooser;
    String str_CurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO=99;
    final static int PICK_FROM_ALBUM=100;

    private SharedPreferences sharedPreferences_fragment_move;
    private String str_fragment_move="";
    private Fragment fragment_sell_list;
    private Fragment fragment_trade_list;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        //????????? ???????????? ????????? ?????? ?????????
        activity = (MainPageActivity) getActivity();
        title ="";
        name = "";
        date = "";
        contents = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        btn_cancel=(Button)view.findViewById(R.id.btn_cancel);
        btn_apply=(Button)view.findViewById(R.id.btn_apply);
        et_title=(EditText)view.findViewById(R.id.et_title);
        et_posting=(EditText)view.findViewById(R.id.et_posting);
        ib_add_photo = (ImageButton)view.findViewById(R.id.ib_add_photo);
        fragment_board=new BoardFragment();
        SimpleDateFormat format = new SimpleDateFormat("yyyy??? MM??? dd??? HH???mm???ss???");
        Calendar time = Calendar.getInstance();

        fragment_sell_list = new SellListFragment();
        fragment_trade_list = new TradeListFragment();
        sharedPreferences_fragment_move = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        str_fragment_move = sharedPreferences_fragment_move.getString("fragment_move","");
        Log.w("PostingFragment",str_fragment_move);


//        if(activity.mBundle != null) {
//            Bundle bundle = activity.mBundle;
//            receiveData = bundle.getString("sendData");
//            StudentDTO student3 = (StudentDTO) bundle.getSerializable("student3");
//            String name = student3.getName();
//            int age = student3.getAge();
//            int index = bundle.getInt("index");
//
//            textView1.append(receiveData + "\n");
//            textView1.append("name : " + name + "\nage : " + age + "\nindex : " + index);
//            activity.mBundle = null;
//        }

        if(str_fragment_move.equals("TradeListFragment") || str_fragment_move.equals("SellListFragment")){
            try{
                JSONObject result = GetPostingData(GetBundleData());

                // ????????? ???????????? ???????????? ???????????????
                et_title.setText(result.getString("title"));
                et_title.setEnabled(false);
                et_posting.setText(result.getString("content"));
                et_posting.setEnabled(false);
                ib_add_photo.setEnabled(false);
                btn_apply.setVisibility(View.GONE);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        //<---------------------------------------------------??????-------------------------------------------------->
        // ?????? ?????? ????????? ??? ?????? ??????
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(str_fragment_move.equals("SellListFragment")){
                    ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_sell_list).commit();
                }
                else if(str_fragment_move.equals("TradeListFragment")){
                    ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_trade_list).commit();
                }
                else {
                    ((MainPageActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_board).commit();
                }
            }
        });

        //?????? ?????? ????????? ??? ?????? ??????
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send_title = et_title.getText().toString();
                String send_posting = et_posting.getText().toString();

                if(send_posting.equals("") && send_title.equals("")){
                    Toast.makeText((MainPageActivity)getActivity(), "????????? ????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else if(send_title.equals("")){
                    Toast.makeText((MainPageActivity)getActivity(), "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else if(send_posting.equals("")){
                    Toast.makeText((MainPageActivity)getActivity(), "????????? ??????????????????", Toast.LENGTH_SHORT).show();
                }
                else{
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("title",et_title.getText() );
                        jsonObject.put("body", et_posting.getText());
                        String date_posting = format.format(time.getTime());
                        //sendPosting(send_title, send_posting, date_posting);
//                        saveText(et_title.getText().toString(), et_posting.getText().toString(), date_posting);        //????????? sharedpreference??? ??????
//                        String tmpPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/mino.jpg";
//                        sendImageFile(send_title,send_posting,date_posting,10000,tmpPath);
                        sendImageFile(send_title,send_posting,date_posting,10000,str_CurrentPhotoPath);

                        ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_board).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        ib_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ?????????/?????? ?????? ??????
                checkPermissions();

                // ????????? ????????? ?????????/?????? ???????????????
                setPopupChooser();

                // TODO : ?????? ??????, ?????? ??????, ?????? ????????? ?????? ?????? - ??????
            }
        });

        // Inflate the layout for this fragment
        return view;

    }
    private void sendPosting(String title, String body, String date) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("body", body);
            jsonObject.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkTask networkTask = new NetworkTask((MainPageActivity)getActivity(), "http://3.35.48.170:3000/singin/submit", jsonObject, "POST");   //????????? url ??????
        try {
            JSONObject resultObject = new JSONObject(networkTask.execute().get());
            if (resultObject == null) {
                Log.d("fail", "?????? ??????");
                return;
            }
            String resultString = resultObject.getString("msg");
            if (resultString.equals("Posting Success")) {
                ((MainPageActivity)getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment_board).commit();
            } else {
                Toast.makeText((MainPageActivity)getActivity(), "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (
                ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendImageFile(String title, String body, String date,int price,String path){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("body", body);
            jsonObject.put("price",price);
            jsonObject.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkTask fileNetworkTask = new NetworkTask(((MainPageActivity)getActivity()).getApplicationContext(),"http://3.35.48.170:3000/SellingRegister/Register",jsonObject,path,"FILE");

        try {
            JSONObject resultFileObject = new JSONObject(fileNetworkTask.execute().get());
            // resultFileObjet = "{"msg":"Register success"}"
            System.out.println(resultFileObject);

            if (resultFileObject.getString("msg").equals("Register success")){
                Toast.makeText(getActivity(), resultFileObject.getString("msg"), Toast.LENGTH_SHORT).show();
            }
            else if(resultFileObject == null){
                Log.w("?????? ??????","?????? ??????");
            }
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void saveText(String title, String name, String date){
        String temp = PostingData.getArray(getActivity());
        JSONObject jsonObject = new JSONObject();
        if(temp != "empty")
        {
            try{
                JSONArray jsonTemp = new JSONArray(temp);
                try{
                    jsonObject.put("name",name);
                    jsonObject.put("title",title);
                    jsonObject.put("date",date);
                    jsonTemp.put(jsonObject);
                    Log.d("test ????????? ??????",jsonTemp.toString());
                    //PostingData.setArray(getActivity(),jsonTemp.toString());
                }catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            JSONArray jsonTemp = new JSONArray();
            try{
                jsonObject.put("name",name);
                jsonObject.put("title",title);
                jsonObject.put("date",date);
                jsonTemp.put(jsonObject);
                //PostingData.setArray(getActivity(),jsonTemp.toString());
            }catch(JSONException e)
            {
                e.printStackTrace();
            }}
    }

    public void checkPermissions() {
        String permission_camera = Manifest.permission.CAMERA;
        String permission_write_external_storage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String[] permissions = {permission_camera, permission_write_external_storage};

        int PERMISSION_ALL = 1;

        // checkSelfPermission??? API 23?????? ??????, ???????????????(Build.VERSION_CODES.M)??? API 23???
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(permission_camera) == PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(permission_write_external_storage) == PackageManager.PERMISSION_GRANTED) {
                Log.w("???????????? ??????", "?????? ?????? ??????");
            } else {
                Log.w("???????????? ??????", "?????? ?????? ??????");
                ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_ALL);
            }
        }
    }

    public void setPopupChooser(){
        View popupView = getLayoutInflater().inflate(R.layout.popupwindow_chooser,null);
        ll_chooser = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        ll_chooser.setFocusable(true);
        ll_chooser.showAtLocation(popupView, Gravity.CENTER,0,0);

        btn_camera = (Button)ll_chooser.getContentView().findViewById(R.id.btn_camera);
        btn_album = (Button)ll_chooser.getContentView().findViewById(R.id.btn_album);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromCamera();

                ll_chooser.dismiss();
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();

                ll_chooser.dismiss();
            }
        });
    }

    // requestPermissions ????????? ???????????? onRequestPermissionsResult ????????? ?????? ??????
    // ?????? ??????
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // permissions[0] == Manifest.permission.CAMERA , permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE
        // grantResults[0] => permissions[0]??? ????????? ?????? ?????? , grantResults[1] => permissions[1]??? ????????? ?????? ??????
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w("???????????? ???????????? ??????", "onRequestPermissionsResult");

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            // PERMISSION_GRANTED == 0 , PERMISSION_DENIED == -1
            Log.w("???????????? ??????", "Permission : " + permissions[0] + " was " + grantResults[0]);
            Log.w("???????????? ??????", "Permission : " + permissions[1] + " was " + grantResults[1]);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        File file_from_path = new File(str_CurrentPhotoPath);
                        Bitmap bitmap = (Bitmap) MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), Uri.fromFile(file_from_path));

                        if (bitmap != null) {
                            ib_add_photo.setImageBitmap(bitmap);
                        }
                    }
                    break;

                case PICK_FROM_ALBUM:
                    if (resultCode == RESULT_OK) {
                        InputStream is_album = getActivity().getContentResolver().openInputStream(intent.getData());
                        Bitmap bitmap = (Bitmap) BitmapFactory.decodeStream(is_album);
                        is_album.close();
                        ib_add_photo.setImageBitmap(bitmap);

                        // ????????? ????????? ?????? ??????
                        Uri uri_photo = intent.getData();
                        String str_path = getImagePath(uri_photo);

                        Toast.makeText((MainPageActivity) getActivity(), "?????? path : " + str_path, Toast.LENGTH_SHORT).show();
                    } else if (resultCode == RESULT_CANCELED) {
                        Toast.makeText((MainPageActivity) getActivity(), "?????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ???????????? ?????? ????????? ????????? ??????????????? ??????
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        str_CurrentPhotoPath = image.getAbsolutePath();

        Log.w("????????????", "imageFileName : " + imageFileName + ".jpg"); // ????????? ?????? ??????
        Log.w("????????????", "str_CurrentPhotoPath : " + str_CurrentPhotoPath); // ?????? ????????? ??????

        return image;
    }

    // ???????????? ????????? ?????? ??????
    private void getImageFromCamera() {
        Intent intent_take_picture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_take_picture.resolveActivity(getActivity().getPackageManager()) != null) {
            File file_image = null;
            try {
                file_image = createImageFile();
            } catch (Exception e) {
                Log.e("??????", "?????? ?????? ????????? ?????? ?????? ??????");
            }
            if (file_image != null) {
                Uri uri_image = FileProvider.getUriForFile(getActivity(), "com.example.login.fileprovider", file_image);

                intent_take_picture.putExtra(MediaStore.EXTRA_OUTPUT, uri_image);
                startActivityForResult(intent_take_picture, REQUEST_TAKE_PHOTO);
            }
        }
    }

    // ????????? ????????? ??????
    private void getImageFromAlbum() {
        Toast.makeText((MainPageActivity) getActivity(), "getImageFromAlbum() ??????", Toast.LENGTH_SHORT).show();

        Intent intent_album = new Intent(Intent.ACTION_PICK);
        intent_album.setType("image/*");
        intent_album.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent_album, "?????? ????????? '??????'??? ???????????????"), PICK_FROM_ALBUM);
    }

    // ????????? ????????? ????????? ??????
    public String getImagePath(Uri uri) {
        String[] data_album = {MediaStore.Images.Media.DATA};
        Cursor cursor_album = getActivity().getContentResolver().query(uri, data_album, null, null, null);
        int column_index = cursor_album.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor_album.moveToFirst();

        String imgPath = cursor_album.getString(column_index);

        str_CurrentPhotoPath = imgPath;

        return imgPath;
    }

    // ????????? ??????????????? ???????????? ?????? ??????_1 => boardId ????????????
    public int GetBundleData(){
        int boardId = 0;
        Bundle bundle = getArguments();
        boardId = bundle.getInt("boardId",0);

        Log.w("PostingFragment","GetBundleData : bundle(boardId : "+boardId+")");

        return boardId;
    }

    // ????????? ??????????????? ???????????? ?????? ??????_2 => boardId??? ????????? ???????????? ???????????? ????????? ????????????
    public JSONObject GetPostingData(int boardId){
        NetworkTask networkTask = new NetworkTask(getActivity().getApplicationContext(),"http://3.35.48.170:3000/board/detail?boardId="+boardId,"GET");
        JSONObject returnObject = null;

        try{
            JSONObject resultObject = new JSONObject(networkTask.execute().get());
            if(resultObject == null){
                Log.e("PostingFragment","????????????");
            }
            else{
                Log.w("PostingFragment",resultObject.toString());
                //{"msg":"success","post":{"boardId":1,"id":"philippe10@naver.com","title":"selling Bang","price":10000,"status":0,"content":"he is huge and beautiful","img":""}

                returnObject = resultObject.getJSONObject("post");
            }
        }catch(ExecutionException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }

        return returnObject;
    }
}