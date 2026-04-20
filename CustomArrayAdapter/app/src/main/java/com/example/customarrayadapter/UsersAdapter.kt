package com.example.customarrayadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UsersAdapter(context: Context, users: ArrayList<User>) :
    ArrayAdapter<User>(context, 0, users) {

    private class ViewHolder(
        val tvName: TextView,
        val tvHome: TextView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
            holder = ViewHolder(
                view.findViewById(R.id.tvName),
                view.findViewById(R.id.tvHome)
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val user = getItem(position)

        holder.tvName.text = user?.name
        holder.tvHome.text = user?.hometown

        return view
    }
}