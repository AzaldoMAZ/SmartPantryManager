# Smart Pantry Manager

An Android application (Java) that helps reduce food waste by tracking the
ingredients a user has at home and suggesting recipes that can be made
using strictly those ingredients - no shopping trip required.

Built for the Mobile App Development 700 practical assignment at Richfield
Graduate Institute of Technology.

## Core rule (strict matching)

A recipe is only shown as "suggested" if every ingredient it requires is
present in the pantry, in at least the required quantity. Partial matches
are excluded from the main suggestions list. See
`utils/RecipeMatcher.java` and `utils/UnitConverter.java`.

## Screens

1. **Pantry List** (`MainActivity`) - view, add, edit and delete pantry
   items; empty-state message when the pantry has nothing in it.
2. **Add/Edit Ingredient** (`AddEditIngredientActivity`) - one screen
   handles both, with validated name/quantity/unit/expiry-date fields.
3. **Suggested Recipes** (`SuggestedRecipesActivity`) - runs the strict
   matching algorithm against the current pantry.
4. **Recipe Detail** (`RecipeDetailActivity`) - full ingredient list and
   preparation steps for a selected recipe.
5. **Settings** (`SettingsActivity`) - expiry alerts toggle and preferred
   unit (both persisted), plus a confirmed "clear pantry data" action.

## Tech stack

- Java (Android Studio)
- SQLite (SQLiteOpenHelper) for local persistent storage
- RecyclerView with custom Adapters (PantryAdapter, RecipeAdapter)
- Standard Android Activities and Intents (only ids are passed through
  Intents, never full objects)

## Why SQLite

SQLite was chosen over Firebase/PostgreSQL because the app's data (pantry
items, recipes, settings) is inherently local to a single user's device,
does not need real-time sync across devices, and SQLiteOpenHelper is
covered directly in the module content, making it easier to implement and
defend confidently in the video demonstration.

## Strict matching in brief

- Ingredient names are normalized (`utils/IngredientNormalizer.java`):
  lowercased, punctuation stripped, a small alias map for common
  singular/plural pairs (tomato/tomatoes, potato/potatoes, onion/onions),
  and a basic trailing-"s" rule for everything else.
- Quantities are only compared within compatible unit groups
  (`utils/UnitConverter.java`): grams/kilograms convert freely,
  millilitres/litres convert freely, but pieces/tablespoons/teaspoons/cups
  never convert into anything else, since that would require an
  ingredient-specific density this app doesn't model.
- A recipe qualifies only if every required ingredient's available pantry
  quantity (summed across matching items, converted where compatible)
  meets the required amount.

## Project structure

```
app/src/main/java/com/azaldo/smartpantrymanager/
├── activities/     # MainActivity, AddEditIngredientActivity, SuggestedRecipesActivity,
│                   # RecipeDetailActivity, SettingsActivity
├── adapters/       # PantryAdapter, RecipeAdapter
├── database/       # DatabaseHelper (schema), RecipeSeeder (20 starter recipes)
├── models/         # PantryItem, Recipe, RecipeIngredient
├── repositories/   # PantryRepository, RecipeRepository, SettingsRepository
└── utils/          # IngredientNormalizer, UnitConverter, RecipeMatcher

app/src/test/java/.../utils/   # JUnit tests: IngredientNormalizerTest, UnitConverterTest
```

## Setup / how to run

1. Open the project folder in Android Studio.
2. Let Gradle sync (requires an internet connection the first time).
3. Run on an emulator or physical device (minSdk 24).
4. The app seeds 20 starter recipes on first launch only.

## Testing

`IngredientNormalizerTest` and `UnitConverterTest` are pure JUnit tests
(no Android dependencies) covering capitalization, plurals, aliases, and
compatible/incompatible unit conversion. Manual test coverage for the
full CRUD and strict-matching flow (add/remove ingredients and watch a
recipe appear/disappear from Suggested Recipes) is documented separately
in the written report.

## Known limitations

- No "Almost There" (missing-one-ingredient) list - out of scope for the
  core requirement.
- Expiry alerts toggle persists but does not yet trigger a notification.

## Author

Azaldo Mazibuko
