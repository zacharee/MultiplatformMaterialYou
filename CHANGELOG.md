# 0.4.0
- Update dependencies including a major version upgrade of transitive oshi dependency.
- Update to AGP 9.

# 0.3.0
- Add explicit KDE dark theme detection.
- Add accent color retrieval for Ubuntu Desktop.
- Fix macOS accent color mapping.
- Hopefully fix compatibility with other libraries using other versions of JFA.

# 0.2.10
- Fix swapped red and blue channels on Windows.

# 0.2.9
- Allow overriding dark mode detection.

# 0.2.8
- Fix JFA.

# 0.2.7
- Move away from using korlibs.

# 0.2.6
- Implement accent color fallbacks on Windows.

# 0.2.5
- Update palette generation to use AOSP's new libmonet module.
  - The old monet module is still available, but marked as deprecated.
  - `DynamicMaterialTheme` uses libmonet by default now.

# 0.1.0
- Initial release.
