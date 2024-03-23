package dev.crsi.manders.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ServicesItemBinding
import dev.crsi.manders.models.ServiceResponse

class ServicesAdapter(private val listRequest: List<ServiceResponse>) :
    RecyclerView.Adapter<ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        return ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.services_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.render(listRequest[position])
    }

    override fun getItemCount(): Int = listRequest.size
}

class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ServicesItemBinding.bind(view)

    fun render(data: ServiceResponse) {
        binding.nameService.text = data.name_service
        Picasso.get().load(data.image_service).into(binding.imageService)
    }

}

