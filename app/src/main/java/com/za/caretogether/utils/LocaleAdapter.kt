package com.za.caretogether.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.za.caretogether.R
import com.za.caretogether.response.Local
import com.za.caretogether.response.LocalX

class LocaleAdapter() : RecyclerView.Adapter<LocaleAdapter.ViewHolder>(){

    private var localeList : List<Local> = emptyList()
    private var context : Context? = null
    private var lastSelectedPosition = -1
    var status : String ? = null

    constructor(localeList: List<Local>, context: Context?, status : String) : this() {
        this.localeList = localeList
        this.context = context
        this.status = status
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocaleAdapter.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.layout_locales,parent,false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun getItemCount(): Int {

        return localeList.count()
    }

    override fun onBindViewHolder(holder: LocaleAdapter.ViewHolder, position: Int) {

        holder.locale_title.text = localeList[position].name
        var prefixName = String.format("ic_%s", localeList[position].locale)
        var resourceId = context!!.resources.getIdentifier(prefixName, "drawable", context!!.packageName)
        holder.img_flag.setImageResource(resourceId)
        var locale = Storage.make(context).locale
        if(locale == localeList[position].locale) {
            holder.radio.visibility = View.VISIBLE
            holder.radio.isChecked = true
        }
        else holder.radio.visibility = View.GONE
    }

    open inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

        var img_flag : ImageView
        var locale_title : TextView
        var radio : RadioButton
        var ll_locale : LinearLayout

        init {

            img_flag= view.findViewById<View>(R.id.img_flag) as ImageView
            locale_title = view.findViewById<View>(R.id.locale_title) as TextView
            radio = view.findViewById<View>(R.id.radio) as RadioButton
            ll_locale = view.findViewById<View>(R.id.ll_locale) as LinearLayout

            ll_locale.setOnClickListener {

                lastSelectedPosition = position
                Storage.make(context).locale = localeList[position].locale
                notifyDataSetChanged()
            }


        }

    }

}