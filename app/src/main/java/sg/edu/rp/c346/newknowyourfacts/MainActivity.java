package sg.edu.rp.c346.newknowyourfacts;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ArrayList<Fragment> al;
    MyFragmentPagerAdapter adapter;
    ViewPager vPager;
    AlarmManager am;
    int notificationID = 888;
    int requestCode = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vPager = (ViewPager) findViewById(R.id.viewPager);

        FragmentManager fm = getSupportFragmentManager();

        al = new ArrayList<Fragment>();
        al.add(new Frag1());
        al.add(new Frag2());
        al.add(new Frag3());

        adapter = new MyFragmentPagerAdapter(fm, al);

        vPager.setAdapter(adapter);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, 5);

                //Create a new PendingIntent and add it to the AlarmManager
                Intent intent = new Intent(MainActivity.this,
                        MainActivity.class);
                int reqCode = 12345;
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(MainActivity.this,
                                reqCode, intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);

                // Get AlarmManager instance
                am = (AlarmManager)
                        getSystemService(Activity.ALARM_SERVICE);

                // Set the alarm
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);


            }
        });

    }
    public void sendNotofication(){
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new
                    NotificationChannel("default", "Default Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(MainActivity.this,SendNotification.class);
        PendingIntent pIntent = PendingIntent.getActivity
                ( MainActivity.this, requestCode, intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

        // Build notification
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(MainActivity.this, "default");
        builder.setContentTitle("P10 test!");
        builder.setContentText("view the sentence");
        builder.setSmallIcon(android.R.drawable.btn_star_big_off);
        builder.setContentIntent(pIntent);
        builder.setAutoCancel(true);

        Notification n = builder.build();

        // An integer good to have, for you to programmatically cancel it
        notificationManager.notify(notificationID, n);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.previous:
                if (vPager.getCurrentItem() > 0){
                    int previousPage = vPager.getCurrentItem() - 1;
                    vPager.setCurrentItem(previousPage, true);
                }
                break;
            case R.id.next:
                int max = vPager.getChildCount();
                if (vPager.getCurrentItem() < max-1){
                    int nextPage = vPager.getCurrentItem() + 1;
                    vPager.setCurrentItem(nextPage, true);
                }
                break;
            case R.id.random:
                Random random = new Random();
                int a = random.nextInt(al.size());
                vPager.setCurrentItem(a, true);
                break;
            default:
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
