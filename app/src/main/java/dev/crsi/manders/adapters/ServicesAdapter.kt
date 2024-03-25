package dev.crsi.manders.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ServicesItemBinding
import dev.crsi.manders.models.ServiceResponse
import dev.crsi.manders.ui.RequestActivity

class ServicesAdapter(private val listRequest: List<ServiceResponse>) :
    RecyclerView.Adapter<ServiceViewHolder>() {
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        mContext = parent.context
        return ServiceViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.services_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        holder.render(listRequest[position], mContext)
    }

    override fun getItemCount(): Int = listRequest.size
}

class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ServicesItemBinding.bind(view)

    fun render(data: ServiceResponse, context: Context) {
        binding.nameService.text = data.name_service
        Picasso.get().load(data.image_service).into(binding.imageService)
        itemView.setOnClickListener {
            val intent = Intent(context, RequestActivity::class.java)
            intent.putExtra("id_service", data.id_service)
            context.startActivity(intent)
        }
    }


}

