package pe.lexcontrol.local;
import android.content.*;
public class BootReceiver extends BroadcastReceiver { @Override public void onReceive(Context c,Intent i){if(Intent.ACTION_BOOT_COMPLETED.equals(i.getAction()))AlertScheduler.schedule(c,c.getSharedPreferences("alerts",0).getString("data","{}"));}}
