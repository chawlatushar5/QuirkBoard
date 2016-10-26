package com.quirkboard.firstapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //CHOCOLATE CHIP MUFFIN

    TextView hello;
    ImageButton homebtn;
    ImageButton postbtn;
    ImageButton settings;
    double FRICTION=1;
    double BACKGROUNDSPEED=1;

    float xspeed=0, yspeed=0, startX=0, startY=0;
    boolean notTouching = true;

    ArrayList<ImageView> posts = new ArrayList<ImageView>();
    ArrayList<TextView> postTexts = new ArrayList<TextView>();

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //This runs first, automatically

        GPS a = new GPS("ASDF");
        a.Fun();

        /*
        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(Looper.getMainLooper());

        Runnable myRunnable = new Runnable()
        {
            @Override
            public void run() {System.out.println("I Am Running");} // This is your code
        };
        mainHandler.post(myRunnable);
        */

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if (notTouching)
                                movePosts(0,0);
                            }
                        });
                        Thread.sleep(2);
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
            }
        }.start();


        super.onCreate(savedInstanceState);
        //Title off
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //this would set the view to be the xml
        setContentView(R.layout.activity_main);

        //BUTTONS
        homebtn = (ImageButton) findViewById(R.id.homie);
        settings = (ImageButton) findViewById(R.id.settingbtn);
        hello = (TextView) findViewById(R.id.hello);
        postbtn = (ImageButton) findViewById(R.id.postbtn);


        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get Android ID
                String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                //store "android_id" to database

                hello.setText("Hello " + android_id + "!");
                System.out.println("hello world");
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Statspage.class));
            }
        });

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hello.setVisibility(View.GONE);
                startActivityForResult(new Intent(MainActivity.this, Pop.class), 0);
            }

        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case (0) :
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    Bundle extras = data.getExtras();
                    System.out.println("This is the data: " + data);

                    String userText = extras.getString("userText");

                    addPost(userText);

                    hello.setText(userText);
                }
                break;
            }
        }
    }

    public void addPost(String textToAdd)
    {
        ImageView stickyImage = new ImageView(this);
        TextView postText = new TextView(this);

        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.mainboard);

        myLayout.addView(stickyImage);
        myLayout.addView(postText);
        postText.setText(textToAdd);
        postText.setTextColor(Color.BLACK);

        BmpProcess processor = new BmpProcess();
        //TODO set image scale relative to screen size
        stickyImage.setImageBitmap(processor.autoScale(getResources(), R.drawable.sticky, 600, 600));

        //Get screen size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        //posttext.setPadding(200,400,0,0);
        postText.setX(width/2-textToAdd.length());
        postText.setY(height / 2 - 10);
        stickyImage.setX(width / 2 - 400);
        stickyImage.setY(height / 2 - 300);

        posts.add(stickyImage);
        postTexts.add(postText);

    }


    public boolean dispatchTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            notTouching=false;
            xspeed = 0;
            yspeed = 0;

            startX = event.getX();
            startY = event.getY();

            return super.dispatchTouchEvent(event);
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            xspeed = (float)((startX-event.getX()) * -BACKGROUNDSPEED);
            yspeed = (float)((startY-event.getY()) * -BACKGROUNDSPEED);
            startX = event.getX();
            startY = event.getY();

            movePosts(xspeed, yspeed);

            return super.dispatchTouchEvent(event);
        }

        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            notTouching = true;
            return super.dispatchTouchEvent(event);
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void movePosts(float xChange, float yChange)
    {
        if (!posts.isEmpty() && !postTexts.isEmpty())
        {
            if(notTouching)
                slowDown(FRICTION);

            for (ImageView pic : posts)
            {
                pic.setX(pic.getX()+xspeed);
                pic.setY(pic.getY()+yspeed);
            }

            for (TextView txt : postTexts)
            {
                txt.setX(txt.getX()+xspeed);
                txt.setY(txt.getY()+yspeed);
            }
        }
    }

    public void slowDown(double FRICTION)
    {

        if (Math.abs(xspeed)-FRICTION <0)
            xspeed=0;
        if (Math.abs(yspeed)-FRICTION <0)
            yspeed=0;
        if (Math.abs(xspeed) > 0 )
        {
            if (xspeed>0)
                xspeed-=FRICTION;
            else if (xspeed<0)
                xspeed+=FRICTION;
        }
        if (Math.abs(yspeed) > 0 )
        {
            if (yspeed>0)
                yspeed-=FRICTION;
            else if (yspeed<0)
                yspeed+=FRICTION;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
