package com.vik.locusassignment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.comment_item.view.tvTitle
import kotlinx.android.synthetic.main.photo_item.view.*
import kotlinx.android.synthetic.main.single_choice_item.view.*


class ItemsAdapter(val mainActivity: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val map : HashMap<Int, Bitmap> = HashMap()
    val COMMENT = 1
    val PHOTO = 2
    val SINGLE_CHOICE = 3

    var assignmentModel : ArrayList<AssignmentModelItem> =  ArrayList()

    class SingleChoiceItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle : TextView ? = null
        var rbSingleChoice: RadioGroup? = null

        init {
            rbSingleChoice = itemView.rgSingleChoice
            tvTitle = itemView.tvTitle
        }

    }

    inner class PhotoItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView? = null
        var ivPreview: ImageView? = null
        var ivCross: ImageView? = null

        init {
            tvTitle = itemView.tvTitle
            ivPreview = itemView.ivPreview
            ivCross = itemView.ivCross

            ivPreview!!.setOnClickListener {
                mainActivity.openCamera(ivPreview!!, layoutPosition, map)
            }

            ivCross!!.setOnClickListener {
                ivPreview!!.setImageBitmap(null)
                val mIcon = BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.ic_pla)
                map.put(layoutPosition, mIcon!!)
                notifyDataSetChanged()
            }
        }

    }

    class CommentItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle : TextView ? = null
        var switch: Switch? = null
        var commentFrame: FrameLayout? = null

        init {
            tvTitle = itemView.tvTitle
            switch = itemView.switchComment
            commentFrame = itemView.commentFrame
            switch!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                    commentFrame!!.visibility = if (checked) VISIBLE else GONE
                }
            })
        }

    }

    override fun getItemViewType(position: Int): Int {
        return getItemType(position)
    }

    private fun getItemType(position: Int): Int {
        when(assignmentModel.get(position).type){
            "COMMENT" -> return COMMENT
            "PHOTO" -> return PHOTO
            "SINGLE_CHOICE" -> return SINGLE_CHOICE
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var viewHolder : RecyclerView.ViewHolder ?  = null

        when (viewType) {
            COMMENT -> {
                viewHolder = CommentItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.comment_item, parent, false)
                )
            }
            PHOTO -> {
                viewHolder = PhotoItemHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
                )
            }
            SINGLE_CHOICE -> {
                viewHolder = SingleChoiceItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.single_choice_item, parent, false)
                )
            }
        }

        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return assignmentModel.size
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder) {

            is SingleChoiceItemHolder -> {
                val holder1 = holder as SingleChoiceItemHolder
                holder1.tvTitle?.text = assignmentModel.get(position).title
                assignmentModel.get(position).dataMap.options.forEach {
                    addRadioButtons(holder1.rbSingleChoice!!, assignmentModel.get(position).dataMap.options)
                }
            }

            is PhotoItemHolder -> {
                val holder1 = holder as PhotoItemHolder
                holder1.tvTitle?.text = assignmentModel.get(position).title
                if (map.get(position) == null) {
                    val mIcon =
                        BitmapFactory.decodeResource(mainActivity.getResources(), R.drawable.ic_pla)
                    holder1.ivPreview?.setImageBitmap(mIcon)
                } else {
                    val bitmap = map.get(position)
                    holder1.ivPreview?.setImageBitmap(bitmap)
                }

            }

            is CommentItemHolder -> {
                val holder1 = holder as CommentItemHolder
                holder1.tvTitle?.text = assignmentModel.get(position).title
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun addRadioButtons(rgButton: RadioGroup, number: ArrayList<String>) {

        rgButton.removeAllViews()

        number.forEach {
            val rdbtn = RadioButton(mainActivity)
            rdbtn.id = View.generateViewId()
            rdbtn.text = it


            rgButton.addView(rdbtn)
        }
    }


    fun setData(objectArray: AssignmentModel?) {
        this.assignmentModel = objectArray!!
        notifyDataSetChanged()
    }

}
