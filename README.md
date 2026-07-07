# ZenithPaw
ZenithPaw is an Android productivity app that transforms your deep focus into life support & evolution for your virtual companion. Every non-distracted minute directly nurtures, grows and sustains your ZenithPet. Stay sharp, smash your goals and keep your companions thriving or else they will pay the price.

## Tech Stack & Architecture
- **MVVM** Architecture
- **Jetpack Compose**: Declarative UI framework for Material 3
- **Hilt** (2.54): Dependency injection framework used to manage object lifecycles
- **Room Database** (2.6.1): SQLite object mapping library for local data persistence
- **KSP Kotlin Symbol Processing** (2.1.0-1.0.29): Compiler plugin API for annotation processing for Room & Hilt
- **Coil** (3.0.4): Image loading library for Kotlin Coroutines 
- **Neobrutal Lib** (1.0.9): Specialized UI styling library implementation for modern neobrutalist aesthetic

## Application Diagram Structure
<img width="2461" height="851" alt="ZenithPaw Architecture drawio" src="https://github.com/user-attachments/assets/2cde4fcf-9f9f-4d19-8aaf-1801c73ae4c2" />

## Next Steps CheckList
- Unit, Integration & UI testing
- Connect UI screens with the ViewModels
- Implement the user workflows such a creating tasks, interacting with shop, pets and inventory
- Future plans to implement cloud sync with Google Firebase to go beyond just local data storage
