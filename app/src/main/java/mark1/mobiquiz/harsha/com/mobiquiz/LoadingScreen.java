package mark1.mobiquiz.harsha.com.mobiquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoadingScreen extends AppCompatActivity
{

    public GoogleSignInAccount account;
    public static int RC_SIGN_IN;
    public static GoogleSignInClient mGoogleSignInClient;
    public String str="";
    public String pts="";
    public String ptscode="";
    public ProgressBar pb;
    public TextView tv;
    public int rank=1;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pb = (ProgressBar) findViewById(R.id.progressbar);
        tv = (TextView) findViewById(R.id.loadertext);
        initializer("x");
    }

    public void initializer(String msg)
    {
        if(msg.compareTo("x")!=0)
        {
            Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        }

        setPro(0,"connecting");

        if(isConnected())
        {
            signIn();
        }

        else
        {
            showAlert();
        }

    }

    public boolean isConnected()
    {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) return true;
        return false;
    }

    public void showAlert()
    {
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(LoadingScreen.this);
        builder1.setMessage("No internet Connection");
        builder1.setCancelable(false);
        builder1.setPositiveButton("try again", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                initializer("x");
            }
        });
        builder1.setNegativeButton("exit" , new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void signIn()
    {
        setPro(20,"signing in");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoadingScreen.this, gso);
        mGoogleSignInClient.signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                account = task.getResult(ApiException.class);
                if(account!=null)
                {
                    setPro(60,"connecting to database");
                    checkFbUser();
                }
                else
                {
                    initializer("login failed");
                }

            } catch (Exception e)
            {
                initializer("login failed");
            }
        }
        else
        {
            initializer("login failed");
        }
    }

    public void checkFbUser()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(account.getEmail());
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
                        setPro(80,"loading DP");
                        loadDP();
                        pts=document.get("pts").toString();
                        ptscode=document.get("ptscode").toString();
                    }
                    else
                    {
                        setPro(70,"creating database account");
                        createFbUser();
                    }
                }
                else
                {
                    initializer("Firebase connection failed");
                }
            }
        });
    }

    public void createFbUser()
    {
        pts="0";
        ptscode="0";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usr = new HashMap<>();
        usr.put("userid",account.getEmail());
        usr.put("username",account.getDisplayName());
        usr.put("pts",0);
        usr.put("ptscode","aptitude~easy:0~medium:0~hard:0~advanced:0~cpp~easy:0~medium:0~hard:0~advanced:0~generalawareness~easy:0~medium:0~hard:0~advanced:0~java~easy:0~medium:0~hard:0~advanced:0~python~easy:0~medium:0~hard:0~advanced:0~english~easy:0~medium:0~hard:0~advanced:0~reasoning~easy:0~medium:0~hard:0~advanced:0~history~easy:0~medium:0~hard:0~advanced:0~");
        db.collection("users").document(account.getEmail()).set(usr).addOnSuccessListener(new OnSuccessListener<Void>()
        {
            @Override
            public void onSuccess(Void aVoid)
            {
                setPro(80,"loading DP");
                loadDP();
            }

        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                initializer("Failed to create Firebase account");
            }

        });

    }

    private class Loaderx extends AsyncTask
    {
        @Override
        protected Object doInBackground(Object[] objects)
        {
            try
            {
                FileOutputStream fos = openFileOutput("dispic.png", Context.MODE_PRIVATE);
                Bitmap bitmap = null;
                InputStream inputStream = new URL(account.getPhotoUrl().toString()).openStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }
            catch (Exception ex)
            {
                initializer("Failed to Load DP`");
            }

            setPro(90,"loading account");
            loadLBoard();
            return null;
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
        }
    }

    public void loadDP()
    {
        new Loaderx().execute(new Object());
    }

    public void loadLBoard()
    {
       /* final String username=account.getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").orderBy("pts", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {

                if (task.isSuccessful())
                {
                    int i=0;
                    for (DocumentSnapshot document : task.getResult())
                    {
                        i++;
                        str = str+i+"~";
                        str = str+document.get("username").toString()+"~";
                        str= str+document.get("pts").toString()+"~";
                        str = str+document.get("userid").toString()+"$";
                        if(document.get("userid").toString().compareTo(username)==0) rank=i;
                    }
                    setPro(100,"loading MainScreen");
                    gotoMainScreen(str);
                }
                else
                {
                    Toast.makeText(LoadingScreen.this,"failed to load leaderboard",Toast.LENGTH_LONG).show();
                    loadLBoard();
                }
            }
        });*/
        setPro(100,"loading MainScreen");
        gotoMainScreen("leaderboard");
    }

    public void gotoMainScreen(String lb)
    {
        Intent intent = new Intent(LoadingScreen.this,MainScreen.class);
        intent.putExtra("lb",lb.substring(0,lb.length()-1));
        intent.putExtra("username",account.getDisplayName());
        intent.putExtra("userid",account.getEmail());
        intent.putExtra("pts",pts);
        intent.putExtra("ptscode",ptscode);
        intent.putExtra("rank",""+rank);
        startActivity(intent);
        finish();
    }

    public void setPro(final int p, final String text)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                pb.setProgress(p);
                tv.setText(text);
            }
        });
    }

}
