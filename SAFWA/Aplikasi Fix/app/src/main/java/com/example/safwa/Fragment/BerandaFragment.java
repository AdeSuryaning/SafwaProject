package com.example.safwa.Fragment;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.safwa.Adapter.TabelHistoryAdapter;
import com.example.safwa.Adapter.TableAdapter;
import com.example.safwa.Model.ModelDataHistory;
import com.example.safwa.Model.ModelDataRealTime;
import com.example.safwa.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BerandaFragment extends Fragment {

    DatabaseReference realtimeReference;
//    Query databaseReference;
    private DatabaseReference mDatabase;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private SwipeRefreshLayout swipeBeranda;
    RecyclerView rv_table;
    TextView tv_debit;
    TextView tv_tagihan;
    String startDate;
    View txt_pilihTanggal;
    EditText et_date_picker;

    Calendar mCalendar = Calendar.getInstance();
    Handler handler = new Handler();
//    String childDate = DateDataFormat.format(Calendar.getInstance().getTime());

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    final public static SimpleDateFormat DateDataFormat = new SimpleDateFormat("yyyy-MM-dd", new Locale("tanggal", "TANGGAL"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);
        tv_debit = view.findViewById(R.id.tv_debit);
        rv_table = view.findViewById(R.id.rv_data_table);
        tv_tagihan = view.findViewById(R.id.tv_tagihan);
        et_date_picker =view.findViewById(R.id.et_date_picker);

        txt_pilihTanggal = view.findViewById(R.id.txt_pilihTanggal);

        //inisiasi
        realtimeReference = FirebaseDatabase.getInstance().getReference().child("RealTime");
        realtimeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i("pemakaian", snapshot.toString());
                tv_debit.setText(snapshot.child("debit").getValue(String.class).length() <= 6 ?
                        snapshot.child("debit").getValue(String.class) : "0");
                tv_tagihan.setText(snapshot.child("tagihan").getValue(String.class).length() <= 6 ?
                        snapshot.child("tagihan").getValue(String.class) : "0");
                Log.i("kebocorann",snapshot.child("kebocoran").getValue(Integer.class)+"");
                if(snapshot.child("kebocoran").getValue(Integer.class) == 1){
                    showNotif();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("hgdhgsdsh", error.toString());
            }
        });

       updateLabel();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }

//        txt_pilihTanggal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                databaseReference.child("History").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date);
//                        String childDate = DateDataFormat.format(Calendar.getInstance().getTime());
//                        Map<String, Object> values = new HashMap<>();
////                        values.put("timestamp", currentDateTimeString);
//                        databaseReference.child("History").child(childDate).push().setValue(values);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//            new DatePickerDialog(BerandaFragment.this, new DatePickerDialog.OnDateSetListener(){
//                @Override
//                public void onDataSet(DatePicker view, int year, int month, int dayOfMonth){
//                    mCalendar.set(Calendar.YEAR, year);
//                    mCalendar.set(Calendar.MONTH, month);
//                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                    txt_pilihTanggal.setText(DateDataFormat.format(mCalendar.getTime()));
//                }
//        });
//
//        mCalendar = Calendar.getInstance();
//        String childDate = DateDataFormat.format(Calendar.getInstance().getTime());



        return view;
    }

    private void updateLabel() {
        String myFormat = "dd-MMM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        et_date_picker.setText(sdf.format(mCalendar.getTime()));

        String myFormat1 = "yyyyMMdd"; //In which you need put here
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.getDefault());
        startDate =sdf1.format(mCalendar.getTime());
        Log.i("dateStarttt",startDate);

        getData();
    }

    private void getData(){
        TabelHistoryAdapter tabelHistoryAdapter = new TabelHistoryAdapter(getContext());
        List<ModelDataHistory> historyData = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("History").child(startDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    //ModelDataHistory modelDataHistory = snapshot1.getValue(ModelDataHistory.class);
                    if(snapshot1.child("tagihan").getValue(String.class).length() <=6){
                        ModelDataHistory modelDataHistory = new ModelDataHistory();
                        modelDataHistory.setDebit(snapshot1.child("debit").getValue(String.class));
                        modelDataHistory.setKebocoran(snapshot1.child("kebocoran").getValue(String.class));
                        modelDataHistory.setTagihan(snapshot1.child("tagihan").getValue(String.class));
                        modelDataHistory.setTimestamp(snapshot1.child("timestamp").getValue(String.class));
                        historyData.add(modelDataHistory);
                    }

                }

                rv_table.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_table.setHasFixedSize(true);
                tabelHistoryAdapter.setData(historyData);
                rv_table.setAdapter(tabelHistoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showNotif(){
        String channelId = getString(R.string.channelId);
        String channelName = getString(R.string.channelName);
        Notification notification;

        Intent resultIntent = new Intent(requireContext(), BerandaFragment.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(requireContext().getApplicationContext(), defaultSoundUri);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(requireContext(), channelId)
                        .setSmallIcon(R.mipmap.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.mipmap.logo))
                        .setContentTitle("PERINGATAN!")
                        .setContentText("HARAP MELAKUKAN PENGECEKAN PADA PIPA!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Terdeteksi adanya kebocoran " +
                                "\nHarap Melakukan Pengecekan Pada Pipa Anda!"))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentIntent(pendingIntent);
        r.play();

        NotificationManager mNotificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build());
            channel.enableVibration(true);

            notificationBuilder.setChannelId(channelId);

            if (mNotificationManager != null) {
                notification = notificationBuilder.build();
                mNotificationManager.createNotificationChannel(channel);
                mNotificationManager.notify(1, notification);
            }

        } else {
            if (mNotificationManager != null) {
                notification = notificationBuilder.build();
                mNotificationManager.notify(1, notification);
            }
        }
    }
}