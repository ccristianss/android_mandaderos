package dev.crsi.manders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.R
import dev.crsi.manders.adapters.viewHolders.DetailMandersViewHolder
import dev.crsi.manders.models.DetailRequestResponse

class MandersAdapter(private val listManders: List<DetailRequestResponse>) :
    RecyclerView.Adapter<DetailMandersViewHolder>() {
        //DetailRequestResponse: Este es el modelo de datos que etsa en moldes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMandersViewHolder {
        return  DetailMandersViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.manders_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DetailMandersViewHolder, position: Int) {
        holder.render(listManders[position])
    }

    override fun getItemCount(): Int = listManders.size



}