package pe.lexcontrol.local;
import android.app.*;import android.content.*;import android.os.*;import android.speech.tts.TextToSpeech;import android.speech.tts.UtteranceProgressListener;import java.util.Locale;
public class VoiceAlertService extends Service {
 private TextToSpeech tts;
 @Override public int onStartCommand(Intent i,int flags,int startId){
  if(Build.VERSION.SDK_INT>=26){NotificationChannel ch=new NotificationChannel("voz","Avisos hablados",NotificationManager.IMPORTANCE_LOW);((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(ch);}
  Notification.Builder nb=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,"voz"):new Notification.Builder(this);
  startForeground(3,nb.setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle("LexControl").setContentText("Reproduciendo aviso de voz").build());
  String msg=i==null?"Tienes un aviso de LexControl":i.getStringExtra("message");if(msg==null||msg.isEmpty())msg="Tienes un aviso de LexControl";
  final String message=msg;
  tts=new TextToSpeech(this,status->{if(status!=TextToSpeech.SUCCESS){stopSelf(startId);return;}tts.setLanguage(new Locale("es","PE"));tts.setSpeechRate(0.93f);tts.setOnUtteranceProgressListener(new UtteranceProgressListener(){public void onStart(String id){}public void onDone(String id){stopSelf(startId);}public void onError(String id){stopSelf(startId);}});tts.speak(message,TextToSpeech.QUEUE_FLUSH,null,"lex-"+startId);});
  new Handler(Looper.getMainLooper()).postDelayed(()->stopSelf(startId),25000);return START_NOT_STICKY;
 }
 @Override public void onDestroy(){if(tts!=null){tts.stop();tts.shutdown();}super.onDestroy();}
 @Override public IBinder onBind(Intent i){return null;}
}
