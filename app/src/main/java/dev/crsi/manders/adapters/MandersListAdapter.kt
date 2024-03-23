package dev.crsi.manders.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.R
import dev.crsi.manders.adapters.viewHolders.DetailMandersViewHolder
import dev.crsi.manders.adapters.viewHolders.MandersListViewHolder
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.ManderResponse

class MandersListAdapter(private val listMander: List<ManderResponse>) :
    RecyclerView.Adapter<MandersListViewHolder>() {
        //DetailRequestResponse: Este es el modelo de datos que etsa en moldes

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MandersListViewHolder {
        return  MandersListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_manders_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MandersListViewHolder, position: Int) {
        holder.render(listMander[position])
    }

    override fun getItemCount(): Int = listMander.size



}