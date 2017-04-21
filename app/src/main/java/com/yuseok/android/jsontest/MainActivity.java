package com.yuseok.android.jsontest;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final int REQ_CODE = 100;

    Retrofit retrofit;

    APIService.mapAndUser mapAndUserService;

    String token;
    String keyword;

    EditText getJson;
    TextView textView;
    TextView textView2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 버전체크해서 마시말로우 보다 낮으면 런타임권한 체크를 하지 않는다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            loadData();
        }

//        // 버전체크해서 마시말로우 보다 낮으면 런타임권한 체크를 하지 않는다
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkPermission();
//        } else {
//            loadData();
//        }



    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        retrofit = new Retrofit.Builder()
                .baseUrl(APIService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();


        token = "token "+ "d3429bc600e4515c37c73c8584936cf58af1ea8a";


        getJson = (EditText) findViewById(R.id.GET);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);

        keyword = getJson.getText().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMapAndUser();
            }
        });

    }

    public void getMapAndUser() {
        mapAndUserService = retrofit.create(APIService.mapAndUser.class);
        Call<MapAndUser> retroDataCall = mapAndUserService.getMapAndUer(token, getJson.getText().toString());
        retroDataCall.enqueue(new Callback<MapAndUser>() {
            @Override
            public void onResponse(Call<MapAndUser> call, Response<MapAndUser> response) {
                if (response.code() == 200) {
                    MapAndUser mapAndUser = response.body();
                    textView.setText(mapAndUser.getMaps().toString());
                    textView2.setText(mapAndUser.getUsers().toString());
                }
                Log.i("Response : ", "Data" + response.toString());
            }

            @Override
            public void onFailure(Call<MapAndUser> call, Throwable t) {
                Log.e("MapData", "오류데이터" + t.getMessage());
            }
        });
    }


//    public void failAlert() {
//        // - 팝업창 만들기
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//        // 팝업창 제목
//        alertDialog.setTitle("네트워크 오류");
//        // 팝업 메시지
//        alertDialog.setMessage("네트워크에 연결되지 않았거나 \n 데이터를 불러올 수 없습니다");
//        // NO 버튼 기능
//        alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.cancel();
//                finish();
//            }
//        });
//        alertDialog.show();
//    }
//
//    private void checkPermission() {
//        //버전 체크해서 마시멜로우(6.0)보다 낮으면 런타임 권한 체크를 하지않는다.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (PermissionControl.checkPermssion(this, REQ_PERMISSION)) {
////                init();
//            }
//        } else {
////            init();
//        }
//    }
//
//    //권한체크 후 콜백< 사용자가 확인후 시스템이 호출하는 함수
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQ_PERMISSION) {
//            //배열에 넘긴 런타임 권한을 체크해서 승인이 됐으면
//            if (PermissionControl.onCheckedResult(grantResults)) {
////                init();
//            } else {
//                Toast.makeText(this, "권한을 사용하지 않으시면 프로그램을 실행시킬수 없습니다", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }
//    }

    // 버전체크해서 마시말로우 보다 낮으면 런타임권한 체크를 하지 않는다
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        checkPermission();
//    } else {
////        loadData();
//    }


    // 1. 권한체크
    @TargetApi(Build.VERSION_CODES.M) // Target 지정 애너테이션
    private void checkPermission(){
        // 1.1 런타임 권한체크
        if( checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                ){
            // 1.2 요청할 권한 목록 작성
            String permArr[] = {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.INTERNET
                    , Manifest.permission.READ_CONTACTS};
            // 1.3 시스템에 권한요청
            requestPermissions(permArr, REQ_CODE);
        }else{
            loadData();
        }
    }

    // 2. 권한체크 후 콜백 < 사용자가 확인후 시스템이 호출하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_CODE){
            // 2.1 배열에 넘긴 런타임권한을 체크해서 승인이 됬으면
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                // 2.2 프로그램 실행
                loadData();
            }else{
                Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_LONG).show();
                // 선택 : 1 종료, 2 권한체크 다시 물어보기
                finish();
            }
        }
    }

    // 3. 데이터 읽어오기 (시스템 실행)
    private void loadData(){
        Toast.makeText(this, "프로그램을 실행합니다", Toast.LENGTH_SHORT).show();
//
//        // 3.1 데이터를 불러온다
//        DataLoader loader = new DataLoader(this);
//        loader.load();
//        ArrayList<Contact> datas = loader.get();
//
//        // 리사이클러뷰 세팅
//        RecyclerView listView = (RecyclerView) findViewById(R.id.listView);
//        RecyclerAdapter adapter = new RecyclerAdapter(datas, this);
//        listView.setAdapter(adapter);
//        listView.setLayoutManager(new LinearLayoutManager(this));

    }
}
