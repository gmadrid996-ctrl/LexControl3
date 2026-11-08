package pe.lexcontrol.local;
import android.app.*;import android.content.*;import android.os.*;import org.json.*;import java.text.*;import java.util.*;
public class AlertScheduler {
 static final String CHANNEL="audiencias";
 static void schedule(Context c,String json){try{
  JSONObject db=new JSONObject(json);JSONArray arr=db.optJSONArray("audiencias");if(arr==null)return;
  JSONArray cases=db.optJSONArray("expedientes"),clients=db.optJSONArray("clientes");AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
  for(int i=0;i<arr.length();i++){JSONObject a=arr.optJSONObject(i);if(a==null)continue;String id=a.optString("id",String.valueOf(i)),date=a.optString("fecha"),time=a.optString("hora","09:00");
   if(!time.matches("\\d{2}:\\d{2}"))time="09:00";
   Date hearing;try{SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.US);df.setLenient(false);hearing=df.parse(date+" "+time);}catch(Exception ex){continue;}
   String client="Cliente",caseNo="";if(cases!=null)for(int j=0;j<cases.length();j++){JSONObject ex=cases.optJSONObject(j);if(ex!=null&&ex.optString("id").equals(a.optString("expedienteId"))){caseNo=ex.optString("numero");if(clients!=null)for(int k=0;k<clients.length();k++){JSONObject cl=clients.optJSONObject(k);if(cl!=null&&cl.optString("id").equals(ex.optString("clienteId")))client=cl.optString("nombre","Cliente");}break;}}
   long[] advance={7L*86400000,3L*86400000,86400000,3600000};String[] labels={"7 días","3 días","1 día","1 hora"};
   for(int n=0;n<advance.length;n++){long when=hearing.getTime()-advance[n];int req=Math.abs((id+"-"+n).hashCode());Intent intent=new Intent(c,AlertReceiver.class);intent.putExtra("title","Audiencia en "+labels[n]);intent.putExtra("body",a.optString("titulo","Audiencia")+" · "+client+" · Exp. "+caseNo+" · "+date+" "+time);intent.putExtra("id",req);intent.putExtra("speech","Tienes una audiencia: "+a.optString("titulo","audiencia")+". Cliente "+client+". En "+labels[n]+".");PendingIntent pi=PendingIntent.getBroadcast(c,req,intent,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
    am.cancel(pi);if(when>System.currentTimeMillis())am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,when,pi);
   }
  }
 }catch(Exception ex){android.util.Log.e("LexControl","No se pudieron programar alertas",ex);}}
}