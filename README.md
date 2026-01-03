🍽️ GlobalChef – Android Recipe App

GlobalChef is an Android application that allows users to browse, search, filter, and add recipes from different cuisines.
The app is built using Kotlin, RecyclerView, and Room Database, following basic Android best practices.

📱 Features

📋 View a list of recipes

🔍 Search recipes by title or ingredients

🌍 Filter recipes by cuisine using Spinner

➕ Add new recipes using a dialog

📖 View detailed recipe information

💾 Offline storage using Room Database

📱 Runs smoothly on real Android devices

🛠️ Tech Stack

Language: Kotlin

UI: XML Layouts, RecyclerView

Architecture: Basic MVVM-style separation

Database: Room (SQLite)

Async Tasks: Kotlin Coroutines

IDE: Android Studio

📂 Project Structure
com.example.globalchef
│
├── MainActivity.kt
├── RecipeDetailActivity.kt
├── RecipeAdapter.kt
├── Recipe.kt
├── RecipeDao.kt
├── RecipeDatabase.kt
│
├── res/layout
│   ├── activity_main.xml
│   ├── activity_recipe_detail.xml
│   ├── item_recipe.xml
│   ├── dialog_add_recipe.xml
│
└── AndroidManifest.xml

🚀 How to Run the Project

Clone the repository:

git clone https://github.com/yourusername/GlobalChef.git


Open Android Studio

Click Open an Existing Project

Select the GlobalChef folder

Let Gradle sync complete

Run the app on:

Emulator OR

Physical Android phone (USB Debugging enabled)

📸 Screens Included

Home screen with recipe list

Search bar & cuisine filter

Add recipe dialog

Recipe detail screen

⚠️ Requirements

Android Studio Arctic Fox or later

Minimum SDK: 21

Internet not required (offline app)

🧠 Learning Outcomes

This project helped me understand:

RecyclerView & Adapters

Room Database integration

Intents and data passing

Dialogs and user input handling

Coroutines for background tasks

📌 Future Improvements

Image support for recipes

Edit & delete recipes

Dark mode

Firebase integration

👩‍💻 Author

Qurat-ul-ain
Android Developer (Student)

⭐ Support

If you like this project, please ⭐ the repository!
