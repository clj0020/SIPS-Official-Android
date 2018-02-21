package edu.auburn.sips_android_official.ui.adapters

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Build
import android.support.annotation.Nullable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.util.DiffUtil
import android.view.View
import edu.auburn.sips_android_official.R
import edu.auburn.sips_android_official.data.models.Athlete
import edu.auburn.sips_android_official.databinding.AthleteItemBinding
import edu.auburn.sips_android_official.ui.athlete.AthleteViewModel
import java.util.*


/**
 * Created by clj00 on 2/19/2018.
 */
class AthleteAdapter(@param:Nullable @field:Nullable
                     private val mAthleteClickCallback: AthleteClickCallback) : RecyclerView.Adapter<AthleteAdapter.AthleteViewHolder>() {

    internal var mAthleteList: List<Athlete>? = null

    fun setAthleteList(athleteList: List<Athlete>) {
        if (mAthleteList == null) {
            mAthleteList = athleteList
            notifyItemRangeInserted(0, athleteList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mAthleteList!!.size
                }

                override fun getNewListSize(): Int {
                    return mAthleteList!!.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return mAthleteList!![oldItemPosition].id === athleteList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newAthlete = athleteList[newItemPosition]
                    val oldAthlete = mAthleteList!![oldItemPosition]
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        (newAthlete.id === oldAthlete.id
                                && Objects.equals(newAthlete.name, oldAthlete.name))
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")

                    }
                }
            })
            mAthleteList = athleteList
            result.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteViewHolder {
        val binding = DataBindingUtil.inflate<AthleteItemBinding>(LayoutInflater.from(parent.context), R.layout.athlete_item,
                        parent, false)
        binding.setCallback(mAthleteClickCallback)
        return AthleteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AthleteViewHolder, position: Int) {
        holder.binding.setAthlete(mAthleteList!![position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (mAthleteList == null) 0 else mAthleteList!!.size
    }

    class AthleteViewHolder(val binding: AthleteItemBinding) : RecyclerView.ViewHolder(binding.getRoot())
}