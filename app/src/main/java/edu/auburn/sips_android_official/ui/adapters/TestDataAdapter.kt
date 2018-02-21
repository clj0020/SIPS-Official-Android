package edu.auburn.sips_android_official.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.databinding.DataBindingUtil
import android.os.Build
import android.support.annotation.Nullable
import android.view.ViewGroup
import android.support.v7.util.DiffUtil
import edu.auburn.sips_android_official.R
import edu.auburn.sips_android_official.data.models.TestData
import edu.auburn.sips_android_official.databinding.TestDataItemBinding
import java.util.*


/**
 * Created by clj00 on 2/19/2018.
 */
class TestDataAdapter(@param:Nullable @field:Nullable
                     private val mTestDataClickCallback: TestDataClickCallback) : RecyclerView.Adapter<TestDataAdapter.TestDataViewHolder>() {

    private var mTestDataList: List<TestData>? = null

    fun setTestDataList(testDataArray: List<TestData>) {
        if (mTestDataList == null) {
            mTestDataList = testDataArray
            notifyItemRangeInserted(0, testDataArray.size)
        } else {
            val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return mTestDataList!!.size
                }

                override fun getNewListSize(): Int {
                    return testDataArray.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = mTestDataList!![oldItemPosition]
                    val testData = testDataArray[newItemPosition]
                    return old.id === testData.id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val old = mTestDataList!![oldItemPosition]
                    val testData = testDataArray[newItemPosition]
                    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        (old.id === testData.id
                                && old.testedAt === testData.testedAt
                                && old.athleteId === testData.athleteId
                                && Objects.equals(old.title, testData.title))
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                }
            })
            mTestDataList = testDataArray
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestDataViewHolder {
        val binding = DataBindingUtil
                .inflate<TestDataItemBinding>(LayoutInflater.from(parent.context), R.layout.test_data_item,
                        parent, false)
        binding.setCallback(mTestDataClickCallback)
        return TestDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TestDataViewHolder, position: Int) {
        holder.binding.setTestData(mTestDataList!![position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return if (mTestDataList == null) 0 else mTestDataList!!.size
    }

    class TestDataViewHolder(val binding: TestDataItemBinding) : RecyclerView.ViewHolder(binding.getRoot())
}