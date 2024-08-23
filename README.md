# Multiplatform Material You
A port of Android's Monet color palette to Compose Multiplatform, with built-in functionality for retrieving the system accent color on most platforms.

## Compatibility
Multiplatform Material You targets the following platforms:
- Android
- JVM
  - Windows 10 and later
  - macOS Mojave and later
  - Linux (KDE, LXDE, partial support for GNOME)
- iOS
- macOS native (untested)
- JS (partial support)
- Web Assembly (partial support)

## Installation
![Maven Central Version](https://img.shields.io/maven-central/v/dev.zwander/materialyou)

Add the dependency to your `commonMain` source set:

```kotlin
sourceSets {
    val commonMain by getting {
        dependencies {
            implementation("dev.zwander:materialyou:VERSION")
        }
    }
}
```

## Usage
The most basic usage simply involves replacing usages of `MaterialTheme {}` with `DynamicMaterialTheme {}` in your code.

```kotlin
@Composable
fun App() {
    DynamicMaterialTheme {
        Surface {
            //...
        }
    }
}
```

The library will attempt to automatically retrieve the system accent color and dark mode status and generate a theme from those values.

### JS/Web Assembly
The JS and Web Assembly targets assume a web environment, which doesn't allow direct retrieval of the system accent color due to fingerprinting concerns.  
This means Multiplatform Material You can't automatically determine which color to use. As a workaround, you can specify `LocalAccentColor` on these platforms yourself.

```kotlin
@Composable
fun Main() {
    val accentColor = // Hardcoded, from a user preference, etc.
    
    CompositionLocalProvider(
        LocalAccentColor provides accentColor,
    ) {
        App()
    }
}
```

### Advanced Usage
The Composable function `rememberThemeInfo()` is also available in the common target to let you directly retrieve the base color scheme and whether the system is in dark mode.

If you want even more control, you can create an instance of `ColorScheme()` directly yourself, providing an ARGB color integer and whether you want dark mode or not. You can also optionally provide a `Style` to `ColorScheme` to change how it generates the palette.

```kotlin
val seedColor = Color(100, 255, 0).toArgb()
val isDark = isSystemInDarkTheme()

val colorScheme = ColorScheme(
    seed = seedColor,
    darkTheme = isDark,
    style = Style.SPRITZ, // optional
)

val composeColorScheme = colorScheme.toComposeColorScheme()
```
