package com.example.foodapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.data.Meal
import com.example.foodapp.databinding.ActivityMealBinding
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.fragments.HomeFragment
import com.example.foodapp.viewModel.MealViewModel
import com.example.foodapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String
    private lateinit var mealMvvm: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInfoFromIntent()

        setInfoInView()

        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeButtonClick()
        onFavouriteClick()
    }

    private fun onFavouriteClick() {
        binding.btnAddToFavorites.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeButtonClick() {
        binding.imageYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave: Meal? = null

    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(value: Meal) {
                val meal = value
                mealToSave = meal

                binding.tvCategory.text = "Category: ${meal!!.strCategory}"
                binding.tvArea.text = "Area: ${meal.strArea}"
                binding.tvInstructionsSteps.text = meal.strInstructions

                youtubeLink = meal.strYoutube.toString()
            }
        })
    }

    private fun setInfoInView() {
        Glide.with(application)
            .load(mealThumb)
            .into(binding.imageMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfoFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.btnAddToFavorites.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imageYoutube.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onResponseCase(){
        binding.btnAddToFavorites.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imageYoutube.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }
}