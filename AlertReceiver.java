package pe.lexcontrol.local;
import android.app.*;import android.content.*;import android.os.*;
public class AlertReceiver extends android.content.BroadcastReceiver {
 @Override public void onReceive(Context c,Intent i){NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);if(Build.VERSION.SDK_INT>=26)nm.createNotificationChannel(new NotificationChannel(AlertScheduler.CHANNEL,"Alertas de audiencias",NotificationManager.IMPORTANCE_HIGH));
  Intent launch=new Intent(c,MainActivity.class);PendingIntent pi=PendingIntent.getActivity(c,0,launch,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
  Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(c,AlertScheduler.CHANNEL):new Notification.Builder(c);
  b.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(i.getStringExtra("title")).setContentText(i.getStringExtra("body")).setStyle(new Notification.BigTextStyle().bigText(i.getStringExtra("body"))).setAutoCancel(true).setContentIntent(pi);
  Intent voice=new Intent(c,VoiceAlertService.class);voice.putExtra("message",i.getStringExtra("speech"));try{if(Build.VERSION.SDK_INT>=26)c.startForegroundService(voice);else c.startService(voice);}catch(Exception ex){android.util.Log.w("LexControl","No se pudo iniciar voz",ex);}
  try{nm.notify(i.getIntExtra("id",1),b.build());}catch(SecurityException ignored){}
 }
}