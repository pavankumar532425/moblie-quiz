package mark1.mobiquiz.harsha.com.mobiquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class QuizPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    public String cat,diff;
    public static Spinner sp;
    public static int q=0;
    public int qc=0;
    public int pts=0;
    int ta=0,tun=0,tc=0,tic=0,tq=0,pg=0,tt=0;
    public String str,ptscode="";
    public Question qs[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page);

        loadLabel();
        loadQuestions();
        setTimer();

    }

    public void setTimer()
    {
        Timer t = new Timer();
        TimerTask task =  new TimerTask() {
            int time=0;
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        TextView tim = (TextView) findViewById(R.id.timerview);
                        tim.setText("time: "+(time++));
                        tt=time;
                    }
                });
            }
        };

        t.scheduleAtFixedRate(task,0,1000);
    }

    public void loadQuestions()
    {
        qc=Integer.parseInt(getIntent().getStringExtra("qcount"));
        sp=(Spinner) findViewById(R.id.spinnerx);
        List<String> l = new ArrayList();
        for(int j=1;j<=qc;j++)
        {
            l.add("Question "+j);
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,l);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
        sp.setOnItemSelectedListener(this);

        qs = new Question[qc];
        str = getIntent().getStringExtra("questions");
        StringTokenizer st = new StringTokenizer(str);
        int i=0;
        while(st.hasMoreTokens())
        {
            qs[i]=new Question(st.nextToken("@"));
            i++;
        }

    }

    public void loadLabel()
    {
        Bundle b=getIntent().getExtras();
        cat= b.getString("cat");
        diff=b.getString("diff");
        ImageView tv=(ImageView)findViewById(R.id.quizlabelview);

        if(cat.compareTo("aptitude")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.aptitudeeasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.aptitudemedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.aptitudehard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.aptitudeadvanced);
        }

        if(cat.compareTo("cpp")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.cppeasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.cppmedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.cpphard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.cppadvanced);
        }

        if(cat.compareTo("java")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.javaeasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.javamedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.javahard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.javaadvanced);
        }

        if(cat.compareTo("generalawareness")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.generaleasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.generalmedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.generalhard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.generaladvanced);
        }

        if(cat.compareTo("history")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.historyeasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.historymedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.historyhard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.historyadvanced);
        }

        if(cat.compareTo("reasoning")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.reasoningeasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.reasoningmedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.reasoninghard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.reasoningadvanced);
        }

        if(cat.compareTo("python")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.pythoneasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.pythonmedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.pythonhard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.pythonadvanced);
        }

        if(cat.compareTo("english")==0)
        {
            if(diff.compareTo("easy")==0) tv.setImageResource(R.drawable.englisheasy);
            if(diff.compareTo("medium")==0) tv.setImageResource(R.drawable.englishmedium);
            if(diff.compareTo("hard")==0) tv.setImageResource(R.drawable.englishhard);
            if(diff.compareTo("advanced")==0) tv.setImageResource(R.drawable.englishadvanced);
        }

    }

    public void getQuestion()
    {
        final TextView ques=(TextView) findViewById(R.id.questiontv);
        final RadioButton op1 = (RadioButton) findViewById(R.id.option1);
        final RadioButton op2 = (RadioButton) findViewById(R.id.option2);
        final RadioButton op3 = (RadioButton) findViewById(R.id.option3);
        final RadioButton op4 = (RadioButton) findViewById(R.id.option4);
        q=sp.getSelectedItemPosition();
        ques.setText(qs[q].ques);
        op1.setText(qs[q].op1);
        op2.setText(qs[q].op2);
        op3.setText(qs[q].op3);
        op4.setText(qs[q].op4);

        final RadioGroup rg = (RadioGroup) findViewById(R.id.optionsgroup);

        if(qs[q].sans.compareTo("0")==0) rg.clearCheck();
        else if(qs[q].sans.compareTo("1")==0) rg.check(R.id.option1);
        else if(qs[q].sans.compareTo("2")==0) rg.check(R.id.option2);
        else if(qs[q].sans.compareTo("3")==0) rg.check(R.id.option3);
        else if(qs[q].sans.compareTo("4")==0) rg.check(R.id.option4);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (rg.getCheckedRadioButtonId())
                {
                    case R.id.option1:
                        qs[q].sans = "1";
                        break;
                    case R.id.option2:
                        qs[q].sans = "2";
                        break;
                    case R.id.option3:
                        qs[q].sans = "3";
                        break;
                    case R.id.option4:
                        qs[q].sans = "4";
                        break;
                    default:
                        qs[q].sans="0";
                        break;
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        getQuestion();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        return;
    }

    public void onfbexi(View v)
    {
        showAlert();
    }

    public void onBackPressed()
    {
        showAlert();
    }

    public void gotoNext(View v)
    {
        if(q<qc-1)
        {
            sp.setSelection(q + 1);
            q++;
        }
    }

    public void gotoPrev(View v)
    {
        if(q>0)
        {
            sp.setSelection(q - 1);
            q--;
        }
    }

    public void onQuesNav(View v)
    {
        sp.performClick();
    }

    public void showAlert()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to End Quiz?");
        builder1.setCancelable(true);

        builder1.setPositiveButton("End Quiz", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                showResult();
            }
        });

        builder1.setNegativeButton("Continue" , new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                return;
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void showResult()
    {
        calculateResult();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_result_page, null));
        builder.setCancelable(true);

        builder.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                updateResults();
            }
        });

        AlertDialog al=builder.create();
        al.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        al.getWindow().setGravity(Gravity.CENTER);
        al.show();

        TextView te[]= new TextView[7];

        te[0] = (TextView) al.getWindow().findViewById(R.id.tan);
        te[1] = (TextView) al.getWindow().findViewById(R.id.tun);
        te[2] = (TextView) al.getWindow().findViewById(R.id.tic);
        te[3] = (TextView) al.getWindow().findViewById(R.id.tc);
        te[4] = (TextView) al.getWindow().findViewById(R.id.tt);
        te[5] = (TextView) al.getWindow().findViewById(R.id.pg);
        te[6] = (TextView) al.getWindow().findViewById(R.id.tq);

        te[0].setText(""+ta);
        te[1].setText(""+tun);
        te[2].setText(""+tic);
        te[3].setText(""+tc);
        te[4].setText(""+tt);
        te[5].setText(""+pg);
        te[6].setText(""+tq);

    }

    public void calculateResult()
    {
        for(int j=0;j<qc;j++)
        {
            if(qs[j].sans.compareTo("0")==0) tun++;
            else ta++;
            if(qs[j].sans.compareTo(qs[j].ans)==0) tc++;
            else tic++;
        }
        pg=tc*10;
        tq=qc;

    }

    public void updateResults()
    {
        showLoad();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("users").document(getIntent().getStringExtra("userid"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task)
            {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists())
                    {
                        ptscode=document.get("ptscode").toString();
                        int ci=ptscode.indexOf(cat);
                        int di=ptscode.indexOf(diff,ci);
                        int pp=Integer.parseInt(ptscode.substring(di+diff.length()+1,ptscode.indexOf("~",di)));
                        pts=Integer.parseInt(document.get("pts").toString());
                        if(pp<pg)
                        {
                            ptscode=ptscode.substring(0,di+diff.length()+1)+pg+ptscode.substring(ptscode.indexOf("~",di));
                            pts=pts+pg-pp;
                            Map<String, Object> usr = new HashMap<>();
                            usr.put("pts",pts);
                            usr.put("ptscode",ptscode);
                            db.collection("users").document(getIntent().getStringExtra("userid")).update(usr).addOnSuccessListener(new OnSuccessListener<Void>()
                            {
                                @Override
                                public void onSuccess(Void aVoid)
                                {
                                    gotoMainScreen();
                                }

                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    updateResults();
                                }

                            });
                        }
                        else gotoMainScreen();
                    }
                }
                else
                {
                    updateResults();
                    return;
                }
            }
        });

    }

    public void showLoad()
    {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_progress_load, null));
        builder.setCancelable(false);
        android.support.v7.app.AlertDialog al=builder.create();
        al.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        al.show();
    }

    public void gotoMainScreen()
    {
        Bundle b = getIntent().getExtras();
        Intent intent;
        intent = new Intent(QuizPage.this,MainScreen.class);
        intent.putExtra("lb",b.get("lb").toString());
        intent.putExtra("username",b.get("username").toString());
        intent.putExtra("userid",b.get("userid").toString());
        intent.putExtra("pts",pts);
        intent.putExtra("ptscode",ptscode);
        intent.putExtra("rank",b.get("rank").toString());
        startActivity(intent);
        finish();
    }

    public class Question
    {
        String ques,op1,op2,op3,op4,ans,sans;

        Question(String s)
        {
            StringTokenizer st = new StringTokenizer(s);
            ques = st.nextToken("~");
            op1 = st.nextToken("~");
            op2 = st.nextToken("~");
            op3 = st.nextToken("~");
            op4 = st.nextToken("~");
            ans = st.nextToken("~");
            sans="0";
        }
    }
}