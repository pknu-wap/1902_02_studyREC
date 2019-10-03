package cafe.adriel.androidaudiorecorder.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
                android.R.layout.simple_list_item_1, arFiles);
        FileList.setAdapter(Adapter);//리스트뷰에 어댑터 연결
        FileList.setOnItemClickListener(mItemClickListener);//리스너 연결
        refreshFiles();
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
}




