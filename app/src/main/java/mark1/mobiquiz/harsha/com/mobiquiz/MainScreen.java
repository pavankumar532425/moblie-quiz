package mark1.mobiquiz.harsha.com.mobiquiz;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.IOException;

public class MainScreen extends AppCompatActivity
{
    public String diff[]={"none","easy","medium","hard","advanced"};
    public String str="";
    public Intent intent;
    public AlertDialog al;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        setSpinner(s1,"aptitude");
        Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        setSpinner(s2,"cpp");
        Spinner s3 = (Spinner) findViewById(R.id.spinner3);
        setSpinner(s3,"generalawareness");
        Spinner s4 = (Spinner) findViewById(R.id.spinner4);
        setSpinner(s4,"java");
        Spinner s5 = (Spinner) findViewById(R.id.spinner5);
        setSpinner(s5,"python");
        Spinner s6 = (Spinner) findViewById(R.id.spinner6);
        setSpinner(s6,"english");
        Spinner s7 = (Spinner) findViewById(R.id.spinner7);
        setSpinner(s7,"reasoning");
        Spinner s8 = (Spinner) findViewById(R.id.spinner8);
        setSpinner(s8,"history");
        setDP();
    }

    public void onDPclick(View v)
    {
        Bundle b = getIntent().getExtras();
        intent = new Intent(MainScreen.this,ProfilePage.class);
        intent.putExtra("lb",b.get("lb").toString());
        intent.putExtra("username",b.get("username").toString());
        intent.putExtra("userid",b.get("userid").toString());
        intent.putExtra("pts",b.get("pts").toString());
        intent.putExtra("ptscode",b.get("ptscode").toString());
        intent.putExtra("rank",b.get("rank").toString());
        startActivity(intent);
        finish();
    }

    public void loadQuiz(String cat,int x)
    {

        showAlert();

        Bundle b = getIntent().getExtras();
        intent = new Intent(MainScreen.this,QuizPage.class);
        intent.putExtra("lb",b.get("lb").toString());
        intent.putExtra("username",b.get("username").toString());
        intent.putExtra("userid",b.get("userid").toString());
        intent.putExtra("pts",b.get("pts").toString());
        intent.putExtra("ptscode",b.get("ptscode").toString());
        intent.putExtra("cat",cat);
        intent.putExtra("diff",diff[x]);
        intent.putExtra("rank",b.get("rank").toString());

        str="";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(cat).document(diff[x]);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        int qc = Integer.parseInt(document.get("qcount").toString());
                        for(int i=1;i<=qc;i++)
                        {
                            str = str+ (String)document.get("question"+i)+"@";
                        }
                        intent.putExtra("qcount",""+qc);
                    }
                    intent.putExtra("questions",str);
                    startActivity(intent);
                    MainScreen.this.finish();
                }
            }
        });
    }

    public void setSpinner(final Spinner s, final String cat)
    {
        s.setSelection(0);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                int x = s.getSelectedItemPosition();
                if (x == 0) return;
                s.setSelection(0);
                if(x!=0)
                {
                    loadQuiz(cat,x);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                return;
            }
        });
    }

    public void setDP()
    {
        ImageView dp = (ImageView) findViewById(R.id.dispic);
        if(true)
        {
            FileInputStream fin = null;
            try {
                fin = openFileInput("dispic.png");
                Bitmap bitmap = BitmapFactory.decodeStream(fin);
                dp.setImageBitmap(bitmap);
            } catch (Exception e) {
            } finally {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else dp.setImageResource(R.drawable.nodp);
    }

    public void showAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_progress_load, null));
        builder.setCancelable(false);
        al=builder.create();
        al.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        al.show();
    }

}