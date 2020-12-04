package com.example.myfovoriteyoutubechannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.myfovoriteyoutubechannel.handlers.YoutubeChannelHandler
import com.example.myfovoriteyoutubechannel.models.YoutubeChannel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    lateinit var rankEditText: EditText
    lateinit var nameEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var addEditButton: Button
    lateinit var youtubechannelHandler: YoutubeChannelHandler
    lateinit var youtubechannels: ArrayList<YoutubeChannel>
    lateinit var displayListView: ListView
    lateinit var youtubechannelGettingEdited: YoutubeChannel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rankEditText = findViewById(R.id.rankEditText)
        nameEditText = findViewById(R.id.nameEditText)
        linkEditText = findViewById(R.id.linkEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        addEditButton = findViewById(R.id.addEditButton)
        youtubechannelHandler = YoutubeChannelHandler()

        youtubechannels = ArrayList()
        displayListView = findViewById(R.id.displayListView)

        addEditButton.setOnClickListener{
            val rank = rankEditText.text.toString()
            val name = nameEditText.text.toString()
            val link = linkEditText.text.toString()
            val reason = reasonEditText.text.toString()

            if(addEditButton.text.toString() == "Add"){
                val youtubechannel = YoutubeChannel(rank = rank, name = name, link = link, reason= reason)
                if(youtubechannelHandler.create(youtubechannel)){
                    Toast.makeText(applicationContext, "Youtube Channel added", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
            }
            else if(addEditButton.text.toString() == "Update"){
                val youtubechannel = YoutubeChannel(id = youtubechannelGettingEdited.id, rank = rank, name = name, link = link, reason = reason)
                if(youtubechannelHandler.update(youtubechannel)){
                    Toast.makeText(applicationContext, "Youtube Channel updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                }

            }

        }

        registerForContextMenu(displayListView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.youtubechannel_options, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when(item.itemId){
            R.id.edit_youtubechannel ->{
                youtubechannelGettingEdited = youtubechannels[info.position]
                rankEditText.setText(youtubechannelGettingEdited.rank)
                nameEditText.setText(youtubechannelGettingEdited.name)
                linkEditText.setText(youtubechannelGettingEdited.link)
                reasonEditText.setText(youtubechannelGettingEdited.reason)
                addEditButton.setText("Update")
                true
            }
            R.id.delete_youtubechannel -> {

                if(youtubechannelHandler.delete(youtubechannels[info.position])){
                    Toast.makeText(applicationContext, "Youtube Channel deleted", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()

        youtubechannelHandler.youtubechannelRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                youtubechannels.clear()

                snapshot.children.forEach {
                    it -> val youtubechannel = it.getValue(YoutubeChannel::class.java)
                    youtubechannels.add(youtubechannel!!)
                    youtubechannels.sortWith(object: Comparator<YoutubeChannel>{
                        override fun compare(o1: YoutubeChannel, o2: YoutubeChannel): Int = when {
                            o1.rank!! > o2.rank.toString() -> 1
                            o1.rank!! == o2.rank.toString() -> 0
                            else -> -1
                        }
                    })

                }
                val adapter = ArrayAdapter<YoutubeChannel>(applicationContext, android.R.layout.simple_list_item_1, youtubechannels)
                displayListView.adapter = adapter
            }

        })
    }
    fun clearFields() {
        rankEditText.text.clear()
        nameEditText.text.clear()
        linkEditText.text.clear()
        reasonEditText.text.clear()
        addEditButton.setText("Add")
    }
}