package com.example.ugdnyakawan

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.inflate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.ugdnyakawan.databinding.ActivityEditBinding.inflate
import com.example.ugdnyakawan.databinding.ActivityHome1Binding.inflate
import com.example.ugdnyakawan.databinding.ActivityMainBinding.inflate
import com.example.ugdnyakawan.databinding.RvItemMinumanBinding.inflate
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivityNote : AppCompatActivity() {
    val db by lazy { NoteDB(this) }
    private var noteId: Int = 0

    private var binding: EditActivityNote? = null
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setupView()
        setupListener()

//        binding = EditActivityNote.inflate(layoutInflater)

//        setContentView(binding!!.root)

        createNotificationChannelN()

        binding!!.button_save.setOnClickListener {
            sendNotificationNote()
        }

 }


    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getNote()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }


    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    Note(0,edit_title.text.toString(),
                        edit_note.text.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNote(
                    Note(noteId, edit_title.text.toString(),
                        edit_note.text.toString())
                )
                finish()
            }
        }
    }

        fun getNote() {
            noteId = intent.getIntExtra("intent_id", 0)
            CoroutineScope(Dispatchers.IO).launch {
                val notes = db.noteDao().getNote(noteId)[0]
                edit_title.setText(notes.nama)
                edit_note.setText(notes.deskripsi)
            }
        }

        override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return super.onSupportNavigateUp()
        }

    private fun createNotificationChannelN() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotificationNote() {
        val intent : Intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Note makanan sudah tersimpan")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val bigPictureBitmap = ContextCompat.getDrawable(this, R.drawable.notif)?.toBitmap()
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bigPictureBitmap)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setStyle(bigPictureStyle)
            .setContentTitle("Order Terkirim")
            .setContentText("Silahkan Tunggu pesanan")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.YELLOW)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId1, builder.build())
        }
    }


    }