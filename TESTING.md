# Testing Checklist - Smart Pantry Manager

Run through this on a real emulator/device before recording the video or
writing the report. Tick items off as you confirm them yourself - this
file is a checklist, not proof they've been tested.

## Pantry functionality
- [ ] Add ingredient (name, quantity, unit, no expiry date)
- [ ] Add ingredient with an expiry date
- [ ] View ingredient in the Pantry List
- [ ] Edit an ingredient's quantity
- [ ] Delete an ingredient
- [ ] Close and reopen the app - confirm the ingredient is still there
- [ ] Empty pantry shows the empty-state message and "Add your first ingredient" button
- [ ] Submit the Add form with an empty name - see the inline error
- [ ] Submit the Add form with a negative or non-numeric quantity - see the inline error

## Recipe / strict-matching functionality (the core rule)
- [ ] Add every ingredient a recipe needs, in sufficient quantity - confirm it appears in Suggested Recipes
- [ ] Remove one required ingredient - confirm the recipe disappears
- [ ] Add the ingredient back - confirm the recipe reappears
- [ ] Add a required ingredient but in a quantity below what's needed - confirm the recipe does NOT appear
- [ ] Add an ingredient as "Tomatoes" when a recipe requires "tomato" - confirm it still matches
- [ ] Add an ingredient in ALL CAPS or mixed case - confirm it still matches
- [ ] Add a required ingredient as 1 kg when the recipe needs 500 g - confirm it matches (compatible unit conversion)
- [ ] Add a required ingredient in an incompatible unit (e.g. "pieces" when the recipe needs "g") - confirm it does NOT match
- [ ] Empty pantry - confirm Suggested Recipes shows the no-recipes-match message
- [ ] Open a recipe's detail screen - confirm ingredients and steps display correctly

## UI and stability
- [ ] Navigate Pantry List -> Suggested Recipes -> Recipe Detail -> back -> back
- [ ] Rotate the screen on each screen (if you enable rotation) - confirm no crash
- [ ] Toggle expiry alerts in Settings, close and reopen the app - confirm it stayed toggled
- [ ] Change preferred unit in Settings, close and reopen the app - confirm it stayed changed
- [ ] Clear pantry data from Settings - confirm the confirmation dialog appears and cancelling does nothing
- [ ] Confirm clearing pantry data actually empties the Pantry List

## Automated tests (run via Android Studio's test runner)
- [x] IngredientNormalizerTest - capitalization, plurals, aliases, double-s guard, punctuation
- [x] UnitConverterTest - compatible conversion (g/kg, ml/L), incompatible units refused

## Not yet covered (call out honestly in the report's "Challenges" or "Limitations" section if still true at submission)
- [ ] Instrumented test for RecipeMatcher against a real database
- [ ] Screen rotation / configuration change handling beyond the default Android behaviour
