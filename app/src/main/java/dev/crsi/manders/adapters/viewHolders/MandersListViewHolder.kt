package dev.crsi.manders.adapters.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.databinding.ListMandersItemBinding
import dev.crsi.manders.databinding.MandersItemBinding
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.ManderResponse

class MandersListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //MandersItemBinding: Este sale de layouts para el diseño  de cada item
    private val binding = ListMandersItemBinding.bind(view)

    fun render(data: ManderResponse) {
        binding.addressmander.text = data.address_mander
       //binding.ccmander.text= data.cc_mander
        //binding.useriduser.text = data.address_mander
    }

}