# Smart Pantry Manager

An Android application (Java) that helps reduce food waste by tracking the
ingredients a user has at home and suggesting recipes that can be made
using strictly those ingredients - no shopping trip required.

Built for the Mobile App Development 700 practical assignment at Richfield
Graduate Institute of Technology.

## Core rule

A recipe is only shown as "suggested" if every ingredient it requires is
present in the pantry, in at least the required quantity. Partial matches
are excluded from the main suggestions list.

## Tech stack

- Java (Android Studio)
- SQLite (SQLiteOpenHelper) for local persistent storage
- RecyclerView with a custom Adapter
- Standard Android Activities and Intents

## Why SQLite

SQLite was chosen over Firebase/PostgreSQL because the app's data (pantry
items, recipes, settings) is inherently local to a single user's device,
does not need real-time sync across devices, and SQLiteOpenHelper is
covered directly in the module content, making it easier to implement and
defend confidently in the video demonstration.

## Project structure

```
app/src/main/java/com/azaldo/smartpantrymanager/
├── activities/     # Screens (Pantry List, Add/Edit, Suggested Recipes, Recipe Detail, Settings)
├── adapters/       # RecyclerView adapters
├── database/       # SQLiteOpenHelper and schema
├── models/         # Plain data classes (PantryItem, Recipe, RecipeIngredient)
├── repositories/   # Data-access layer between Activities and the database
└── utils/          # IngredientNormalizer, RecipeMatcher, etc.
```

## Status

Work in progress. This README will be expanded with setup instructions,
screenshots, and a full features list as development continues.

## Author

Azaldo Mazibuko
