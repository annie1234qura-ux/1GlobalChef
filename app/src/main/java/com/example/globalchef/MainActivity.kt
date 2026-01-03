package com.example.globalchef

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: RecipeAdapter
    private lateinit var database: RecipeDatabase
    private var allRecipes: List<Recipe> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = RecipeDatabase.getDatabase(this)

        // Preload sample recipes if database is empty
        preloadRecipes()

        // RecyclerView setup
        val recyclerView = findViewById<RecyclerView>(R.id.rvRecipes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Spinner for filtering cuisine
        val spinnerCuisine = findViewById<Spinner>(R.id.spinnerCuisine)
        val cuisines = listOf("All", "Italian", "Indian", "American")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuisines)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCuisine.adapter = spinnerAdapter

        // Search bar
        val etSearch = findViewById<EditText>(R.id.etSearch)

        // Add Recipe button
        val btnAddRecipe = findViewById<Button>(R.id.btnAddRecipe)
        btnAddRecipe.setOnClickListener {
            showAddRecipeDialog()
        }

        // Load recipes from database
        CoroutineScope(Dispatchers.IO).launch {
            allRecipes = database.recipeDao().getAll()
            withContext(Dispatchers.Main) {
                setupAdapter(recyclerView)
                setupSearchAndFilter(etSearch, spinnerCuisine)
            }
        }
    }

    // -------------------------------
    // Setup RecyclerView Adapter
    // -------------------------------
    private fun setupAdapter(recyclerView: RecyclerView) {
        adapter = RecipeAdapter(allRecipes) { recipe ->
            openRecipeDetail(recipe)
        }
        recyclerView.adapter = adapter
    }

    // -------------------------------
    // Setup search bar and spinner filter
    // -------------------------------
    private fun setupSearchAndFilter(etSearch: EditText, spinnerCuisine: Spinner) {

        fun filterRecipes() {
            val searchText = etSearch.text.toString().trim()
            val selectedCuisine = spinnerCuisine.selectedItem.toString()
            var filtered = allRecipes

            // Filter by cuisine
            if (selectedCuisine != "All") {
                filtered = filtered.filter {
                    it.cuisine.equals(selectedCuisine, ignoreCase = true)
                }
            }

            // Filter by search text
            if (searchText.isNotEmpty()) {
                filtered = filtered.filter {
                    it.title.contains(searchText, ignoreCase = true) ||
                            it.ingredients.contains(searchText, ignoreCase = true)
                }
            }

            adapter.updateData(filtered)
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { filterRecipes() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        spinnerCuisine.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                filterRecipes()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    // -------------------------------
    // Open Recipe Detail Activity
    // -------------------------------
    private fun openRecipeDetail(recipe: Recipe) {
        val intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra("recipe", recipe)
        startActivity(intent)
    }

    // -------------------------------
    // Preload sample recipes if DB is empty
    // -------------------------------
    private fun preloadRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = database.recipeDao()
            if (dao.getAll().isEmpty()) {
                val sampleRecipes = listOf(
                    Recipe(
                        id = 0,
                        title = "Spaghetti Carbonara",
                        ingredients = "Spaghetti, Eggs, Parmesan, Pancetta, Pepper",
                        cuisine = "Italian",
                        steps = "1. Boil pasta\n2. Cook pancetta\n3. Mix eggs and cheese\n4. Combine all",
                        createdAt = "2026-01-02"
                    ),
                    Recipe(
                        id = 0,
                        title = "Chicken Tikka Masala",
                        ingredients = "Chicken, Yogurt, Tomato, Spices, Cream",
                        cuisine = "Indian",
                        steps = "1. Marinate chicken\n2. Cook sauce\n3. Combine and simmer",
                        createdAt = "2026-01-02"
                    ),
                    Recipe(
                        id = 0,
                        title = "Veggie Burger",
                        ingredients = "Burger buns, Veggie patty, Lettuce, Tomato, Cheese",
                        cuisine = "American",
                        steps = "1. Cook patty\n2. Assemble burger\n3. Serve",
                        createdAt = "2026-01-02"
                    )
                )
                sampleRecipes.forEach { dao.insert(it) }
                allRecipes = dao.getAll()
            }
        }
    }

    // -------------------------------
    // Show dialog to add new recipe
    // -------------------------------
    private fun showAddRecipeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_recipe, null)

        val etTitle = dialogView.findViewById<EditText>(R.id.etRecipeTitle)
        val spinnerCuisine = dialogView.findViewById<Spinner>(R.id.spinnerCuisineDialog)
        val etIngredients = dialogView.findViewById<EditText>(R.id.etIngredients)
        val etSteps = dialogView.findViewById<EditText>(R.id.etSteps)

        // Spinner values
        val cuisines = listOf("Italian", "Indian", "American", "Desi", "Vegan")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cuisines)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCuisine.adapter = spinnerAdapter

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Recipe")
            .setView(dialogView)
            .setPositiveButton("Save", null) // We'll override click to prevent auto-dismiss
            .setNegativeButton("Cancel") { d, _ -> d.dismiss() }
            .create()

        dialog.setOnShowListener {
            val btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnSave.setOnClickListener {
                val title = etTitle.text.toString().trim()
                val cuisine = spinnerCuisine.selectedItem.toString()
                val ingredients = etIngredients.text.toString().trim()
                val steps = etSteps.text.toString().trim()
                val createdAt = java.time.LocalDate.now().toString()

                if (title.isEmpty()) {
                    Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val newRecipe = Recipe(
                    id = 0, // Important: Room will auto-generate
                    title = title,
                    cuisine = cuisine,
                    ingredients = ingredients,
                    steps = steps,
                    createdAt = createdAt
                )

                CoroutineScope(Dispatchers.IO).launch {
                    database.recipeDao().insert(newRecipe)
                    allRecipes = database.recipeDao().getAll()
                    withContext(Dispatchers.Main) {
                        adapter.updateData(allRecipes)
                        dialog.dismiss()
                    }
                }
            }
        }

        dialog.show()
    }
}
