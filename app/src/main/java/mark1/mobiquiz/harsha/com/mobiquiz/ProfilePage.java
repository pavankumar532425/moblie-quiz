package mark1.mobiquiz.harsha.com.mobiquiz;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

public class ProfilePage extends AppCompatActivity
{
    public RecyclerView recyclerView;
    public Vector<String> rh=new Vector();
    public FirebaseFirestore db;
    public AlertDialog al;
    public String rank="",user="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_page);

        /*StringTokenizer st=new StringTokenizer(getIntent().getStringExtra("lb"));
        while (st.hasMoreTokens())
        {
            rh.add(st.nextToken("$"));
        }*/

        recyclerView=(RecyclerView) findViewById(R.id.lboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new Adapter(this,rh));

        onRefreshClick(new View(this));

        setDP();

        TextView tv = (TextView) findViewById(R.id.username);
        tv.setText(getIntent().getStringExtra("username"));

        tv = (TextView) findViewById(R.id.mypts);
        tv.setText("Points: "+getIntent().getStringExtra("pts"));

        rank=getIntent().getStringExtra("rank");
        user=getIntent().getStringExtra("userid");

        updateRank();

    }

    public void setDP()
    {
        ImageView dp = (ImageView) findViewById(R.id.dispic2);
        FileInputStream fin = null;
            try
            {
                fin = openFileInput("dispic.png");
                Bitmap bitmap = BitmapFactory.decodeStream(fin);
                dp.setImageBitmap(bitmap);
            } catch (Exception e) {}
            finally
            {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ImageButton ib = (ImageButton) findViewById(R.id.logio);
            ib.setColorFilter(Color.WHITE);
            ib.setImageResource(R.drawable.logoutbtn);
    }

    public void onBackPressed()
    {
        Bundle b = getIntent().getExtras();
        Intent intent;
        intent = new Intent(ProfilePage.this,MainScreen.class);
        intent.putExtra("lb",b.get("lb").toString());
        intent.putExtra("username",b.get("username").toString());
        intent.putExtra("userid",b.get("userid").toString());
        intent.putExtra("pts",b.get("pts").toString());
        intent.putExtra("ptscode",b.get("ptscode").toString());
        intent.putExtra("rank",rank);
        startActivity(intent);
        this.finish();
    }

    public void onSigninClick(View v)
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        GoogleSignIn.getClient(this, gso).signOut();
        startActivity(new Intent(ProfilePage.this,LoadingScreen.class));
        finish();
    }

    public void onRefreshClick(View v)
    {
        showLoader();
        db = FirebaseFirestore.getInstance();
        db.collection("users").orderBy("pts", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                if (task.isSuccessful())
                {
                    int i=1;
                    rh.removeAllElements();
                    for(DocumentSnapshot document:task.getResult())
                    {
                        rh.add(i+"~"+document.get("username").toString()+"~"+document.get("pts").toString());
                        i++;
                        if(user.compareTo(document.get("userid").toString())==0) rank=""+(i-1);
                    }
                }
                else
                {
                    Toast.makeText(ProfilePage.this,"refresh failed",Toast.LENGTH_SHORT).show();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                updateRank();
                al.dismiss();
            }
        });
    }

    public void showLoader()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_progress_load, null));
        builder.setCancelable(false);
        al=builder.create();
        al.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        al.show();
    }

    public void updateRank()
    {
        TextView rv = (TextView) findViewById(R.id.myrank);
        rv.setText("Rank: "+rank);
    }

}