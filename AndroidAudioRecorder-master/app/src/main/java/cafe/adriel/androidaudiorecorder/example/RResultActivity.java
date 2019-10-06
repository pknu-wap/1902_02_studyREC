package cafe.adriel.androidaudiorecorder.example;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RResultActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> adapter;
    ListView listView;
    EditText ed;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rresult);

        // 내장메모리 용량기능
        TextView txtInterTotal = (TextView) findViewById(R.id.textview);
        TextView txtInterAvailTotal = (TextView) findViewById(R.id.textview2);

        txtInterTotal.setText("총 내장메모리 : " +
                formatSize(getTotalInternalMemorySize()));
        txtInterAvailTotal.setText("사용가능한 내장메모리 : " +
                formatSize(getInternalMemorySize()));

        //데이터 준비
        items = new ArrayList<String>();

        // 어댑터 생성
        adapter = new ArrayAdapter<String>(RResultActivity.this,
                android.R.layout.simple_list_item_single_choice, items);

        // 어댑터 설정
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나의 항목만 선택할 수 있도록 설정

        //EditText
        ed = (EditText) findViewById(R.id.newitem);

        //listview 꾹 누르면 다이얼(대화상자) 뜨게 하기
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                show();
                return true;
            }
        });

        // ResultActivity로 넘어감
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(RResultActivity.this, FileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(prefs.contains("key")){
            items.clear();
            Set<String> set = new HashSet<String>();
            set = prefs.getStringSet("key",null);
            items.addAll(set);
            Collections.sort(items);
        }

    }

    /** 전체 내장 메모리 크기를 가져온다 */

    private long getTotalInternalMemorySize(){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();

        return totalBlocks * blockSize;
    }

    /** 사용가능한 내장 메모리 크기를 가져온다 */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private long getInternalMemorySize(){
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocksLong();

        return availableBlocks * blockSize;
    }

    /** 보기 좋게 MB, KB 단위로 축소시킨다 */
    private String formatSize(long size){
        String suffix = null;

        if(size >= 1024){
            suffix = "KB";
            size /= 1024;

            if(size >= 1024){
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while(commaOffset > 0){
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if(suffix != null){
            resultBuffer.append(suffix);
        }

        return resultBuffer.toString();
    }

    // items에 listView 생성
    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.btnAdd:                           // ADD 버튼 클릭시
                String text = ed.getText().toString();  // EditText에 입력된 문자열값을 얻기
                if(!text.isEmpty()){                    // 입력된 text 문자열이 비어있지 않으면
                    items.add(text);                    // items 리스트에 입력된 문자열 추가
                    ed.setText("");                     // EditText 입력란 초기화
                    adapter.notifyDataSetChanged();     // 리스트 목록 갱신
                }
        }
    }

    // 대화상자 처리
    void show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("AlertDialog Content");
        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"삭제를 선택했습니다.",Toast.LENGTH_LONG).show();

                        Delete();
                    }
                });
        builder.setNegativeButton("수정",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"수정을 선택했습니다.",Toast.LENGTH_LONG).show();

                        Modify();

                    }
                });
        builder.show();
    }

    //대화상자에서 삭제 선택 시
    void Delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "삭제", Toast.LENGTH_LONG).show();

                        int pos = listView.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
                        if (pos != ListView.INVALID_POSITION) {      // 선택된 항목이 있으면
                            items.remove(pos);                       // items 리스트에서 해당 위치의 요소 제거
                            listView.clearChoices();                 // 선택 해제
                            adapter.notifyDataSetChanged();
                            // 어답터와 연결된 원본데이터의 값이 변경된을 알려 리스트뷰 목록 갱신
                        }


                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "삭제하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    //대화창에서 수정 선택 시
    void Modify(){

        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog Title");
        builder.setMessage("수정할 내용 입력");
        builder.setView(edittext);
        builder.setPositiveButton("입력",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String text = edittext.getText().toString();        // EditText에 입력된 문자열값을 얻기
                        int pos = listView.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
                        if (pos != ListView.INVALID_POSITION) {
                            if (!text.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
                                items.set(pos,text);                           //arraylist에 저장된 listview(pos)의 내용을 text로 수정
                                adapter.notifyDataSetChanged();           // 리스트 목록 갱신
                            }
                            Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();

    }


    protected void onPause() {
        super.onPause();
        if (!items.isEmpty()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear().commit();
            Set<String> set = new HashSet<String>();
            set.addAll(items);
            editor.putStringSet("key", set).commit();
        }
    }
}
