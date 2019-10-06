package cafe.adriel.androidaudiorecorder.example;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class FileActivity extends Activity {
    String Current;
    String Root;
    TextView CurrentTxt;
    ListView FileList;
    ArrayAdapter<String> Adapter;
    ArrayList<String> arFiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file);

        CurrentTxt = (TextView) findViewById(R.id.current);
        FileList = (ListView) findViewById(R.id.filelist);
        arFiles = new ArrayList<String>();
        //폴더 경로 가져옴
        Root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/StudyRec";
        Current = Root;//어댑터를 생성하고 연결해줌
        Adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, arFiles);
        FileList.setAdapter(Adapter);//리스트뷰에 어댑터 연결
        FileList.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나의 항목만 선택할 수 있도록 설정

        FileList.setOnItemClickListener(mItemClickListener);//리스너 연결
        refreshFiles();

        //listview 꾹 누르면 다이얼(대화상자) 뜨게 하기
        FileList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                show();
                return true;
            }
        });

    }



    //리스트뷰 클릭 리스너
    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String Name = arFiles.get(position);//클릭된 위치의 값을 가져옴
                    //디렉토리이면
                    if (Name.startsWith("[") && Name.endsWith("]")) {
                        Name = Name.substring(1, Name.length() - 1);//[]부분을 제거해줌
                    }
                    //들어가기 위해 /와 터치한 파일 명을 붙여줌
                    String Path = Current + "/" + Name;
                    File f = new File(Path);//File 클래스 생성
                    if(f.isDirectory()) {//디렉토리면?
                        Current = Path;//현재를 Path로 바꿔줌
                        refreshFiles();//리프레쉬
                    } else {//디렉토리가 아니면 토스트 메세지를 뿌림
                    }

                    Intent intent = new Intent(FileActivity.this, ResultActivity.class);
                    startActivity(intent);
                }




            };


    void refreshFiles() {
        CurrentTxt.setText(Current);//현재 PATH를 가져옴
        arFiles.clear();//배열리스트를 지움
        File current = new File(Current);//현재 경로로 File클래스를 만듬
        String[] files = current.list();//현재 경로의 파일과 폴더 이름을 문자열 배열로 리턴
        //파일이 있다면?
        if (files != null) {
            //여기서 출력을 해줌
            for (int i = 0; i < files.length; i++) {
                String Path = Current + "/" + files[i];
                String Name = "";
                File f = new File(Path);
                if (f.isDirectory()) {
                    Name = "[" + files[i] + "]";//디렉토리면 []를 붙여주고
                } else {
                    Name = files[i];//파일이면 그냥 출력
                }
                arFiles.add(Name);//배열리스트에 추가해줌
            }
        }
        // 리스트뷰를 갱신시킴
        Adapter.notifyDataSetChanged();

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

                        int pos = FileList.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
                        if (pos != ListView.INVALID_POSITION) {      // 선택된 항목이 있으면
                            arFiles.remove(pos);                       // items 리스트에서 해당 위치의 요소 제거
                            FileList.clearChoices();                 // 선택 해제
                            Adapter.notifyDataSetChanged();
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
                        int pos = FileList.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
                        if (pos != ListView.INVALID_POSITION) {
                            if (!text.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
                                arFiles.set(pos,text);                           //arraylist에 저장된 listview(pos)의 내용을 text로 수정
                                Adapter.notifyDataSetChanged();           // 리스트 목록 갱신
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



}

