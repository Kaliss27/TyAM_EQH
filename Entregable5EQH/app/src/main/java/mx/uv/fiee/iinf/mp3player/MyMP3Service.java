package mx.uv.fiee.iinf.mp3player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyMP3Service extends Service
{
    public static final String CHANNEL_ID = "MyServiceChannel";
    public static final String CHANNEL_DESC = "Foreground Service Notifications";
    Uri mediaUri;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        Intent notificationIntent = new Intent (this, DetailsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity (this, 0, notificationIntent, 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel (CHANNEL_ID, CHANNEL_DESC, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights (false);
            channel.setShowBadge (true);
            NotificationManager manager = (NotificationManager) getSystemService (NOTIFICATION_SERVICE);
            manager.createNotificationChannel (channel);
        }

        // creamos una imagen de mapa de bits a partir del archivo de recursos, para ser usado como ícono
        Bitmap icon = BitmapFactory.decodeResource (getResources (), R.mipmap.baseline_library_music_white_48dp);

        // en versiones previas a android 8
        //Notification notification = new Notification.Builder (getBaseContext (), "NOTICHANNELTYAM")
        Notification notification = new NotificationCompat.Builder (this, CHANNEL_ID)
                .setContentTitle ("MP3Player")
                .setContentText (" ")
                .setSmallIcon (R.mipmap.baseline_queue_music_white_24dp)
                .setLargeIcon (icon)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build ();

        startForeground (1, notification); // se establece la bandera para servicio en primer plano y la notificación

        // es necesario ejecutar la tarea de modo asíncrono de modo que su ejecución no bloquee
        // la finalización del método
        new Thread ( () -> {
            for (int i = 0; i < 10; i++)
            {
                try
                {
                    Thread.sleep (1000);
                    Log.i ("TYAM", "Foreground service running");
                }
                catch (InterruptedException ex)
                {
                    ex.printStackTrace ();
                }
            }
            stopSelf (); // al finalizar la tarea se detiene el servicio
        } ).start ();

        return START_STICKY;
    }
}
